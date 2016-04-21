package com.lang.dao;

import java.util.List;
import java.util.Map;

public interface IClientDao {

    List<Map<String, Object>> listClients(String type, String mac);

    List<Map<String, Object>> listClients();

    void register(String type, String mac, String grps, String htls, String uuid, String version);

    void updateRegistedVersion(String type, String mac, String currentVersion);

}
