package com.zxq.globalwindow.service.impl;

import com.zxq.globalwindow.mapper.keywordMapper;
import com.zxq.globalwindow.pojo.keyword;
import com.zxq.globalwindow.mapper.news_keywordsMapper;
import com.zxq.globalwindow.service.news_keywordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class news_keywordsServiceImpl implements news_keywordsService {

    @Autowired
    news_keywordsMapper news_keywordsMapper;
    @Autowired
    keywordMapper keywordMapper;

    @Override
    public List<String> getkeywordBynewsId(Integer newsId){
        return news_keywordsMapper.getkeywordBynewsId(newsId);
    }

    @Override
    public List<Integer> getnewsIdBykeyword(String keyword){
        return news_keywordsMapper.getnewsIdBykeyword(keyword);
    }

    @Override
    public List<Integer> getHotnewsIdBykeyword(String keyword, Integer num){
        return news_keywordsMapper.getHotnewsIdBykeyword(keyword,num);
    }

    @Override
    public List<Integer> getRHotnewsIdBykeyword(String keyword, Integer num){
        return news_keywordsMapper.getRHotnewsIdBykeyword(keyword,num);
    }

    @Override
    public boolean setnews_keywords(Integer newsId, String keyword){
        return news_keywordsMapper.setnews_keywords(newsId,keyword);
    }
}
