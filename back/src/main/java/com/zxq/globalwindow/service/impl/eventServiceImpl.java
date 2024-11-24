package com.zxq.globalwindow.service.impl;

import com.zxq.globalwindow.pojo.event;
import com.zxq.globalwindow.pojo.timeLink;
import com.zxq.globalwindow.service.eventService;
import com.zxq.globalwindow.service.timeLinkService;
import com.zxq.globalwindow.utils.jaccardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.zxq.globalwindow.mapper.eventMapper;
import com.zxq.globalwindow.mapper.timeLinkMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class eventServiceImpl implements eventService{

    @Autowired
    private eventMapper eventMapper;
    @Autowired
    private timeLinkService timeLinkService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean readData(){
        List<event> events = eventMapper.findAllEvent();
        for(event event : events){
            Set<timeLink> timeLinks = timeLinkService.findAlltimeLink();
            if(event.getTimeLink() == null && timeLinks != null && !timeLinks.isEmpty()){
                String[] newkey = event.getKeyword().split(",");
                Set<String> newkeys = new HashSet<>();
                for (String kw : newkey) {
                    newkeys.add(kw.trim().toLowerCase());
                }
                double max = 0;
                String key = event.getKeyword();
                for (timeLink t : timeLinks) {
                    String TimeLink = t.getTimeLink();
                    String[] TimeLinks = TimeLink.split(",");
                    Set<String> normalizedTimeLinkKeywords = new HashSet<>();
                    for (String tl : TimeLinks) {
                        normalizedTimeLinkKeywords.add(tl.trim().toLowerCase());
                    }
                    double similarity = jaccardUtils.calculateJaccardSimilarity(newkeys, normalizedTimeLinkKeywords);

                    // 检查相似度是否>= 50%
                    if (similarity >= 0.5) {
                        if (similarity > max){
                            max = similarity;
                            key = t.getTimeLink();
                        }else{
                            continue;
                        }
                    }else{
                        continue;
                    }

                }

                if (key.equals(event.getKeyword())){
                    timeLinkService.setTimeLink(key);
                    eventMapper.setTimeLink(key,event.getEventId());
                }else {
                    eventMapper.setTimeLink(key,event.getEventId());
                }
            }else if(event.getTimeLink() == null && timeLinks.isEmpty()){
                timeLinkService.setTimeLink(event.getKeyword());
                eventMapper.setTimeLink(event.getKeyword(),event.getEventId());
            }
            else{
                continue;
            }
        }
        return true;
    }

    @Override
    public boolean findAllEvent(){
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();

        List<event> events =  eventMapper.findAllEvent();
        for(event event : events){
            operations.set(event.getEventId(),event.getKeyword(),5, TimeUnit.DAYS);
            operations.set("text:"+event.getKeyword(),event.getEvent(),5,TimeUnit.DAYS);
            operations.set("popularity:"+event.getKeyword(),event.getPopularity().toString(),5,TimeUnit.DAYS);
        }
        return true;
    }

    @Override
    public boolean setAllEvent(){

        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();

        Set<String> redisKeys = operations.getOperations().keys("Id:*");
        if(redisKeys==null||redisKeys.isEmpty()){return false;}
        for (String rediskey : redisKeys) {
            String key = operations.get(rediskey);
            String content = operations.get("text:"+key);
            String popularity = operations.get("popularity:"+key);
            if(key == "nil" || content == "nil" || popularity == "nil"){
                try {
                    // Attempt to parse popularityStr into an integer
                    Integer newPopularity = Integer.parseInt(popularity);

                    // Call eventMapper to set the event
                    eventMapper.setevent(rediskey, key, content, newPopularity);
                } catch (NumberFormatException e) {
                    // Handle the case where popularityStr cannot be parsed as an integer
                    // Log an error or handle it according to your application's logic
                    System.err.println("Error parsing popularity for eventId " + ": " + e.getMessage());
                    // You may choose to skip this event or handle it differently
                }
            } else {
                // Handle the case where popularityStr is null or empty
                System.err.println("value is null or empty for eventId ");
                // You may choose to skip this event or handle it differently
            }
        }
        return true;
    }

    @Override
    public event findByeventId(String eventId){
        return eventMapper.findByeventId(eventId);
    }

    @Override
    public Set<String> findBypopularity(Integer num){
        String rankList = "rankList";

        Set<String> hotpots = redisTemplate.opsForZSet().reverseRange(rankList,0,(num-1));

        return hotpots;
    }

    @Override
    public List<event> findRBykeyword(String keyword, Integer num){
        return eventMapper.findRBykeyword(keyword,num);
    }

    @Override
    public List<event> findRBypopularity(Integer num,LocalDateTime time){
        return eventMapper.findRBypopularity(num,time);
    }

    @Override
    public  List<event> findByTimeLink(String timeLink){
        return eventMapper.findByTimeLink(timeLink);
    }

    @Override
    public String geteventByeventId(String eventId){
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
        String keys = operations.get(eventId);
        if(keys != null && !keys.isEmpty()){
            return operations.get("text:0:" +keys);
        }else {
            event event = eventMapper.findByeventId(eventId);
            return event.getEvent();
        }
    }

    @Override
    public Integer getpopularityByeventId(String eventId){
        Random random = new Random();
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
        String keys = operations.get(eventId);
        if(keys != null && !keys.isEmpty()){
            Integer popularity = Integer.parseInt(operations.get("popularity:"+operations.get(eventId)));
            return popularity;
        }else {
            event event = eventMapper.findByeventId(eventId);
            return event.getPopularity();
        }
    }

    @Override
    public List<event> findHotBysource(String source, Integer num, LocalDateTime time){
        List<event> events = eventMapper.findHotBysource(source,num,time);
        return events;
    }

    @Override
    public boolean seteventTextBykeyword(String text,String eventId){
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();

        operations.set("text:"+operations.get(eventId),text,5,TimeUnit.DAYS);
        return true;
    }

    @Override
    public Set<String> getKeywordsByeventId(String eventId){
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();

        try {
            System.err.println(eventId);
            String keys = operations.get(eventId);
            if (keys == null || keys.equals("nil")) {
                keys = eventMapper.getkeywordsByeventId(eventId);
            }

            Set<String> normalizedInputKeywords = new HashSet<>();
            String[] inputKeywords = keys.split(",");

            for (String kw : inputKeywords) {
                normalizedInputKeywords.add(kw.trim().toLowerCase());
            }

            return normalizedInputKeywords;
        } catch (Exception e) {
            // Handle any exceptions that occur during Redis operation or processing
            // Logging the exception or other appropriate actions can be taken here
            e.printStackTrace(); // Example: Print stack trace for debugging
            return Collections.emptySet(); // Return an empty set in case of any exception
        }
    }

    @Override
    public Set<String> findAllBykeyword(String keyword){
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();

        Set<String> keys = redisTemplate.keys("Id:"+"*" + keyword + "*");
        return keys;
    }
}
