package com.lang.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.lang.dao.IGrpsDao;
import com.lang.utils.SQLHelper;
@Repository
public class GrpsDaoImpl implements IGrpsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> findByCode(String code) {
        SQLHelper sh = new SQLHelper("select * from grps where code = ?");
        sh.insertValue(code);

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sh.getSQL(), sh.getValues());

        if (list.size() > 0) {
            return list.get(0);
        }
        
        return null;
    }

    public void insert(String code, String descript) {
        SQLHelper sh = new SQLHelper("insert into grps(code,descript) values (?,?)");
        sh.insertValue(code);
        sh.insertValue(descript);

        jdbcTemplate.update(sh.getSQL(), sh.getValues());
    }

}
