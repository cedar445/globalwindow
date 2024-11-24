package com.zxq.globalwindow.pojo;

public class message {

    private Integer messageId;
    private String message;
    private Integer likeNum;
    private String type;
    private Integer linkId;
    private String picturelink;

    public message(){}

    public message(Integer messageId, String message, String type, Integer likeNum, Integer linkId, String picturelink){
        this.messageId = messageId;
        this.message = message;
        this.type = type;
        this.likeNum = likeNum;
        this.linkId = linkId;
        this.picturelink = picturelink;
    }
}
