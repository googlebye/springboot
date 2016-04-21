package com.lang.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.lang.dto.RegistDto;

public interface IClientService {

    String register(RegistDto registInfo);

    List<Map<String, Object>> listRegistred();

    boolean checkVersion(String type, String mac, String uuid, String currentVersion);

    File getFile(String type, String uuid);

}
