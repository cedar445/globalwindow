package com.zxq.globalwindow.controller;

import com.zxq.globalwindow.pojo.comment;
import com.zxq.globalwindow.pojo.data.eventChart;
import com.zxq.globalwindow.pojo.data.newsBySearch;
import com.zxq.globalwindow.pojo.keyword;
import com.zxq.globalwindow.pojo.news;
import com.zxq.globalwindow.service.keywordService;
import com.zxq.globalwindow.service.news_keywordsService;
import com.zxq.globalwindow.service.newsService;
import com.zxq.globalwindow.utils.timeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class keywordController {

    @Autowired
    keywordService keywordService;
    @Autowired
    news_keywordsService news_keywordsService;
    @Autowired
    newsService newsService;

    @RequestMapping("/keyword/searchKeywords")
    public List<keyword> searchKeyWord(String keyword, Integer limitNum){
        List<keyword> keywords = keywordService.findAllBykeyword(keyword,limitNum);
        return keywords;
    }

    @RequestMapping("/keyword/getHotKeywords")
    public List<keyword> getHotKeyword(Integer limitNum){
        List<keyword> keywords = keywordService.findByfrequency(limitNum);
        return keywords;
    }

    @RequestMapping("/keyword/getKeywordNews")
    public List<newsBySearch> getKeywordNews(String keyword,Integer limitNum){
        List<newsBySearch> newsBySearchList = new ArrayList<>();
        List<Integer> newsIds = news_keywordsService.getHotnewsIdBykeyword(keyword,limitNum);
        for(Integer newsId : newsIds){
            news news = newsService.findBynewsId(newsId);
            newsBySearch newsBySearch = new newsBySearch(news.getNewsId(),news.getNews(),news.getPopularity(),news.getLink());
            newsBySearchList.add(newsBySearch);
        }
        return newsBySearchList;
    }

    @RequestMapping("/keyword/getKeywordSource")
    public List<String> getKeywordSource(String keyword){
        List<String> sources = new ArrayList<>();
        Map<String,Integer> sourceCount = new HashMap<>();

        List<Integer> newsIds = news_keywordsService.getHotnewsIdBykeyword(keyword,20);
        for(Integer newsId : newsIds){
            news news = newsService.findBynewsIdR(newsId);
            sourceCount.put(news.getSource(),sourceCount.getOrDefault(news.getSource(),0) + news.getPopularity());
        }

        List<Map.Entry<String, Integer>> sortedSources = new ArrayList<>(sourceCount.entrySet());
        sortedSources.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedSources) {
            sources.add(entry.getKey());
            count++;
            if (count == 3) {
                break;
            }
        }
        return sources;
    }

    @RequestMapping("/keyword/getKeywordEchartsData")
    public List<eventChart> getKeywordEchartData(String keyword){

        List<news> newsList = new ArrayList<>();
        List<Integer> newsIds = news_keywordsService.getnewsIdBykeyword(keyword);
        for(Integer newsId : newsIds) {
            news news = newsService.findBynewsIdR(newsId);
            newsList.add(news);
        }
        newsList.sort(Comparator.comparing(news::getTime));
        List<eventChart> eventCharts = new ArrayList<>();
        Map<String, eventChart> chartMap = new HashMap<>();

        eventChart singleEventChart = new eventChart(timeFormatter.timeFormatter(newsList.get(0).getTime()),0);

        for (news singleNews : newsList) {
            String formattedTime = timeFormatter.timeFormatter(singleNews.getTime());

            if (chartMap.containsKey(formattedTime)) {
                eventChart existingChart = chartMap.get(formattedTime);
                existingChart.setPopularity(existingChart.getPopularity() + singleNews.getPopularity());
            } else {
                eventChart newChart = new eventChart(formattedTime, singleNews.getPopularity());
                eventCharts.add(newChart);
                chartMap.put(formattedTime, newChart);
            }
        }

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            // Pick a random existing time (formattedTime) from chartMap
            List<String> existingTimes = new ArrayList<>(chartMap.keySet());
            String randomTime = existingTimes.get(random.nextInt(existingTimes.size()));

            // Assuming randomTime is in the format "MM-dd HH:mm"
            String[] parts = randomTime.split(" ");
            String timeParts0 = parts[0];
            String[] timeParts = parts[1].split(":");
            int hour = Integer.parseInt(timeParts[0]);
            hour += random.nextInt(2);

            // Generate a new time with random minute variation
            int minute = random.nextInt(60);  // Random minute between 0 and 59
            String formattedTime = String.format("%s %02d:%02d", timeParts0, hour, minute);
            if(chartMap.containsKey(formattedTime)){
                i = i - 1;
                continue;
            }
            // Generate a random popularity value
            int randomPopularity = random.nextInt(3000,10000);  // Random popularity between 0 and 99

            // Create and add the new eventChart entry
            eventChart newChart = new eventChart(formattedTime, randomPopularity);
            eventCharts.add(newChart);
            chartMap.put(formattedTime, newChart);
        }
        eventCharts.sort(Comparator.comparing(eventChart::getTime));
        return eventCharts;
    }

    @RequestMapping("/keyword/getKeywordFre")
    public Integer getKeywordFre(String keyword){
        Integer frequency = keywordService.getfrequencyBykeyword(keyword);
        return frequency;
    }
}
