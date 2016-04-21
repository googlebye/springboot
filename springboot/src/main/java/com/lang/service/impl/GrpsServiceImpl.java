package com.lang.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lang.dao.IGrpsDao;
import com.lang.service.IGrpsService;
@Service
public class GrpsServiceImpl implements IGrpsService {

    @Autowired
    private IGrpsDao grpsDao;

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
