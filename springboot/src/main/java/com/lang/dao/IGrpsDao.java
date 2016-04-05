package com.lang.dao;

import java.util.Map;


public interface IGrpsDao {

    Map<String, Object> findByCode(String code);

    void insert(String code, String descript);

}
