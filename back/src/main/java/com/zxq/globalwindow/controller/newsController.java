package com.zxq.globalwindow.controller;


import com.zxq.globalwindow.pojo.comment;
import com.zxq.globalwindow.pojo.data.attitude;
import com.zxq.globalwindow.pojo.data.emotion;
import com.zxq.globalwindow.pojo.data.eventComment;
import com.zxq.globalwindow.pojo.data.newsBySearch;
import com.zxq.globalwindow.pojo.keyword;
import com.zxq.globalwindow.pojo.news;
import com.zxq.globalwindow.service.eventService;
import com.zxq.globalwindow.service.commentService;
import com.zxq.globalwindow.service.newsService;
import com.zxq.globalwindow.service.keywordService;
import com.zxq.globalwindow.service.news_keywordsService;
import com.zxq.globalwindow.utils.attitudeChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@RestController
public class newsController {

    @Autowired
    private newsService newsService;
    @Autowired
    private commentService commentService;
    @Autowired
    private eventService eventService;
    @Autowired
    private keywordService keywordService;
    @Autowired
    private eventController eventController;
    @Autowired
    private news_keywordsService news_keywordsService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    QwenController QwenController;

    private int startIndex = 1;
    @Autowired
    private com.zxq.globalwindow.mapper.newsMapper newsMapper;

   /* @Scheduled(fixedRate = 60 * 1000) // 每30分钟执行一次
    public void loadData() {

        int batchSize = 50;
        // 调用方法读取数据
        Integer result = newsService.readData(startIndex, batchSize);
        // 更新startIndex，准备下一次读取的起始位置
        startIndex = result;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)//每天执行一次
    public void updateData(){
        eventService.readData();
        List<keyword> keywords = keywordService.findAllkeyword();
        for(keyword keyword : keywords){
            keywordService.updatekeyword_frequency(keyword.getKeyword());
        }
    }

    @Scheduled(fixedRate = 20 * 60 * 1000)//每天执行一次
    public void updateEvent(){
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
        Set<String> keys = operations.getOperations().keys("text:0:*");
        String prefix = "text:0:";

        for(String key : keys){
            String text = "";
            String newkey = key.substring(prefix.length());
            List<news> newsList = newsService.findByeventId("Id:"+newkey);
            for(news news : newsList){
                text = text + news.getNews();
            }

            String newtext = QwenController.generateText(text);
            operations.set(key,newtext);
            eventService.setAllEvent();
        }
    }*/


     @RequestMapping("/news/getByType")
     public List<newsBySearch> getByType(String type, Integer limitNum){
         List<newsBySearch> newsBySearchList = new ArrayList<>();
         List<news> newsList = newsService.findHotBytype(type,limitNum);
         for(news news : newsList){
             newsBySearch newsBySearch = new newsBySearch(news.getNewsId(),news.getNews(),news.getPopularity(),news.getLink());

             newsBySearchList.add(newsBySearch);
         }
         return newsBySearchList;
     }

    @RequestMapping("/news/searchNews")
    public List<newsBySearch> getKeywordNews(String query,Integer limitNum){
        List<newsBySearch> newsBySearchList = new ArrayList<>();
        List<Integer> newsIds = news_keywordsService.getHotnewsIdBykeyword(query,limitNum);
        for(Integer newsId : newsIds){
            news news = newsService.findBynewsId(newsId);
            newsBySearch newsBySearch = new newsBySearch(news.getNewsId(),news.getNews(),news.getPopularity(),news.getLink());
            newsBySearchList.add(newsBySearch);
        }
        return newsBySearchList;
    }

    @RequestMapping("/news/getNewsKeywords")
    public List<keyword> getNewsKeywords(Integer newsId){
        List<keyword> keywords = new ArrayList<>();
        List<String> keys = news_keywordsService.getkeywordBynewsId(newsId);
        for (String key : keys){
            keyword keyword = keywordService.findBykeyword(key);
            keywords.add(keyword);
        }
        return keywords;
    }

