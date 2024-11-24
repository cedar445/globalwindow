package com.zxq.globalwindow.mapper;

import com.zxq.globalwindow.pojo.timeLink;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface timeLinkMapper {
    public Set<timeLink> findAlltimeLink();

    public boolean setTimeLink(@Param("timeLink")String timeLink);
}
