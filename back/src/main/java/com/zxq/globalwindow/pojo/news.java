package com.zxq.globalwindow.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class news implements Serializable {

    private Integer newsId;
    private String news;
    private String type;
    private Integer popularity;
    private String link;
    private String picturelink;//设置默认图片的链接
    private String source;
    private Integer attitude;
    private LocalDateTime time;
    private String emotion;
    private String eventId;
}
