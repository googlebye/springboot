package com.lang.service;

import java.util.List;
import java.util.Map;


public interface IGrpsService {

    void insertIfAbsent(String code, String descript);
    
    void insertIfAbsent(List<Map<String,Object>> list);
}
