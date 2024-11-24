package com.zxq.globalwindow.service.impl;

import com.zxq.globalwindow.pojo.timeLink;
import com.zxq.globalwindow.mapper.timeLinkMapper;
import com.zxq.globalwindow.service.timeLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class timeLinkServiceImpl implements timeLinkService{

    @Autowired
    timeLinkMapper timeLinkMapper;

    @Override
    public Set<timeLink> findAlltimeLink(){
        return timeLinkMapper.findAlltimeLink();
    }

    @Override
    public boolean setTimeLink(String timeLink){
        return timeLinkMapper.setTimeLink(timeLink);
    }
}
