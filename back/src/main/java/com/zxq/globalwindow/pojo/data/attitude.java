package com.zxq.globalwindow.pojo.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class attitude implements Serializable {
    String attitude;
    Integer number;
}
