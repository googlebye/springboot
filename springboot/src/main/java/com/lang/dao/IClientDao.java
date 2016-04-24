package com.lang.dao;

import org.springframework.data.repository.CrudRepository;

import com.lang.entity.Client;

public interface IClientDao extends CrudRepository<Client, Long>{

    Client findByTypeAndMac(String type, String mac);

}
