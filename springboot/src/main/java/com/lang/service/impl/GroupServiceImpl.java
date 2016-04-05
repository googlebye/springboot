package com.lang.service.impl;

import java.util.Map;

import com.lang.dao.IGroupDao;
import com.lang.service.IGroupService;

public class GroupServiceImpl implements IGroupService {

    private IGroupDao groupDao;

    public void setGroupDao(IGroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public void insertIfAbsent(String uuid) {
        Map<String, Object> group = groupDao.findByUUID(uuid);
        if (group == null) {
            groupDao.insert(uuid);
        }
    }

    public Map<String, Object> findByUUID(String uuid) {
        return groupDao.findByUUID(uuid);
    }

}
