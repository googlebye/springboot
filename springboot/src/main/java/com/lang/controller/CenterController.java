package com.lang.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

    /**
     * 终端向服务器注册，如果没有注册过，会将注册信息里面的集团、酒店信息存入服务端的表里面;
     * <p>
     * 如果已经注册过，则返回上传注册时所属归类的唯一编码
     * 
     * @param registInfo
     * @return 此终端所属归类的唯一编码
     */
    @RequestMapping(value = "/ctrl/register", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public Long register(@RequestBody RegistDto registInfo) {
        return clientService.register(registInfo);
    }
    

    /**
     * 终端向服务器报告状态，服务器返回是否有新版本
     * 
     * @param reportDtoList
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    @RequestMapping(value = "/ctrl/report", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public boolean report(@RequestBody List<ReportDto> reportDtoList) {
        return clientService.handleReports(reportDtoList);
    }

    
    @RequestMapping(value = "/ctrl/download", produces = MediaTypes.JSON_UTF_8, consumes = MediaTypes.JSON_UTF_8)
    public void download(@RequestBody DownloadDto downloadDto, HttpServletResponse response) throws IOException {

        if (downloadDto.getIsDownloading()) {
            // Client is downloading files,print the progress on screen
            clientService.handleDownloadingProgress(downloadDto);
            return;
        }

        File file = clientService.getFile(downloadDto.getClientId());

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
