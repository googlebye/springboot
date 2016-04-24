package com.lang.service;

import com.lang.dto.ReportDto;


public interface IReportHandler {

    <T,E> E handleReport(ReportDto<T> reportDto);
    
}
