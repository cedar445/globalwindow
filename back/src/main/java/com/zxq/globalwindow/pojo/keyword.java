package com.zxq.globalwindow.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class keyword implements Serializable {
    private String keyword;
    private Integer frequency;

    public keyword(){}

    public keyword(String keyword, Integer frequency){
        this.keyword = keyword;
        this.frequency = frequency;
    }
}
