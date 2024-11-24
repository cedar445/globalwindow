package com.zxq.globalwindow.pojo;

public class link {
    private Integer linkId;
    private String link;
    private String source;
    private Integer attitude;
    private String time;
    private Integer keywordId;
    private String emotion;

    public link(){}

    public link(Integer linkId,String link,String source,Integer attitude,String time,Integer keywordId,String emotion){
        this.linkId = linkId;
        this.link = link;
        this.source = source;
        this.attitude = attitude;
        this.time = time;
        this.keywordId = keywordId;
        this.emotion = emotion;
    }
}
