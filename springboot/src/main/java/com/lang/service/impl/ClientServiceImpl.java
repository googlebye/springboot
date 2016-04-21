package com.lang.service.impl;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lang.dao.IClientDao;
import com.lang.dto.RegistDto;
import com.lang.service.IGroupService;
import com.lang.service.IGrpsService;
import com.lang.service.IHtlsService;
import com.lang.service.IClientService;
import com.lang.utils.ZipUtil;

@Service
public class ClientServiceImpl implements IClientService {

    private static Logger log = Logger.getLogger(IClientService.class);
    @Autowired
    private IClientDao    clientDao;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private IHtlsService  htlsService;
    @Autowired
    private IGrpsService  grpsService;

    public String register(RegistDto registEntity) {

        String type = registEntity.getServerType();
        String mac = registEntity.getMac();
        String grps = registEntity.getGrps();
        String htls = registEntity.getHtls();
        String version = registEntity.getVersion();

        List<Map<String, Object>> list = clientDao.listClients();
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
        grpsService.insertIfAbsent(registEntity.getHotelGroups());
        htlsService.insertIfAbsent(registEntity.getHotels());
        clientDao.register(type, mac, grps, htls, uuid, version);

        return uuid;
    }

    public List<Map<String, Object>> listRegistred() {
        return clientDao.listClients();
    }

    public boolean checkVersion(String type, String mac, String uuid, String currentVersion) {
        List<Map<String, Object>> list = clientDao.listClients(type, mac);

        if (list.size() == 0) {
            log.info("method:checkVersion not regist,mac=" + mac + ",type=" + type);
            return false;
        } else if (list.size() > 1) {
            log.info("method:checkVersion duplicate registed,mac=" + mac + ",type=" + type);
            return false;
        } else {
            Map<String, Object> group = groupService.findByUUID(uuid);

            if (group != null) {

                String isReady = MapUtils.getString(group, "is_ready");
                String newVersion = MapUtils.getString(group, "version");
                
                clientDao.updateRegistedVersion(type, mac, currentVersion);

                if (!currentVersion.equalsIgnoreCase(newVersion) && "T".equals(isReady)) {
                    return true;
                }
                
            } else {
                throw new RuntimeException("method:checkVersion group not found,uuid=" + uuid);
            }
        }

        return false;
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
