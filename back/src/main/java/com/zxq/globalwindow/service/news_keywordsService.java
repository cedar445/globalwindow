package com.zxq.globalwindow.service;

import com.zxq.globalwindow.pojo.keyword;

import java.util.List;

public interface news_keywordsService {

    public List<String> getkeywordBynewsId(Integer newsId);

    public List<Integer> getnewsIdBykeyword(String keyword);

    public List<Integer> getHotnewsIdBykeyword(String keyword, Integer num);

    public List<Integer> getRHotnewsIdBykeyword(String keyword, Integer num);

    public boolean setnews_keywords(Integer newsId, String keyword);
}
