package com.zxq.globalwindow.service;

import com.zxq.globalwindow.pojo.comment;


import java.time.LocalDateTime;
import java.util.List;

public interface commentService {
    public comment findBycommentId(Integer commentId);

    public String getattitudeBycommentId(Integer commentId);

    public String gettimeBycommentId(Integer commentId);

    public String getsourceBycommentId(Integer commentId);

    public String getemotionBycommentId(Integer commentId);

    public String getcommentBycommentId(Integer commentId);

    public List<comment> findBynewsId(Integer newsId);

    public List<comment> findHotBynewsId(Integer newsId,Integer num);

    public boolean setcomment(String comment, Integer attitude, LocalDateTime time, String source, Integer newsId, String emotion, Integer likeNum);
}
