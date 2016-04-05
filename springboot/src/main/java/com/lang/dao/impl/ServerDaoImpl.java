package com.lang.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lang.dao.IServerDao;
import com.lang.utils.SQLHelper;

public class ServerDaoImpl implements IServerDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> listRegistred(String type, String mac) {
        SQLHelper sh = new SQLHelper();
        sh.appendSql(" SELECT * FROM register where (1=1) ");
        if (type != null && type.trim().length() > 0) {
            sh.appendSql(" and type = ? ");
            sh.insertValue(type.trim());
        }
        if (mac != null && mac.trim().length() > 0) {
            sh.appendSql(" and mac = ? ");
            sh.insertValue(mac.trim());
        }
        return jdbcTemplate.queryForList(sh.getSQL(),sh.getValues());
    }

    public List<Map<String, Object>> listRegistred() {
        return listRegistred(null, null);
    }

    public void register(String type, String mac, String grps, String htls, String uuid) {
        SQLHelper sh = new SQLHelper("INSERT INTO register (type, mac, grps, htls, uuid)VALUES(?,?,?,?,?)");
        sh.insertValue(type);
        sh.insertValue(mac);
        sh.insertValue(grps);
        sh.insertValue(htls);
        sh.insertValue(uuid);

        jdbcTemplate.update(sh.getSQL(), sh.getValues());
    }

    public void updateRegistedVersion(String type, String mac, String currentVersion) {
        SQLHelper sh = new SQLHelper("update register set version = ? where type = ? and mac = ?");
        sh.insertValue(currentVersion);
        sh.insertValue(type);
        sh.insertValue(mac);

        jdbcTemplate.update(sh.getSQL(), sh.getValues());
    }
}
