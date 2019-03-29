package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.model.ClZdglReport;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ClZdglReportService extends BaseService<ClZdglReport, String> {

     void saveBatch(List<ClZdglReport> list);

    ApiResponse<List<List<String>>> getWeekMile(String deviceId);

    ApiResponse<List<List<String>>> getMonthMile(String deviceId);

    ApiResponse<List<List<String>>> getDayMile(String deviceId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException;
}