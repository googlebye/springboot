package com.lang.springboot.dto;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

public class RegistInfo {

    private String                    serverType;
    private String                    version;
    private String                    mac;
    private List<Map<String, Object>> hotelGroups;
    private List<Map<String, Object>> hotels;

    public String getGrps() {
        String grps = "";
        
        for(Map<String,Object> map : hotelGroups){
            String code = MapUtils.getString(map, "code");
            grps = grps + code + ",";
        }

        if (grps.endsWith(",")) {
            grps = grps.substring(0, grps.length() - 1);
        }

        return grps;
    }

    public String getHtls() {
        String grps = "";

        for(Map<String,Object> map : hotels){
            String code = MapUtils.getString(map, "code");
            grps = grps + code + ",";
        }

        if (grps.endsWith(",")) {
            grps = grps.substring(0, grps.length() - 1);
        }

        return grps;
    }

    public List<Map<String, Object>> getHotelGroups() {
        return hotelGroups;
    }

    public void setHotelGroups(List<Map<String, Object>> hotelGroups) {
        this.hotelGroups = hotelGroups;
    }

    public List<Map<String, Object>> getHotels() {
        return hotels;
    }

    public void setHotels(List<Map<String, Object>> hotels) {
        this.hotels = hotels;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }
}
