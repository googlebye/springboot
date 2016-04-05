package com.lang.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.lang.springboot.dto.RegistInfo;

public interface IServerService {

    String register(RegistInfo registInfo);

    List<Map<String, Object>> listRegistred();

    String checkVersion(String type, String mac, String uuid, String currentVersion);

    File getFile(String type, String uuid);

}
