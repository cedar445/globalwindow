package com.zxq.globalwindow.service.impl;

import com.zxq.globalwindow.pojo.keyword;
import com.zxq.globalwindow.pojo.news;
import com.zxq.globalwindow.service.keywordService;
import com.zxq.globalwindow.service.news_keywordsService;
import com.zxq.globalwindow.mapper.keywordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class keywordServiceImpl implements keywordService{

    @Autowired
    private keywordMapper keywordMapper;
    @Autowired
    private news_keywordsService news_keywordsService;
    @Autowired
    private com.zxq.globalwindow.mapper.news_keywordsMapper news_keywordsMapper;

    @Cacheable(cacheNames = "keywords")
    @Override
    public keyword findBykeyword(String keyword){
        return keywordMapper.findBykeyword(keyword);
    }

    @Override
    public Integer getfrequencyBykeyword(String keyword){
        keyword newkeyword = keywordMapper.findBykeyword(keyword);
        return newkeyword.getFrequency();
    }

    @Override
    public List<keyword> findByfrequency(Integer num){
        return keywordMapper.findByfrequency(num);
    }

    @Override
    public List<keyword> findAllBykeyword(String keyword, Integer num){
        return keywordMapper.findAllBykeyword(keyword,num);
    }

    @Override
    public boolean setkeyword(String keyword){
        return keywordMapper.setkeyword(keyword);
    }

    @Override
    public List<keyword> findAllkeyword(){
        return keywordMapper.findAllkeyword();
    }

    @Override
    public boolean updatekeyword_frequency(String keyword){
        List<Integer> getfrequency = news_keywordsService.getnewsIdBykeyword(keyword);
        Integer number = getfrequency.size();
        keywordMapper.updatekeyword_frequency(number,keyword);
        return true;
    }
}
