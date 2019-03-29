package com.ldz.api.module.app.controller;

import com.ldz.service.biz.interfaces.ClZdglReportService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/app/report")
public class ReportCtrl {

    @Autowired
    private ClZdglReportService reportService;

    /**
     * 周里程
     */
    @PostMapping("/getWeekMile")
    public ApiResponse<List<List<String>>> getWeekMile(String deviceId){
        return reportService.getWeekMile(deviceId);
    }

    /**
     * 月里程
     */
    @PostMapping("/getMonthMile")
    public ApiResponse<List<List<String>>> getMonthMile(String deviceId){
        return reportService.getMonthMile(deviceId);
    }

    /**
     * 天里程
     */
    @PostMapping("/getDayMile")
    public ApiResponse<List<List<String>>> getDayMile(String deviceId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return reportService.getDayMile(deviceId);
    }



}
