package com.zxq.globalwindow.service.impl;

import com.zxq.globalwindow.service.QwenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class QwenServiceImpl implements QwenService {
    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public QwenServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callQwen(String userInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String jsonInputString = String.format("{\"model\": \"qwen-plus\", \"input\": {\"messages\": [{\"role\": \"system\", \"content\": \"你的任务是对新闻或者事件生成一个文本摘要。最多30个词汇。\"}, {\"role\": \"user\", \"content\": \"%s\"}]}, \"parameters\": {\"result_format\": \"message\"}}", userInput);
        HttpEntity<String> request = new HttpEntity<>(jsonInputString, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        return response.getBody();
    }

    public String girlCallQwen(String userInput) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String jsonInputString = String.format("{\"model\": \"qwen1.5-110b-chat\", \"input\": {\"messages\": [{\"role\": \"system\", \"content\": \"请按照如下格式给出关键词的解释：{关键词：简要的解释}。保持在30字以内\"}, {\"role\": \"user\", \"content\": \"%s\"}]}, \"parameters\": {\"result_format\": \"message\"}}", userInput);

        HttpEntity<String> request = new HttpEntity<>(jsonInputString, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
        return response.getBody();
    }
}

