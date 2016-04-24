package com.lang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lang.dao.IGroupDao;
import com.lang.entity.ClientGroup;
import com.lang.service.IGroupService;

@Service
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private IGroupDao groupDao;

    public ClientGroup insertIfAbsent(String uuid) {
        ClientGroup group = uuid != null ? groupDao.findOne(uuid) : null;
        if (group == null) {
            group = new ClientGroup();
            group.uuid = uuid;
            groupDao.save(group);
        }
        
        return group;
    }

    public ClientGroup findByUUID(String uuid) {
        return groupDao.findOne(uuid);
    }

}
