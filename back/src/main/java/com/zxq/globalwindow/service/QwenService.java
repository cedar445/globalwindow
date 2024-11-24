package com.zxq.globalwindow.service;

import org.springframework.stereotype.Service;

@Service
public interface QwenService {
    public String callQwen(String userInput);
    public String girlCallQwen(String userInput);
}
