package com.lang.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClientGroup {

    @Id
    public String uuid;
    public String version;
    public String path;
    public String isReady;

}
