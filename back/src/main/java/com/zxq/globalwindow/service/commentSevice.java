package com.zxq.globalwindow.service;

import com.zxq.globalwindow.pojo.comment;


import java.util.List;

public interface commentSevice {
    public comment findBycommentId(Integer commentId);

    public String getattitudeBycommentId(Integer commentId);

    public String gettimeBycommentId(Integer commentId);

    public String getsourceBycommentId(Integer commentId);

    public String getemotionBycommentId(Integer commentId);

    public String getcommentBycommentId(Integer commentId);

    public List<comment> findBynewsId(Integer newsId);

    public boolean setcomment(String comment, Integer attitude, String time, String source, Integer newsId, String emotion);
}
