package com.zxq.globalwindow.service;

import com.zxq.globalwindow.pojo.keyword;

import java.util.List;

public interface keywordService {

    public keyword findBykeyword(String keyword);

    public Integer getfrequencyBykeyword(String keyword);

    public List<keyword> findByfrequency(Integer num);

    public List<keyword> findAllBykeyword(String keyword, Integer num);

    public boolean setkeyword(String key);

    public List<keyword> findAllkeyword();

    public boolean updatekeyword_frequency(String keyword);
}
