package com.zxq.globalwindow.service.impl;
import com.zxq.globalwindow.controller.NlpController;
import com.zxq.globalwindow.pojo.news;
import com.zxq.globalwindow.service.eventService;
import com.zxq.globalwindow.service.keywordService;
import com.zxq.globalwindow.service.newsService;
import com.zxq.globalwindow.utils.attitudeChange;
import com.zxq.globalwindow.utils.timeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import com.zxq.globalwindow.mapper.newsMapper;
import com.zxq.globalwindow.mapper.news_keywordsMapper;
import com.zxq.globalwindow.utils.jaccardUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

@Service
public class newsServiceImpl implements newsService{

    @Autowired
    private newsMapper newsMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private eventService eventService;
    @Autowired
    private news_keywordsMapper news_keywordsMapper;
    @Autowired
    private keywordService keywordService;
    @Autowired
    private NlpController NlpController;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Integer readData(Integer startIndex, Integer batchSize){
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
        Integer result = startIndex;
        for(int i = startIndex ; i < (startIndex + batchSize) ; i++){
            if(i >= 1627 && i <= 1674){
                i = Math.min((startIndex + batchSize - 1), 1674);
                result = i;
                continue;
            }
            System.err.println(i);
            news news = findBynewsId(i);
            if(news == null){
                System.err.println("已经到底了！");
                return result;
            }
            String eventId = news.getEventId();
            if(!eventId.isEmpty()){
                result++;
                Set<String> keywords = eventService.getKeywordsByeventId(eventId);
                for(String keyword : keywords){
                    keywordService.setkeyword(keyword);
                    news_keywordsMapper.setnews_keywords(news.getNewsId(),keyword);
                }
                System.err.println("continue");
                continue;
            }

            ResponseEntity<?> response = NlpController.callNlpService(news.getNews());
            String keyword = (String) response.getBody();

            String[] newkey = keyword.split(",");

            Set<String> newkeys = new HashSet<>();
            for (String kw : newkey) {
                newkeys.add(kw.trim().toLowerCase());
                keywordService.setkeyword(kw);
                news_keywordsMapper.setnews_keywords(news.getNewsId(),kw);
            }
            double max = 0;
            String key = keyword;

            Set<String> redisKeys = operations.getOperations().keys("Id:*");

            for (String rediskey : redisKeys) {
                String redisKey = operations.get(rediskey);
                String[] redisKeywords = redisKey.split(",");
                Set<String> normalizedRedisKeywords = new HashSet<>();
                for (String kw : redisKeywords) {
                    normalizedRedisKeywords.add(kw.trim().toLowerCase());
                }
                double similarity = jaccardUtils.calculateJaccardSimilarity(newkeys, normalizedRedisKeywords);

                // 检查相似度是否>= 60%
                if (similarity >= 0.6) {
                    if (similarity > max){
                        max = similarity;
                        key = operations.get(rediskey);
                    }
                    else{
                        continue;
                    }
                }
                else{
                    continue;
                }
            }

            if (key.equals(keyword)){
                operations.set("Id:"+key,keyword,5, TimeUnit.DAYS);
                operations.set("text:0:"+key,news.getNews(),5,TimeUnit.DAYS);
                operations.set("popularity:"+key,news.getPopularity().toString(),5,TimeUnit.DAYS);
                setnewsEventId("Id:"+key,news.getNewsId());
                String rankList = "rankList";
                redisTemplate.opsForZSet().add(rankList,key,news.getPopularity());
            }else {
                String oldPopularity = operations.get("popularity:"+key);

                String rankList = "rankList";
                redisTemplate.opsForZSet().incrementScore(rankList,key,news.getPopularity());

                Integer newPopularity =  Integer.parseInt(oldPopularity) + news.getPopularity();
                operations.set("popularity:"+key,newPopularity.toString(),5,TimeUnit.DAYS);
            }
            result++;
        }
        System.out.println(result);
        return result;
    }

    @Override
    public List<news> getNewsByX(Integer limitNum){
        return newsMapper.getNewsByX(limitNum);
    }

    @Cacheable(cacheNames = "news")
    @Override
    public news findBynewsId(Integer newsId) {
        return newsMapper.findBynewsId(newsId);
    }

    @Override
    public news findBynewsIdR(Integer newsId) {
        return newsMapper.findBynewsId(newsId);
    }

    //@CachePut(cacheNames = "hotnews")
    @Override
    public List<news> findBypopularity(Integer num){
        return newsMapper.findBypopularity(num);
    }

    @Override
    public String getlinkBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

