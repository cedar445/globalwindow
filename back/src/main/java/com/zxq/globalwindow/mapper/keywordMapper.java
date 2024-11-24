package com.zxq.globalwindow.mapper;

import com.zxq.globalwindow.pojo.keyword;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface keywordMapper {


    public keyword findBykeyword(@Param("keyword")String keyword);


    public List<keyword> findAllBykeyword(@Param("keyword")String keyword, @Param("num")Integer num);

    /*@Select("select frequency from keyword where keyword = #{keyword}")
    public keyword getkeywordBykeyword(String keyword);*/

    public List<keyword> findByfrequency(@Param("num")Integer num);

    public boolean setkeyword(@Param("keyword")String keyword);

    public List<keyword> findAllkeyword();

    public boolean updatekeyword_frequency(@Param("frequency")Integer frequency, @Param("keyword")String keyword);
}
