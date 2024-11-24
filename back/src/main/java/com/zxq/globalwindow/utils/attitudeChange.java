package com.zxq.globalwindow.utils;

public class attitudeChange {
    public static String getAttitude(Integer attitudeI){
        String attitude = "";
        switch(attitudeI){
            case -2:
                attitude =  "负面";
                break;
            case -1:
                attitude = "比较负面";
                break;
            case 0:
                attitude = "中立";
                break;
            case 1:
                attitude = "比较正面";
                break;
            case 2:
                attitude = "正面";
                break;
        }
        return attitude;
    }
}
