package com.zxq.globalwindow.service;

import java.util.Map;

public interface NlpService {

    public String getPosChEcom(String text) throws Exception; //中文分词接口
    public String getSaChGeneral(String text) throws Exception; //情感分析接口
    public Map<String, Object> getKeyWord(String text) throws Exception; //关键词提取接口
}
