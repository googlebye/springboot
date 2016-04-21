package com.lang.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lang.dao.IGroupDao;
import com.lang.service.IGroupService;

@Service
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private IGroupDao groupDao;

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
