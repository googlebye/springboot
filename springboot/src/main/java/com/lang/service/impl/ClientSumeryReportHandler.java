package com.lang.service.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lang.dto.ReportDto;
import com.lang.service.IClientService;
import com.lang.service.IReportHandler;

@Component
public class ClientSumeryReportHandler implements IReportHandler {
    
    @Autowired
    private IClientService clientService;
    
    @PostConstruct
    public void addReportHandler(){
        clientService.addReportHandlerMapping("clientSumery", this);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Boolean handleReport(ReportDto reportDto) {
        Boolean hasNewVersion = false;
        
        Map<String, Object> clientStatus = (Map<String, Object>) reportDto.getDetail();
        String serverType = MapUtils.getString(clientStatus, "serverType");
        String currentVersion = MapUtils.getString(clientStatus, "version");
        String mac = MapUtils.getString(clientStatus, "mac");
        String uuid = MapUtils.getString(clientStatus, "uuid");
        
        hasNewVersion = clientService.checkVersion(serverType, mac, uuid, currentVersion);
        
        return hasNewVersion;
    }

}
