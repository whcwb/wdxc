package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface GpsLsService extends BaseService<ClGpsLs,String>{
    ApiResponse<String> saveEntity(ClGpsLs entity);

    List<ClGpsLs> getGpsLs(gpsSJInfo gpsSJInfo);

    long getMaxSpeed(String zdbh, String time);

    Map<String,Double> getMSpeedAndAvgSpeed(String zdbh,String startTime , String endTime);

    List<Map<String, BigDecimal>> getJdAndWd(String zdbh, Date cjsj);
}
