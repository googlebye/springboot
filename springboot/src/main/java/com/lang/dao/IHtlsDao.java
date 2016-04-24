package com.lang.dao;

import org.springframework.data.repository.CrudRepository;

import com.lang.entity.Htl;


public interface IHtlsDao extends CrudRepository<Htl, Long>{

    Htl findByCode(String code);

}
