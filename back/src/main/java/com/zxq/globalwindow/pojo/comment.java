package com.zxq.globalwindow.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class comment implements Serializable {

    private Integer commentId;
    private String comment;
    private Integer attitude;
    private LocalDateTime time;
    private String source;
    private Integer newsId;
    private String emotion;
    private Integer likeNum;
}
