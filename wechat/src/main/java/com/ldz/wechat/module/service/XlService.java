package com.ldz.wechat.module.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.ClClyxjl;
import com.ldz.wechat.module.model.ClXl;
import com.ldz.wechat.module.model.ClZd;

import java.util.List;
import java.util.Map;

public interface XlService extends BaseService<ClXl,String> {
    List<ClXl> getAll(String lx);

    ApiResponse<Map<String,Object>> getBySiteVehicleList(String xlId);

    ApiResponse<List<Integer>> getNextCars(String xlId, String zdId);

    List<ClZd> getStationList(String xlId);

    ApiResponse<List<Map<String,Object>>> getStationGpsList(String xlId);

    ApiResponse<List<ClClyxjl>> getBusPositions(String xlId);
}
