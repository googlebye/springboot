package com.lang.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lang.dao.IGrpsDao;
import com.lang.dao.IHtlsDao;
import com.lang.entity.Grp;
import com.lang.entity.Htl;
import com.lang.service.IHtlsService;

@Service
public class HtlsServiceImpl implements IHtlsService {

    @Autowired
    private IHtlsDao htlsDao;
    @Autowired
    private IGrpsDao grpsDao;

    public void insertIfAbsent(String code, String descript, String grpcode) {
        Htl htl = htlsDao.findByCode(code);
        if (htl == null) {
            htl = new Htl();
            htl.code = code;
            htl.descript = descript;

            Grp grp = grpsDao.findByCode(grpcode);

            htl.grp = grp;

            htlsDao.save(htl);
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
