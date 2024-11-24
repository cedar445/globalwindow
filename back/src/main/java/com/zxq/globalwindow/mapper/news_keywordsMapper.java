package com.zxq.globalwindow.mapper;

import com.zxq.globalwindow.pojo.keyword;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface news_keywordsMapper {

    public List<String> getkeywordBynewsId(@Param("newsId")Integer newsId);

    public List<Integer> getnewsIdBykeyword(@Param("keyword")String keyword);

    public List<Integer> getHotnewsIdBykeyword(@Param("keyword")String keyword, @Param("num")Integer num);

    public List<Integer> getRHotnewsIdBykeyword(@Param("keyword")String keyword, @Param("num")Integer num);

    public boolean setnews_keywords(@Param("newsId")Integer newsId, @Param("keyword")String keyword);
}
