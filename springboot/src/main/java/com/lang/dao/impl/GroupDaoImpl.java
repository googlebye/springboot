package com.lang.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lang.dao.IGroupDao;
import com.lang.utils.SQLHelper;

public class GroupDaoImpl implements IGroupDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> findByUUID(String uuid) {
        List<Map<String, Object>> list = list(uuid);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<Map<String, Object>> listAll() {
        return list(null);
    }

    private List<Map<String, Object>> list(String uuid) {
        SQLHelper sh = new SQLHelper("select * from `group` ");

        if (uuid != null && uuid.trim().length() > 0) {
            sh.appendSql(" where uuid = ? ");
            sh.insertValue(uuid.trim());
        }

        return jdbcTemplate.queryForList(sh.getSQL(), sh.getValues());
    }

    public void insert(String uuid) {
        SQLHelper sh = new SQLHelper("insert into `group`(uuid,is_ready) values (?,?)");
        sh.insertValue(uuid);
        sh.insertValue("F");

        jdbcTemplate.update(sh.getSQL(), sh.getValues());
    }
}
