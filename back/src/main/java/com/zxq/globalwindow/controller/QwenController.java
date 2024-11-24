package com.zxq.globalwindow.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zxq.globalwindow.service.QwenService;
import org.springframework.web.bind.annotation.*;

@RestController
public class QwenController {

    private final QwenService qwenService;

    public QwenController(QwenService qwenService) {
        this.qwenService = qwenService;
    }

    //example: http://localhost:8080/generate-text?userInput=我很开心
    @PostMapping("/generate-text")
    public String generateText(@RequestParam String userInput) {
        String jsonStr = qwenService.callQwen(userInput);
        Gson gson = new Gson();
        JsonObject jsonObj = JsonParser.parseString(jsonStr).getAsJsonObject();
        // 假设我们知道 JSON 结构并直接访问路径
        String content = jsonObj.getAsJsonObject("output")
                .getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
        return content;
    }

    @PostMapping("/girl-keyword-explain")
    public String girlKeywordExplain(@RequestParam String userInput) {
        String jsonStr = qwenService.girlCallQwen(userInput);
        Gson gson = new Gson();
        JsonObject jsonObj = JsonParser.parseString(jsonStr).getAsJsonObject();
        String content = jsonObj.getAsJsonObject("output")
                .getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
        return content;
    }
}
