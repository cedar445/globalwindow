package com.zxq.globalwindow.mapper;

import com.zxq.globalwindow.pojo.news;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface newsMapper {

    public news findBynewsId(@Param("newsId")Integer newsId);

    public List<news> findBypopularity(@Param("num")Integer num);

    /*@Select("select link from news where newsId = #{newsId}")
    public String getlinkBynewsId(Integer newsId);*/

    public List<news> findHotBytype(@Param("type")String type, @Param("num")Integer num);

    public List<news> findBytype(@Param("type")String type);

    public List<news> findBysource(@Param("source")String source);

    public List<news> findHotBysource(@Param("source")String source, @Param("num")Integer num);

    public List<news> findByeventId(@Param("eventId")String eventId);

    public List<news> findHotByeventId(@Param("eventId")String eventId, @Param("num")Integer num);

    public Integer getnewsIdBylink(@Param("link")String link);

    public List<news> getNewsByX(@Param("limitNum")Integer limitNum);

    public List<String> getNewsImages(@Param("num")Integer num);

    /*@Select("select news from news where newsId = #{newsId}")
    public String getnewsBynewsId(Integer newsId);*/

    /*@Select("select popularity from news where newsId = #{newsId}")
    public int getpopulairtyBynewsId(Integer newsId);*/

    /*@Select("select picturelink from news where newsId = #{newsId}")
    public String getpicturelinkBynewsId(Integer newsId);*/

    /*@Insert("insert into news(news,type,popularity,link,picturelink,source,attitude,time,emotion,eventId) values (#{news},#{type},#{popularity},#{link},#{picturelink},#{source},#{attitude},#{time},#{emotion},#{eventId})")
    public boolean setnews(String news, String type, Integer popularity, String link, String picturelink, String source, Integer attitude, String time, String emotion, String eventId);*/

    public boolean setnewsEventId(@Param("eventId")String eventId,@Param("newsId")Integer newsId);

    public boolean updatenews_popularity(@Param("popularity")Integer popularity, @Param("newsId")Integer newsId);

    public boolean updatenews_type(@Param("type")String type, @Param("newsId")Integer newsId);
}
