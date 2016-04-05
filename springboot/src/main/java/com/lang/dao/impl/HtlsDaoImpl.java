package com.lang.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.lang.dao.IHtlsDao;
import com.lang.utils.SQLHelper;

public class HtlsDaoImpl implements IHtlsDao {

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, Object> findByCode(String code) {
        SQLHelper sh = new SQLHelper("select * from htls where code = ? ");
        sh.insertValue(code);

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sh.getSQL(), sh.getValues());
        if(list.size() > 0){
            return list.get(0);
        }

        return null;
    }

    public void insert(String code, String descript, String grpcode) {
        SQLHelper sh = new SQLHelper("insert into htls(code,descript,grpcode) values (?,?,?)");
        sh.insertValue(code);
        sh.insertValue(descript);
        sh.insertValue(grpcode);
        
        jdbcTemplate.update(sh.getSQL(), sh.getValues());
    }

}
