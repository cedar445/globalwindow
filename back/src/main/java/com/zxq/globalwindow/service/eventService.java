package com.zxq.globalwindow.service;

import com.zxq.globalwindow.pojo.event;
import com.zxq.globalwindow.pojo.news;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface eventService {

    public boolean readData();

    public boolean findAllEvent();

    public boolean setAllEvent();

    public List<event> findByTimeLink(String timeLink);

    public Set<String> findBypopularity(Integer num);

    public event findByeventId(String eventId);

    public List<event> findRBykeyword(String keyword, Integer num);

    public List<event> findRBypopularity(Integer num, LocalDateTime time);

    public List<event> findHotBysource(String source, Integer num, LocalDateTime time);

    public String geteventByeventId(String eventId);

    public Integer getpopularityByeventId(String eventId);

    public Set<String> getKeywordsByeventId(String eventId);

    public boolean seteventTextBykeyword(String text,String eventId);

    public Set<String> findAllBykeyword(String keyword);
}
