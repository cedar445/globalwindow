package com.zxq.globalwindow.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO<T> implements Serializable {

    private Integer code;
    private String info;
    private T data;


    public static ResponseVO error(String info){return new ResponseVO(1,info,null);}
}

