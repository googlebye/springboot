package com.lang.dao;

import java.util.Map;


public interface IHtlsDao {

    Map<String, Object> findByCode(String code);

    void insert(String code, String descript, String grpcode);

}
