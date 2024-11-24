package com.zxq.globalwindow.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class timeFormatter {
    public static String timeFormatter(LocalDateTime time){
        // 创建一个自定义的日期时间格式化器，仅包含月、日和小时部分
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");

        // 格式化日期时间
        String formattedDateTime = time.format(formatter);

        return formattedDateTime;
    }
}
