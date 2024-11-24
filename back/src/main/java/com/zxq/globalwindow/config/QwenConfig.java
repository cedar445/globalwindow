package com.zxq.globalwindow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class QwenConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}