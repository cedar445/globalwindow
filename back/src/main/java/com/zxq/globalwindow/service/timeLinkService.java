package com.zxq.globalwindow.service;

import com.zxq.globalwindow.pojo.timeLink;

import java.util.List;
import java.util.Set;

public interface timeLinkService {

    public Set<timeLink> findAlltimeLink();

    public boolean setTimeLink(String timeLink);
}
