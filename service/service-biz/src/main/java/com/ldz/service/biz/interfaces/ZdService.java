package com.ldz.service.biz.interfaces;

import java.util.List;
import java.util.Map;

import com.ldz.dao.biz.bean.DdClModel;
import com.ldz.dao.biz.model.ClXl;
import com.ldz.dao.biz.model.ClZd;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

public interface ZdService extends BaseService<ClZd,String>{
    ApiResponse<String> saveEntity(ClZd entity);
    ApiResponse<String> updateEntity(ClZd entity);
    List<ClZd> getByXlId(String xlId);
    List<ClZd> getByXlIds(List<String> xlIds);

    void setStationOrder(ClZd station);

    /**
     * 获取线路下站点和车辆的信息
     * @param xlId  线路ID
     * @return
     */
    ApiResponse<List<DdClModel>> getBySiteVehicleList(String xlId);
    List<ClZd> getNearbyStations(String lng,String lat);


    List<ClXl> getNearbyRoutes(String lng, String lat);

    Map<String,Object> getNearbyRoutesAndStations(String lng, String lat);

    ApiResponse<List<ClZd>> getNotBindList(String stationId);

    ApiResponse<Map<String,Object>> getStationInfo(String lng, String lat);
}
