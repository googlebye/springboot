package com.lang.service;

import java.io.File;
import java.util.List;

import com.lang.dto.DownloadDto;
import com.lang.dto.RegistDto;
import com.lang.dto.ReportDto;

public interface IClientService {

    Long register(RegistDto registInfo);

    boolean checkVersion(String type, String mac, String uuid, String currentVersion);

    File getFile(Long clientId);

    void addReportHandlerMapping(String report, IReportHandler handler);

    boolean handleReports(List<ReportDto> reportDtoList);

    void handleDownloadingProgress(DownloadDto downloadDto);

}
