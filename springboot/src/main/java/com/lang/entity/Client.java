package com.lang.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = true, updatable = false)
    private Long        id;

    private String      name;

    private String      type;
    
    private String      mac;
    
    private String      grps;
    
    private String      htls;
    
    private String      version;
    
    private BigDecimal  progress;

    @ManyToOne
    @JoinColumn(name = "client_group")
    private ClientGroup group;

    public Client(){
        this.progress = BigDecimal.ZERO;
    }

    public Client(String name, String type, String mac, String grps, String htls, String version, ClientGroup group){
        this();

        this.name = name;
        this.type = type;
        this.mac = mac;
        this.grps = grps;
        this.htls = htls;
        this.version = version;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGrps() {
        return grps;
    }

    public void setGrps(String grps) {
        this.grps = grps;
    }

    public String getHtls() {
        return htls;
    }

    public void setHtls(String htls) {
        this.htls = htls;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BigDecimal getProgress() {
        return progress;
    }

    public void setProgress(BigDecimal progress) {
        this.progress = progress;
    }

    public ClientGroup getGroup() {
        return group;
    }

    public void setGroup(ClientGroup group) {
        this.group = group;
    }

}
