package com.lang.dao;

import org.springframework.data.repository.CrudRepository;

import com.lang.entity.Grp;


public interface IGrpsDao extends CrudRepository<Grp, Long>{

    Grp findByCode(String code);

}