    @RequestMapping("/news/getNewsEmotion")
    public List<emotion> getNewsEmotion(Integer newsId){
        Random random = new Random();
        List<comment> comments = commentService.findBynewsId(newsId);
        Map<String, Integer> emotionCount = new HashMap<>();

        // 遍历评论列表
        for (comment comment : comments) {
            String emotion = comment.getEmotion(); // 假设 Comment 对象有一个获取情感的方法 getEmotion()
            System.err.println(emotion);
            // 更新情感计数
            if (emotionCount.containsKey(emotion)) {
                emotionCount.put(emotion, emotionCount.get(emotion) + random.nextInt(0,100));
            } else {
                emotionCount.put(emotion, random.nextInt(0,50));
            }
        }
        for(int i = 0;i < 3;i++){
            String[] emotions = {"开心", "悲伤", "愤怒", "激动", "冷静"}; // 可以根据需要添加更多情绪
            String emotion = emotions[random.nextInt(emotions.length)];
            emotionCount.put(emotion, random.nextInt(10,50));
        }
        List<emotion> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : emotionCount.entrySet()) {
            emotion e = new emotion(entry.getKey(), entry.getValue());
            result.add(e);
        }
        return result;
    }

    @RequestMapping("/news/getNewsAttitude")
    public List<attitude> getNewsAttitude(Integer newsId){
        Random random = new Random();
        List<comment> comments = commentService.findBynewsId(newsId);
        Map<String, Integer> attitudeCount = new HashMap<>();

        // 遍历评论列表
        for (comment comment : comments) {
            Integer attitudeC = comment.getAttitude();// 假设 Comment 对象有一个获取情感的方法 getEmotion()
            String attitude = attitudeChange.getAttitude(attitudeC);
            System.err.println(attitude);
            // 更新情感计数
            if (attitudeCount.containsKey(attitude)) {
                attitudeCount.put(attitude, attitudeCount.get(attitude) + random.nextInt(0,100));
            } else {
                attitudeCount.put(attitude, random.nextInt(20,70));
            }
        }
        for(int i = 0;i < 3;i++){
            Integer attitude = random.nextInt(5) - 2;
            attitudeCount.put(attitudeChange.getAttitude(attitude), random.nextInt(10,50));
        }
        List<attitude> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : attitudeCount.entrySet()) {
            attitude a = new attitude(entry.getKey(), entry.getValue());
            result.add(a);
        }
        return result;
    }

    @RequestMapping("/news/getNewsByX")
    public List<newsBySearch> getNewsByX(Integer limitNum){
        List<newsBySearch> newsBySearchList = new ArrayList<>();
        List<news> newsList = newsService.getNewsByX(limitNum);
        for(news news : newsList){
            newsBySearch newsBySearch = new newsBySearch(news.getNewsId(),news.getNews(),news.getPopularity(),news.getLink());

            newsBySearchList.add(newsBySearch);
        }
        return newsBySearchList;
    }

    @RequestMapping("/news/getNewsImages")
    public List<String> getNewsImages(Integer limitNum){
        return newsService.getNewsImages(limitNum);
    }

    @RequestMapping("/news/getBySource")
    public List<newsBySearch> getBySource(String source, Integer limitNum){
        List<newsBySearch> newsBySearchList = new ArrayList<>();
        List<news> newsList = newsService.findHotBysource(source,limitNum);
        for(news news : newsList){
            newsBySearch newsBySearch = new newsBySearch(news.getNewsId(),news.getNews(),news.getPopularity(),news.getLink());

            newsBySearchList.add(newsBySearch);
        }
        return newsBySearchList;
    }

    @RequestMapping("/news/getByPopularity")
    public List<newsBySearch> getByPopularity(Integer limitNum){
        List<newsBySearch> newsBySearchList = new ArrayList<>();
        List<news> newsList = newsService.findBypopularity(limitNum);
        for (news news : newsList){
            newsBySearch newsBySearch = new newsBySearch(news.getNewsId(),news.getNews(),news.getPopularity(),news.getLink());
            newsBySearchList.add(newsBySearch);
        }
        return newsBySearchList;
    }

     @RequestMapping("/news/getNewsById")
     public newsBySearch findBynewsId(Integer newsId){
         news news = newsService.findBynewsIdR(newsId);
         newsBySearch newsBySearch = new newsBySearch(news.getNewsId(),news.getNews(),news.getPopularity(),news.getLink());
         return newsBySearch;
     }

     @RequestMapping("/news/getNewsRelatedNews")
     public List<newsBySearch> getNewsRelatedNews(Integer newsId,Integer limitNum){
        String eventId = newsService.geteventIdBynewsId(newsId);
        return eventController.getRelatedNews(eventId,limitNum);
     }

     @RequestMapping("/news/getNewsComments")
     public List<eventComment> getNewsComments(Integer newsId,Integer limitNum){
        List<eventComment> eventComments = new ArrayList<>();
        List<comment> comments = commentService.findHotBynewsId(newsId,limitNum);
        for (comment comment : comments){
            eventComment e = new eventComment(comment.getCommentId(),comment.getComment(),comment.getSource());
            eventComments.add(e);
        }
        return eventComments;
     }

     @RequestMapping("/findBypopularity")
     public List<news> findBypopularity(Integer num){
         return newsService.findBypopularity(num);
     }

     @RequestMapping("/getlinkBynewsId")
     public String getlinkBynewsId(Integer newsId){
         return newsService.getlinkBynewsId(newsId);
     }

     @RequestMapping("/getnewsIdBylink")
     public Integer getnewsIdBylink(String link){
         return newsService.getnewsIdBylink(link);
     }

     @RequestMapping("/findBytype")
     public List<news> findbytype(String type){
         return newsService.findBytype(type);
     }

     @RequestMapping("/findBykeyword")
     public List<news> findbykeyword(String keyword){
         return newsService.findBykeyword(keyword);
     }

     @RequestMapping("/findByeventId")
     public List<news> findByeventId(String eventId){
         return newsService.findByeventId(eventId);
     }

     @RequestMapping("/getnewsBynewsId")
     public String getnewsBynewsId(Integer newsId){
         return newsService.getnewsBynewsId(newsId);
     }

     @RequestMapping("/getpopularityBynewsId")
     public Integer getpopularityBynewsId(Integer newsId){
         return newsService.getpopulairtyBynewsId(newsId);
     }

     @RequestMapping("/getpictureLinkBynewsId")
     public String getpicturelinkBynewsId(Integer newsId){
        return newsService.getpicturelinkBynewsId(newsId);
     }

     /*@PostMapping("/setnews")
     public boolean setnews(String news, String type, Integer popularity, String link, String picturelink, String source, Integer attitude, String time, String emotion, String keyword){
         return newsService.setnews(news,type,popularity,link,picturelink,source,attitude,time,emotion,keyword);
     }*/

     @PostMapping("/updatenews_popularity")
     public boolean updatenews_popularity(Integer popularity, Integer newsId){
         return newsService.updatenews_popularity(popularity,newsId);
     }

     @PostMapping("/updatenews_type")
     public boolean updatenews_type(String type, Integer newsId){
         return newsService.updatenews_type(type,newsId);
     };
}


