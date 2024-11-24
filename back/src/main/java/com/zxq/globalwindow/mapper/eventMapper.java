package com.zxq.globalwindow.mapper;

import com.zxq.globalwindow.pojo.event;
import com.zxq.globalwindow.pojo.news;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface eventMapper {


    public List<event> findAllEvent();


    public event findByeventId(@Param("eventId")String eventId);


    public List<event> findHotBysource(@Param("source")String source,
                                       @Param("num")Integer num,
                                       @Param("time")LocalDateTime time);

    public List<event> findRBykeyword(@Param("keyword")String keyword,@Param("num")Integer num);


    public  List<event> findByTimeLink(@Param("timeLink")String timeLink);


    public String getkeywordsByeventId(@Param("eventId")String eventId);

    public List<event> findRBypopularity(@Param("num") Integer num,@Param("time")LocalDateTime time);


    public boolean setTimeLink(@Param("timeLink")String timeLink, @Param("eventId")String eventId);

    public boolean setevent(@Param("eventId")String eventId, @Param("keyword")String keyword,
                            @Param("event")String event, @Param("popularity")Integer popularity);
}
