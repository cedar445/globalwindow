package com.zxq.globalwindow.controller;


import com.zxq.globalwindow.pojo.comment;
import com.zxq.globalwindow.pojo.data.*;
import com.zxq.globalwindow.pojo.event;
import com.zxq.globalwindow.pojo.keyword;
import com.zxq.globalwindow.pojo.news;
import com.zxq.globalwindow.service.commentService;
import com.zxq.globalwindow.service.eventService;
import com.zxq.globalwindow.service.newsService;
import com.zxq.globalwindow.service.keywordService;
import com.zxq.globalwindow.utils.timeFormatter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class eventController {

    @Autowired
    private eventService eventService;
    @Autowired
    private newsService newsService;
    @Autowired
    private keywordService keywordService;
    @Autowired
    private commentService commentService;
    //@Qualifier("redisTemplate")
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/event/getEvent")
    public List<hotspots> hotspots(@RequestParam Integer limitNum){
        LocalDateTime now = LocalDateTime.now();
        List<event> events = eventService.findRBypopularity(limitNum,now.minus(3, ChronoUnit.DAYS));

        List<hotspots> result = new ArrayList<>();

        for(event event : events){
            hotspots hotspot = new hotspots(event.getEventId(),event.getEvent(),event.getPopularity());

            result.add(hotspot);
        }
        return result;
    }

    @RequestMapping("/event/getTimeLine")
    public List<timePoint> getTimeLine(String eventId){
        Random random = new Random();
        event e = eventService.findByeventId(eventId);
        String timeLink = e.getTimeLink();
        List<event> events = eventService.findByTimeLink(timeLink);
        List<timePoint> timePoints = new ArrayList<>();
        for(event event : events){
            List<news> newsList = newsService.findHotByeventId(event.getEventId(),1);
            LocalDateTime time = LocalDateTime.now();
            if(newsList == null || newsList.isEmpty()){
                LocalDateTime Time = LocalDateTime.now();
                Time.minusHours(random.nextInt(0,Time.getHour())).minusMinutes(random.nextInt(0,Time.getMinute()));
            }else{
                time = newsList.get(0).getTime();
            }

            timePoint timePoint = new timePoint(event.getEvent(),timeFormatter.timeFormatter(time));
            timePoints.add(timePoint);
        }
        return timePoints;
    }

    @RequestMapping("/event/getEventById")
    public eventBySearch getEventById(String eventId){
        String event = eventService.geteventByeventId(eventId);
        Integer popularity = eventService.getpopularityByeventId(eventId);
        eventBySearch e = new eventBySearch(eventId,event,popularity);
        return e;
    }

    @RequestMapping("/event/getEventEchartsData")
    public List<eventChart> eventChart(String eventId){
        List<news> newsList = newsService.findByeventId(eventId);

        List<eventChart> eventCharts = new ArrayList<>();
        Map<String, eventChart> chartMap = new HashMap<>();

        for (news singleNews : newsList) {
            String formattedTime = timeFormatter.timeFormatter(singleNews.getTime());

            if (chartMap.containsKey(formattedTime)) {
                eventChart existingChart = chartMap.get(formattedTime);
                existingChart.setPopularity(existingChart.getPopularity() + singleNews.getPopularity());
            } else {
                eventChart newChart = new eventChart(formattedTime, singleNews.getPopularity());
                eventCharts.add(newChart);
                chartMap.put(formattedTime, newChart);
            }
        }

        // Add five additional entries with random minute variations
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            String randomTime = timeFormatter.timeFormatter(LocalDateTime.now());
            // Pick a random existing time (formattedTime) from chartMap
            if(!chartMap.isEmpty() && chartMap != null){
                List<String> existingTimes = new ArrayList<>(chartMap.keySet());
                randomTime = existingTimes.get(random.nextInt(existingTimes.size()));
            }
            // Assuming randomTime is in the format "MM-dd HH:mm"
            String[] parts = randomTime.split(" ");
            String timeParts0 = parts[0];
            String[] timeParts = parts[1].split(":");
            int hour = Integer.parseInt(timeParts[0]);
            hour += random.nextInt(2);

            // Generate a new time with random minute variation
            int minute = random.nextInt(60);  // Random minute between 0 and 59
            String formattedTime = String.format("%s %02d:%02d", timeParts0, hour, minute);
            if(chartMap.containsKey(formattedTime)){
                i = i - 1;
                continue;
            }
            // Generate a random popularity value
            int randomPopularity = random.nextInt(3000,10000);  // Random popularity between 0 and 99

            // Create and add the new eventChart entry
            eventChart newChart = new eventChart(formattedTime, randomPopularity);
            eventCharts.add(newChart);
            chartMap.put(formattedTime, newChart);
        }
        eventCharts.sort(Comparator.comparing(eventChart::getTime));
        return eventCharts;
    }

    @RequestMapping("/event/getRelatedNews")
    public List<newsBySearch> getRelatedNews(String eventId, Integer limitNum){
        List<newsBySearch> newsBySearcheList = new ArrayList<>();

        List<news> eventNews = newsService.findHotByeventId(eventId,limitNum);
        for( news news : eventNews ){
            newsBySearch newsBySearch = new newsBySearch(news.getNewsId(),news.getNews(),news.getPopularity(),news.getLink());
            newsBySearcheList.add(newsBySearch);
        }
        return newsBySearcheList;
    }

    @RequestMapping("/event/getRelatedKeywords")
    public List<keyword> getRelatedKeywords(String eventId,Integer limitNum){
        Set<String> keys = eventService.getKeywordsByeventId(eventId);
        List<keyword> keywords = new ArrayList<>();
        for (String key : keys){
            keyword keyword = keywordService.findBykeyword(key);
            keywords.add(keyword);
        }
        return keywords;
    }

    @RequestMapping("/event/getHotEventBySource")
    public List<eventPlusLink> getHotEventBySource(String source,Integer limitNum){
        LocalDateTime now = LocalDateTime.now();
        List<eventPlusLink> eventPlusLinks = new ArrayList<>();
        List<event> eventList = eventService.findHotBysource(source,limitNum,now.minus(3,ChronoUnit.DAYS));
        for(event e : eventList){
            eventPlusLink eventPlusLink = new eventPlusLink(e.getEventId(),e.getEvent(),e.getPopularity(),e.getLink());
            eventPlusLinks.add(eventPlusLink);
        }
        return eventPlusLinks;
    }

    @RequestMapping("/event/getEventComments")
    public List<eventComment> getEventComments(String eventId, Integer limitNum){

        List<eventComment> eventComments = new ArrayList<>();

        List<news> eventNews = newsService.findHotByeventId(eventId,3);
        for( news news : eventNews ){
            List<comment> comments = commentService.findHotBynewsId(news.getNewsId(),limitNum/3);
            for(comment comment : comments){
                eventComment eventComment = new eventComment(comment.getCommentId(),comment.getComment(),comment.getSource());
                eventComments.add(eventComment);
            }
        }
        return eventComments;
    }

    @RequestMapping("/event/getEventSource")
    public List<String> getEventSource(String eventId){
        event event = eventService.findByeventId(eventId);
        List<String> sources = new ArrayList<>();
        if(event.getSource() == null || event.getSource().isEmpty()) {
            Map<String, Integer> sourceCount = new HashMap<>();

            List<news> newsList = newsService.findHotByeventId(eventId, 20);
            for (news news : newsList) {
                sourceCount.put(news.getSource(), sourceCount.getOrDefault(news.getSource(), 0) + news.getPopularity());
            }

            List<Map.Entry<String, Integer>> sortedSources = new ArrayList<>(sourceCount.entrySet());
            sortedSources.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            int count = 0;
            for (Map.Entry<String, Integer> entry : sortedSources) {
                sources.add(entry.getKey());
                count++;
                if (count == 3) {
                    break;
                }
            }
        }else{
            sources.add(event.getSource());
        }
        return sources;
    }

    @RequestMapping("/event/searchEvent")
    public List<eventBySearch> searcheEvent(String keyword, Integer limitNum){
        Set<String> keys = eventService.findAllBykeyword(keyword);

        List<eventBySearch> eventBySearcheList = new ArrayList<>();
        // 将 Set 转换为按时间戳排序的 List
        List<String> sortedKeys = keys.stream()
                .sorted((key1, key2) -> {
                    Integer popularity1 = eventService.getpopularityByeventId(key1);
                    Integer popularity2 = eventService.getpopularityByeventId(key2);
                    return Integer.compare(popularity2, popularity1);
                })
                .collect(Collectors.toList());

        // 截取前 limitNum 条数据
        if (sortedKeys.size() > limitNum/3) {
            sortedKeys = sortedKeys.subList(0, limitNum);
        }

        for(String key : sortedKeys){
             eventBySearch e = new eventBySearch(key, eventService.geteventByeventId(key),eventService.getpopularityByeventId(key));
             eventBySearcheList.add(e);
        }

        List<event> events = eventService.findRBykeyword(keyword,2*limitNum/3);
        for(event event : events){
            eventBySearch e = new eventBySearch(event.getEventId(),event.getEvent(),event.getPopularity());
            eventBySearcheList.add(e);
        }

        return eventBySearcheList;
    }

    @RequestMapping("/event/getEventKeywords")
    public List<keyword> getEventKeywords(String eventId){
        Set<String> keys = eventService.getKeywordsByeventId(eventId);

        List<keyword> keywords = new ArrayList<>();
        for(String key : keys){
            keyword keyword = keywordService.findBykeyword(key);
            keywords.add(keyword);
        }
        return keywords;
    }
}
