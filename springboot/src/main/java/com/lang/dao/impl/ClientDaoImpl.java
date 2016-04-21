package com.lang.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lang.dao.IClientDao;
import com.lang.utils.SQLHelper;

@Repository
public class ClientDaoImpl implements IClientDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> listClients(String type, String mac) {
        SQLHelper sh = new SQLHelper();
        sh.appendSql(" SELECT * FROM client where (1=1) ");
        if (type != null && type.trim().length() > 0) {
            sh.appendSql(" and type = ? ");
            sh.insertValue(type.trim());
        }
        if (mac != null && mac.trim().length() > 0) {
            sh.appendSql(" and mac = ? ");
            sh.insertValue(mac.trim());
        }
        return jdbcTemplate.queryForList(sh.getSQL(), sh.getValues());
    }

    public List<Map<String, Object>> listClients() {
        return listClients(null, null);
    }

    public void register(String type, String mac, String grps, String htls, String uuid, String version) {
        SQLHelper sh = new SQLHelper("INSERT INTO client (type, mac, grps, htls, uuid, version)VALUES(?,?,?,?,?,?)");
        sh.insertValue(type);
        sh.insertValue(mac);
        sh.insertValue(grps);
        sh.insertValue(htls);
        sh.insertValue(uuid);
        sh.insertValue(version);

        jdbcTemplate.update(sh.getSQL(), sh.getValues());
    }

    public void updateRegistedVersion(String type, String mac, String currentVersion) {
        SQLHelper sh = new SQLHelper("update client set version = ? where type = ? and mac = ?");
        sh.insertValue(currentVersion);
        sh.insertValue(type);
        sh.insertValue(mac);

        jdbcTemplate.update(sh.getSQL(), sh.getValues());
    }
}
