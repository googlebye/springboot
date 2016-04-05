package com.lang.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.lang.dao.IServerDao;
import com.lang.service.IGroupService;
import com.lang.service.IGrpsService;
import com.lang.service.IHtlsService;
import com.lang.service.IServerService;
import com.lang.springboot.dto.RegistInfo;
import com.lang.utils.ZipUtil;

public class ServerServiceImpl implements IServerService {

    private static Logger log = Logger.getLogger(IServerService.class);
    private IServerDao    serverDao;
    private IGroupService groupService;
    private IHtlsService  htlsService;
    private IGrpsService  grpsService;

    public void setGroupService(IGroupService groupService) {
        this.groupService = groupService;
    }

    public void setHtlsService(IHtlsService htlsService) {
        this.htlsService = htlsService;
    }

    public void setGrpsService(IGrpsService grpsService) {
        this.grpsService = grpsService;
    }

    public void setServerDao(IServerDao serverDao) {
        this.serverDao = serverDao;
    }

    public String register(RegistInfo registInfo) {
        String type = registInfo.getServerType();
        String mac = registInfo.getMac();
        String grps = registInfo.getGrps();
        String htls = registInfo.getHtls();
        List<Map<String, Object>> list = serverDao.listRegistred();
        Map<String, Object> sameGroup = null;

        for (Map<String, Object> map : list) {
            if (MapUtils.getString(map, "type").equalsIgnoreCase(type) && MapUtils.getString(map, "mac").equalsIgnoreCase(mac)) {
                // 已经注册过
                return MapUtils.getString(map, "uuid");
            }

            List<String> registedList = Arrays.asList(MapUtils.getString(map, "htls").split(","));
            List<String> uRegistedList = Arrays.asList(htls.split(","));

            if (registedList.containsAll(uRegistedList) || uRegistedList.containsAll(registedList)) {
                // 如果有同组的注册了
                sameGroup = map;
            }
        }

        String uuid = null;

        if (sameGroup != null) {
            uuid = MapUtils.getString(sameGroup, "uuid");
        } else {
            uuid = UUID.randomUUID().toString();
        }

        groupService.insertIfAbsent(uuid);
        grpsService.insertIfAbsent(registInfo.getHotelGroups());
        htlsService.insertIfAbsent(registInfo.getHotels());
        serverDao.register(type, mac, grps, htls, uuid);

        return uuid;
    }

    public List<Map<String, Object>> listRegistred() {
        return serverDao.listRegistred();
    }

    public String checkVersion(String type, String mac, String uuid, String currentVersion) {
        List<Map<String, Object>> list = serverDao.listRegistred(type, mac);

        if (list.size() == 0) {
            log.info("method:checkVersion not regist,mac=" + mac + ",type=" + type);
            return currentVersion;
        } else if (list.size() > 1) {
            log.info("method:checkVersion duplicate registed,mac=" + mac + ",type=" + type);
            return currentVersion;
        } else {
            Map<String, Object> group = groupService.findByUUID(uuid);

            if (group != null) {

                String newVersion = MapUtils.getString(group, "version");
                String registedVersion = MapUtils.getString(list.get(0), "version");

                if (!currentVersion.equalsIgnoreCase(registedVersion)) {
                    serverDao.updateRegistedVersion(type, mac, currentVersion);
                }

                return newVersion;
            } else {
                throw new RuntimeException("method:checkVersion group not found,uuid=" + uuid);
            }
        }

    }

    public File getFile(String type, String uuid) {

        File zipFile = null;
        Map<String, Object> map = groupService.findByUUID(uuid);
        String path = MapUtils.getString(map, "path");

        path = path.endsWith("/") ? path : path + "/";

        if ("group".equalsIgnoreCase(type)) {
            zipFile = new File(path + "ipmsgroup.zip");
            if (!zipFile.exists()) {
                ZipUtil.doZip(new String[] { path + "ipmsgroup.war", path + "group_sql.tar.gz" }, path + "ipmsgroup.zip");
            }
        } else if ("member".equalsIgnoreCase(type)) {
            zipFile = new File(path + "ipmsmember.zip");
            if (!zipFile.exists()) {
                ZipUtil.doZip(new String[] { path + "ipmsmember.war", path + "member_sql.tar.gz" }, path + "ipmsmember.zip");
            }
        } else if ("thef".equalsIgnoreCase(type)) {
            zipFile = new File(path + "ipmsthef.zip");
            if (!zipFile.exists()) {
                ZipUtil.doZip(new String[] { path + "ipmsthef.war", path + "pms_sql.tar.gz" }, path + "ipmsthef.zip");
            }
        } else if ("ipms".equalsIgnoreCase(type)) {
            zipFile = new File(path + "ipms.zip");
            if (!zipFile.exists()) {
                ZipUtil.doZip(new String[] { path + "ipms.war", path + "pms_sql.tar.gz" }, path + "ipms.zip");
            }
        } else if ("sync".equalsIgnoreCase(type)) {
            zipFile = new File(path + "ipmssync.war");
        }

        return zipFile;
    }
}
