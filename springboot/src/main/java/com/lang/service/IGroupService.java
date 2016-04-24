package com.lang.service;

import com.lang.entity.ClientGroup;

public interface IGroupService {

    ClientGroup insertIfAbsent(String uuid);

    ClientGroup findByUUID(String uuid);
}
