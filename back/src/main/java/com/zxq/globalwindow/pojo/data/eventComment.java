package com.zxq.globalwindow.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class eventComment implements Serializable {
    Integer commentId;
    String comment;
    String source;
}
