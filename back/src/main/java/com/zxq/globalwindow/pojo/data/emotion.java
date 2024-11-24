package com.zxq.globalwindow.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class emotion implements Serializable {
    String emotion;
    Integer number;
}