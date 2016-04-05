package com.lang.dao;

import java.util.List;
import java.util.Map;

public interface IServerDao {

    List<Map<String, Object>> listRegistred(String type, String mac);

    List<Map<String, Object>> listRegistred();

    void register(String type, String mac, String grps, String htls, String uuid);

    void updateRegistedVersion(String type, String mac, String currentVersion);

}
