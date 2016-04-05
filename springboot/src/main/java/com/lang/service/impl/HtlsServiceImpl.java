package com.lang.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.lang.dao.IHtlsDao;
import com.lang.service.IHtlsService;

public class HtlsServiceImpl implements IHtlsService {

    private IHtlsDao htlsDao;

    public void setHtlsDao(IHtlsDao htlsDao) {
        this.htlsDao = htlsDao;
    }

    public void insertIfAbsent(String code, String descript, String grpcode) {
        Map<String, Object> htl = htlsDao.findByCode(code);
        if (htl == null) {
            htlsDao.insert(code, descript, grpcode);
        }
    }

    public void insertIfAbsent(List<Map<String, Object>> hotels) {
        for (Map<String, Object> map : hotels) {
            String code = MapUtils.getString(map, "code");
            String descript = MapUtils.getString(map, "descript");
            String grpcode = MapUtils.getString(map, "grpcode");

            insertIfAbsent(code, descript, grpcode);
        }
    }

}
