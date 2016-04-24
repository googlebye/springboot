package com.lang.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.lang.dao.IClientDao;
import com.lang.dto.DownloadDto;
import com.lang.dto.RegistDto;
import com.lang.dto.ReportDto;
import com.lang.entity.Client;
import com.lang.entity.ClientGroup;
import com.lang.service.IClientService;
import com.lang.service.IGroupService;
import com.lang.service.IGrpsService;
import com.lang.service.IHtlsService;
import com.lang.service.IReportHandler;
import com.lang.utils.ZipUtil;

@Service
@Transactional
public class ClientServiceImpl implements IClientService {

    private static Logger                   log                  = Logger.getLogger(IClientService.class);
    @Autowired
    private IClientDao                      clientDao;
    @Autowired
    private IGroupService                   groupService;
    @Autowired
    private IHtlsService                    htlsService;
    @Autowired
    private IGrpsService                    grpsService;

    /**
     * 处理客户端报告状态的类
     */
    private HashMap<String, IReportHandler> reportHandlerMapping = new HashMap<String, IReportHandler>();

    @Override
    public void addReportHandlerMapping(String report, IReportHandler handler) {
        reportHandlerMapping.put(report, handler);
    }

    public Long register(RegistDto registEntity) {
        String name = registEntity.getName();
        String type = registEntity.getServerType();
        String mac = registEntity.getMac();
        String grps = registEntity.getGrps();
        String htls = registEntity.getHtls();
        String version = registEntity.getVersion();

        Iterable<Client> list = clientDao.findAll();

        Client sameGroupClient = null;

        for (Client client : list) {
            if (client.getType().equalsIgnoreCase(type) && client.getMac().equalsIgnoreCase(mac)) {
                // 已经注册过
                return client.getId();
            }

            List<String> registedList = Arrays.asList(client.getHtls().split(","));
            List<String> uRegistedList = Arrays.asList(htls.split(","));

            if (registedList.containsAll(uRegistedList) || uRegistedList.containsAll(registedList)) {
                // 如果有同组的注册了
                sameGroupClient = client;
            }
        }

        String uuid = null;

        if (sameGroupClient != null) {
            uuid = sameGroupClient.getGroup().uuid;
        } else {
            uuid = UUID.randomUUID().toString();
        }

        ClientGroup clientGroup = groupService.insertIfAbsent(uuid);
        grpsService.insertIfAbsent(registEntity.getHotelGroups());
        htlsService.insertIfAbsent(registEntity.getHotels());

        uuid = clientGroup.uuid;
        Client newClient = new Client(name, type, mac, grps, htls, version, clientGroup);
        clientDao.save(newClient);

        return newClient.getId();
    }

    public boolean checkVersion(String type, String mac, String uuid, String currentVersion) {
        /*
         * Client client = clientDao.findByTypeAndMac(type, mac); Group group = groupService.findByUUID(uuid); if (group
         * != null) { String isReady = group.isReady; String newVersion = group.version;
         * clientDao.updateRegistedVersion(type, mac, currentVersion); if (!currentVersion.equalsIgnoreCase(newVersion)
         * && "T".equals(isReady)) { return true; } } else { throw new
         * RuntimeException("method:checkVersion group not found,uuid=" + uuid); }
         */

        return false;
    }

    public File getFile(String type, String uuid) {

        File zipFile = null;
        ClientGroup group = groupService.findByUUID(uuid);
        String path = group.path;

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

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleReports(List<ReportDto> reportDtoList) {
        boolean hasNewVersion = false;

        for (ReportDto reportDto : reportDtoList) {
            IReportHandler handler = reportHandlerMapping.get(reportDto.getCategory());
            Assert.notNull(handler, "No handler for this report.[code=" + reportDto.getCategory() + "]");

            if ("clientSumery".equalsIgnoreCase(reportDto.getCategory())) {
                hasNewVersion = handler.handleReport(reportDto);
            } else {
                handler.handleReport(reportDto);
            }
        }

        return hasNewVersion;
    }

    @Override
    public void handleDownloadingProgress(DownloadDto downloadDto) {
        BigDecimal d1 = new BigDecimal(downloadDto.getAvailable());
        BigDecimal d2 = new BigDecimal(downloadDto.getTotalSize());

        BigDecimal percent = d1.divide(d2, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

    }

    @Override
    public File getFile(Long clientId) {
        return null;
    }
}
