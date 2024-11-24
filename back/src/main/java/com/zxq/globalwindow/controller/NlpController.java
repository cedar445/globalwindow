package com.zxq.globalwindow.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zxq.globalwindow.service.NlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@RestController
public class NlpController {

    @Autowired
    private NlpService nlpService;
    private final Gson gson;  // 解析JSON

    public NlpController(Gson gson) {
        this.gson = gson;
    }

    @GetMapping("/pos-tag")
    public String posTag(@RequestParam String text) {
        try {
            return nlpService.getPosChEcom(text);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    //Example: http://localhost:8080/sentiment-analysis?text=我很开心
    @GetMapping("/sentiment-analysis")
    public String sentimentAnalysis(@RequestParam String text) {
        try {
            return nlpService.getSaChGeneral(text);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    //Example: http://localhost:8080/keyword-extraction?text=我很开心
    @GetMapping("/keyword-extraction")
    public ResponseEntity<?> callNlpService(@RequestParam String text) {
        try {
            Map<String, Object> response = nlpService.getKeyWord(text);
                Gson gson = new Gson();
                // 从response中提取body，需要处理可能的字符串形式的JSON
                Object bodyObject = response.get("body");
                if (bodyObject == null) {
                    throw new Exception("Response body is missing");
                }

                    Map<String, Object> body;
                    if (bodyObject instanceof String) {
                        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                        body = gson.fromJson((String) bodyObject, mapType);
                    } else {
                        body = (Map<String, Object>) bodyObject;
                    }
                    // 提取并处理PredictResult
                    Object predictResultObject = body.get("PredictResult");
                    Map<String, Object> predictResult;
                    if (predictResultObject instanceof String) {
                        Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                        predictResult = gson.fromJson((String) predictResultObject, mapType);
                    } else {
                        predictResult = (Map<String, Object>) predictResultObject;
                    }

                    // 从PredictResult中提取关键词列表
                    List<String> keywordsOrSummaries = (List<String>) predictResult.get("keywords_or_summaries");
                    if (keywordsOrSummaries == null) {
                        throw new Exception("Keywords or summaries are missing in the PredictResult");
                    }

                     // 转化成String
                    String result = String.join(", ", keywordsOrSummaries);
                    System.out.println(result);

                    // 返回成功的响应体，包含关键词列表
                    return ResponseEntity.ok(result);
                } catch (Exception e) {

                    // 返回错误的响应体，包含错误信息
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }


        }
