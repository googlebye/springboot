package com.lang.service;

import java.util.List;
import java.util.Map;

public interface IHtlsService {

    void insertIfAbsent(String code, String descript, String grpcode);

    void insertIfAbsent(List<Map<String, Object>> hotels);
}
