package com.zxq.globalwindow.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class event implements Serializable{
    private String eventId;
    private String keyword;
    private String event;
    private Integer popularity;
    private String source;
    private String timeLink;
    private String link;
    private LocalDateTime time;
}

