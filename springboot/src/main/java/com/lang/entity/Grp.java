package com.lang.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Grp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long   id;
    public String code;
    public String descript;

}
