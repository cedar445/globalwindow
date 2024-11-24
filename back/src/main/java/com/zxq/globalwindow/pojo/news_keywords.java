package com.zxq.globalwindow.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class news_keywords implements Serializable {
    private Integer newsId;
    private String keyword;

    public news_keywords(){}

    public news_keywords(Integer newsId, String keyword){
        this.newsId = newsId;
        this.keyword = keyword;
    }
}
