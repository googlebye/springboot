package com.lang.service;

import java.util.Map;

public interface IGroupService {

    void insertIfAbsent(String uuid);

    Map<String, Object> findByUUID(String uuid);
}
