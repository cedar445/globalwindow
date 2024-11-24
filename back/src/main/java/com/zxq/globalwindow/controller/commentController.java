package com.zxq.globalwindow.controller;


import com.zxq.globalwindow.pojo.comment;
import com.zxq.globalwindow.pojo.data.eventComment;
import com.zxq.globalwindow.pojo.data.newsBySearch;
import com.zxq.globalwindow.pojo.news;
import com.zxq.globalwindow.service.commentService;
import com.zxq.globalwindow.service.newsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class commentController {

    @Autowired
    private commentService commentService;
    @Autowired
    private newsService newsService;

}