            String link = news.getLink();
            return link;
    }

    @Override
    public String geteventIdBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

        String eventId = news.getEventId();
        return eventId;
    }

    @Override
    public List<news> findBytype(String type){
        return newsMapper.findBytype(type);
    }

    @Override
    public List<news> findHotBytype(String type, Integer num) { return newsMapper.findHotBytype(type,num);}

    @Override
    public List<news> findBykeyword(String singlekeyword){
        List<Integer> newsIdList = news_keywordsMapper.getnewsIdBykeyword(singlekeyword);
        List<news> newsList = new ArrayList<>();
        for(Integer newsId : newsIdList){
            news news = newsMapper.findBynewsId(newsId);
            newsList.add(news);
        }
        return newsList;
    }

    @Override
    public List<news> findHotBykeyword(String singlekeyword,Integer num){
        List<Integer> newsIdList = news_keywordsMapper.getHotnewsIdBykeyword(singlekeyword,num);
        List<news> newsList = new ArrayList<>();
        for(Integer newsId : newsIdList){
            news news = newsMapper.findBynewsId(newsId);
            newsList.add(news);
        }
        return newsList;
    }

    @Override
    public List<String> getNewsImages(Integer num){
        return newsMapper.getNewsImages(num);
    }

    @Override
    public List<news> findByeventId(String eventId){return newsMapper.findByeventId(eventId);}

    @Override
    public List<news> findHotByeventId(String eventId, Integer num){return newsMapper.findHotByeventId(eventId,num);}

    @Override
    public List<news> findBysource(String source) {return newsMapper.findBysource(source);}

    @Override
    public List<news> findHotBysource(String source, Integer num){return newsMapper.findHotBysource(source,num);};

    @Override
    public String getnewsBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

            String newstext = news.getNews();
            return newstext;
    }

    @Override
    public Integer getpopulairtyBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

            Integer popularity = news.getPopularity();
            return popularity;
    }

    @Override
    public Integer getnewsIdBylink(String link){
        return newsMapper.getnewsIdBylink(link);
    }

    @Override
    public String getpicturelinkBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

            String picturelink = news.getPicturelink();
            return picturelink;
    }

    @Override
    public String getsourceBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

        String source = news.getSource();
        return source;
    }

    @Override
    public String getattitudeBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

        Integer attitude = news.getAttitude();
        String Attitude = attitudeChange.getAttitude(attitude);
        return Attitude;
    }

    @Override
    public String gettimeBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

        String time = timeFormatter.timeFormatter(news.getTime());
        return time;
    }

    @Override
    public String getemotionBynewsId(Integer newsId){
        news news = findBynewsId(newsId);

        String emotion = news.getEmotion();
        return emotion;
    }

    /*@Override
    public boolean setnews(String news, String type, Integer popularity, String link, String picturelink, String source, Integer attitude, String time, String emotion, String keyword){
        double max = 0;
        String key = keyword;

        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();

        String[] inputKeywords = keyword.split(",");
        Set<String> normalizedInputKeywords = new HashSet<>();
        for (String kw : inputKeywords) {
            normalizedInputKeywords.add(kw.trim().toLowerCase());
        }

        Set<String> redisKeys = operations.getOperations().keys("Id:");

        for (String rediskey : redisKeys) {
            String redisKey = operations.get(rediskey);
            String[] redisKeywords = redisKey.split(",");
            Set<String> normalizedRedisKeywords = new HashSet<>();
            for (String kw : redisKeywords) {
                normalizedRedisKeywords.add(kw.trim().toLowerCase());

                keywordMapper.setkeyword(keyword);

            }
            double similarity = jaccardUtils.calculateJaccardSimilarity(normalizedInputKeywords, normalizedRedisKeywords);

            // 检查相似度是否>= 60%
            if (similarity >= 0.6) {
               if (similarity > max){
                   max = similarity;
                   key = operations.get(rediskey);
               }
               else{
                   continue;
               }
            }
            else{
                continue;
            }
        }

        if (key == keyword){
            operations.set("Id:"+key,keyword);
            operations.set("popularity:"+key,popularity.toString());

            String rankList = "rankList";
            redisTemplate.opsForZSet().add(rankList,key,popularity);

            newsMapper.setnews(news,type,popularity,link,picturelink,source,attitude,time,emotion,"Id:"+key);
        }else {
            String oldPopularity = operations.get("popularity:"+key);

            String rankList = "rankList";
            redisTemplate.opsForZSet().incrementScore(rankList,key,popularity);

            Integer newPopularity =  Integer.parseInt(oldPopularity) + popularity;
            operations.set("popularity:"+key,newPopularity.toString());

            newsMapper.setnews(news,type,popularity,link,picturelink,source,attitude,time,emotion,"Id:"+key);
        }

        Integer newsId = newsMapper.getnewsIdBylink(link);

        for (String kw : inputKeywords) {
            news_keywordsMapper.setnews_keywords(newsId,kw.trim().toLowerCase());
        }
        return true;
    }*/

    @Override
    public boolean setnewsEventId(String eventId,Integer newsId){
        newsMapper.setnewsEventId(eventId,newsId);
        return true;
    }

    @Override
    public boolean updatenews_popularity(Integer popularity, Integer newsId){
        newsMapper.updatenews_popularity(popularity,newsId);
        return true;
    }

    @Override
    public boolean updatenews_type(String type, Integer newsId){
        newsMapper.updatenews_type(type,newsId);
        return true;
    }
}
