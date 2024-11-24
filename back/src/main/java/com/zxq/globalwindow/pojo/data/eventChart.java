package com.zxq.globalwindow.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class eventChart implements Serializable {
    String time;
    Integer popularity;
}
