package com.ldz.znzp.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.util.gps.Gps;
import com.ldz.znzp.base.BaseService;
import com.ldz.znzp.bean.GpsInfo;
import com.ldz.znzp.model.*;

import java.math.BigDecimal;

public interface ClService extends BaseService<ClCl,String>{
    ApiResponse<String> saveEntity(ClCl entity);

    ApiResponse<String> report(String tid,ClPb pb,ClCl car,ClXl route,ClClyxjl clClyxjl);

    ApiResponse<String> updateGps(GpsInfo gpsInfo, ClPb pb, ClCl car, ClXl route, ClClyxjl clClyxjl);


    ClZd findCurrentZd(String prvStationId,Gps currentGps, ClCl car, ClPb pb);

    ClPb getCarPb(String carId);
    ClPb getCarPbByDeviceId(String deviceId);

    ClCl getByDeviceId(String deviceId);

    ClZd getCurrentZd(BigDecimal jd, BigDecimal wd, ClCl car, String currentZdId,ClPb pb);

    ApiResponse<String> report(String tid);

    ApiResponse<String> updateGpsNew(GpsInfo gpsInfo, ClPb pb,ClCl car,ClXl route, ClClyxjl record);
    void initXlZd(String xlId);
}
