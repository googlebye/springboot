package com.lang.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lang.constants.MediaTypes;
import com.lang.dto.DownloadDto;
import com.lang.dto.RegistDto;
import com.lang.dto.ReportDto;
import com.lang.service.IClientService;

@RestController
public class CenterController {

    private static Logger  logger = LoggerFactory.getLogger(CenterController.class);

    @Autowired
    private IClientService clientService;

    @RequestMapping(value = "/ctrl/hello", produces = MediaTypes.JSON_UTF_8)
    public HashMap<String, Object> hello() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("age", 12);
        map.put("name", "Lang");
        return map;
    }

    @RequestMapping(value = "/ctrl/register", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public String register(@RequestBody RegistDto registInfo) {
        return clientService.register(registInfo);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value = "/ctrl/report", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public boolean report(@RequestBody List<ReportDto> reportDtoList) {
        
        boolean hasNewVersion = false;

        for (ReportDto reportDto : reportDtoList) {
            if ("clientSumery".equalsIgnoreCase(reportDto.getCategory())) {
                Map<String, Object> clientStatus = (Map<String, Object>) reportDto.getDetail();
                
                String serverType = MapUtils.getString(clientStatus, "serverType");
                String currentVersion = MapUtils.getString(clientStatus, "version");
                String mac = MapUtils.getString(clientStatus, "mac");
                String uuid = MapUtils.getString(clientStatus, "uuid");
                
                hasNewVersion = clientService.checkVersion(serverType, mac, uuid, currentVersion);
            } else {
                // hadle other report info
            }
        }

        return hasNewVersion;
    }

    @RequestMapping(value = "/ctrl/download", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public void download(@RequestBody DownloadDto downloadEntity, HttpServletResponse response) throws IOException {

        if (downloadEntity.getIsDownloading()) {
            // Client is downloading files,print the progress on screen
            BigDecimal d1 = new BigDecimal(downloadEntity.getAvailable());
            BigDecimal d2 = new BigDecimal(downloadEntity.getTotalSize());

            String percent = d1.divide(d2, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "%";
            String d = d1.divide(BigDecimal.valueOf(1000000), 2, BigDecimal.ROUND_HALF_UP) + "M/" + d2.divide(BigDecimal.valueOf(1000000), 2, BigDecimal.ROUND_HALF_UP) + "M";

            logger.info("downloading.... " + d + " " + percent);
            return;
        }

        File file = clientService.getFile(downloadEntity.getType(), downloadEntity.getUuid());

        if (!file.exists()) {
            String errorMessage = "Sorry. The file you are looking for does not exist";
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            logger.info("mimetype is not detectable, will take default");
            mimeType = "application/x-msdownload;charset=utf-8";
        }

        logger.info("mimeType : " + mimeType);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setContentType(mimeType);
        response.setContentLength((int) file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        // Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
}
