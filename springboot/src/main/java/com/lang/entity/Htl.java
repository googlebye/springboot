package com.lang.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Htl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long   id;
    public String code;
    public String descript;
    
    @ManyToOne
    @JoinColumn(name = "grpcode")
    public Grp    grp;

}
