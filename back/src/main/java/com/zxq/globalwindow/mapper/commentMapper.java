package com.zxq.globalwindow.mapper;

import com.zxq.globalwindow.pojo.comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
@Mapper
public interface commentMapper {
    public comment findBycommentId(Integer commentId);

    public List<comment>  findBynewsId(Integer newsId);

    public List<comment>  findHotBynewsId(Integer newsId,Integer num);

    public boolean setcomment(String comment, Integer attitude, LocalDateTime time, String source, Integer newsId, String emotion, Integer likeNum);
}
