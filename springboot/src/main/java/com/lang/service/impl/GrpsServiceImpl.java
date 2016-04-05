package com.lang.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.lang.dao.IGrpsDao;
import com.lang.service.IGrpsService;

public class GrpsServiceImpl implements IGrpsService {

    private IGrpsDao grpsDao;

    public void setGrpsDao(IGrpsDao grpsDao) {
        this.grpsDao = grpsDao;
    }

    public void insertIfAbsent(String code, String descript) {
        Map<String, Object> htl = grpsDao.findByCode(code);
        if (htl == null) {
            grpsDao.insert(code, descript);
        }
    }

    public void insertIfAbsent(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            String code = MapUtils.getString(map, "code");
            String descript = MapUtils.getString(map, "descript");

            insertIfAbsent(code, descript);
        }
    }
}
