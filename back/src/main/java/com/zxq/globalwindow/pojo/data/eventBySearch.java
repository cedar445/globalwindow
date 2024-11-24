package com.zxq.globalwindow.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class eventBySearch implements Serializable {
    String eventId;
    String event;
    Integer popularity;
}
