package com.zxq.globalwindow.service;

import com.zxq.globalwindow.pojo.news;

import java.util.List;

public interface newsService {

    public List<news> getNewsByX(Integer limitNum);

    public news findBynewsId(Integer newsId);

    public Integer readData(Integer startIndex, Integer batchSize);

    public List<news> findBypopularity(Integer num);

    public String getlinkBynewsId(Integer newsId);

    public String geteventIdBynewsId(Integer newsId);

    public List<news> findBytype(String type);

    public List<news> findHotBytype(String type, Integer num);

    public List<news> findBysource(String source);

    public List<news> findHotBysource(String source, Integer num);

    public List<news> findBykeyword(String singlekeyword);

    public List<String> getNewsImages(Integer num);

    public List<news> findHotBykeyword(String singlekeyword, Integer num);

    public List<news> findByeventId(String eventId);

    public List<news> findHotByeventId(String eventId, Integer num);

    public String getnewsBynewsId(Integer newsId);

    public news findBynewsIdR(Integer newsId);

    public Integer getpopulairtyBynewsId(Integer newsId);

    public String getpicturelinkBynewsId(Integer newsId);

    public Integer getnewsIdBylink(String link);

    public String getsourceBynewsId(Integer newsId);

    public String getattitudeBynewsId(Integer newsId);

    public String gettimeBynewsId(Integer newsId);

    public String getemotionBynewsId(Integer newsId);

    //public boolean setnews(String news, String type, Integer popularity, String link, String picturelink, String source, Integer attitude, String time, String emotion, String keyword);

    public boolean setnewsEventId(String eventId,Integer newsId);

    public boolean updatenews_popularity(Integer popularity, Integer newsId);

    public boolean updatenews_type(String type, Integer newsId);

}
