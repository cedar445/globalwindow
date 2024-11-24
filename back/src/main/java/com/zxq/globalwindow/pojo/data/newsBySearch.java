package com.zxq.globalwindow.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class newsBySearch implements Serializable {
    Integer newsId;
    String news;
    Integer popularity;
    String link;
}
