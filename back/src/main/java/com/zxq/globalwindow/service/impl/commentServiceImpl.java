package com.zxq.globalwindow.service.impl;

import com.zxq.globalwindow.pojo.comment;
import com.zxq.globalwindow.utils.timeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.zxq.globalwindow.mapper.commentMapper;
import com.zxq.globalwindow.service.commentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class commentServiceImpl implements commentService{

    @Autowired
    commentMapper commentMapper;

    @Cacheable(cacheNames = "comments")
    @Override
    public comment findBycommentId(Integer commentId){
        return commentMapper.findBycommentId(commentId);
    }

    @Override
    public String getattitudeBycommentId(Integer commentId){
         comment comment = findBycommentId(commentId);
         String attitude = "";
         switch(comment.getAttitude()){
             case -2:
                 attitude =  "负面";
             case -1:
                 attitude = "比较负面";
             case 0:
                 attitude = "中立";
             case 1:
                 attitude = "比较正面";
             case 2:
                 attitude = "正面";
         }
         return attitude;
    }

    @Override
    public String gettimeBycommentId(Integer commentId){
        comment comment = findBycommentId(commentId);
        return timeFormatter.timeFormatter(comment.getTime());
    }

    @Override
    public String getsourceBycommentId(Integer commentId){
        comment comment = findBycommentId(commentId);
        return comment.getSource();
    }

    @Override
    public String getemotionBycommentId(Integer commentId){
        comment comment = findBycommentId(commentId);
        return comment.getEmotion();
    }

    @Override
    public String getcommentBycommentId(Integer commentId){
        comment comment = findBycommentId(commentId);
        return comment.getComment();
    }

    @Override
    public List<comment> findBynewsId(Integer newsId){
        return commentMapper.findBynewsId(newsId);
    }

    @Override
    public List<comment>  findHotBynewsId(Integer newsId,Integer num){
        return commentMapper.findHotBynewsId(newsId,num);
    }

    @Override
    public boolean setcomment(String comment, Integer attitude, LocalDateTime time, String source, Integer newsId, String emotion, Integer likeNum){
        return commentMapper.setcomment(comment,attitude,time,source,newsId,emotion,likeNum);
    }
}
