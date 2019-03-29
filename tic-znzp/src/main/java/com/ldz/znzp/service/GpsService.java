package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.bean.GpsInfo;
import com.ldz.znzp.model.ClGps;

public interface GpsService extends BaseService<ClGps,String>{
    ApiResponse<String> saveEntity(ClGps entity);

    /*
     * 将tic-server传入的gps信息转换百度 谷歌 经纬度.
     * @parm GpsInfo
     * @Return ClGps
     */
    ClGps  changeCoordinates(GpsInfo entity);

}
