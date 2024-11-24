package com.zxq.globalwindow.exception;

import org.springframework.util.StringUtils;
import com.zxq.globalwindow.pojo.ResponseVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseVO handleException(Exception e){
        e.printStackTrace();
        return  ResponseVO.error(StringUtils.hasLength(e.getMessage())? e.getMessage() : "操作失败");
    }
}
