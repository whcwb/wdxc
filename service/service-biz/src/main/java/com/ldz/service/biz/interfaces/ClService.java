package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.model.ClCl;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.util.List;
import java.util.Map;

public interface ClService extends BaseService<ClCl,String>{
    ClCl findByOrgCode(String code);
    List<ClCl> getOrgCarList(String orgCode);
    ApiResponse<String> saveEntity(ClCl entity);
    ApiResponse<String> updateEntity(ClCl entity);

    ApiResponse<List<Map<String,Object>>> getVehicleTypeStatistics(String zxzt);

	ApiResponse<List<ClCl>> nianshen(ClCl car);

    ApiResponse<Map<String,Object>> carAccStatistics(Integer days,String type);

	//获取当前登陆用户获取车辆年审饼图
	ApiResponse<Map<String, Integer>> getnianshen();

    ApiResponse<String> unbindDevice(String carId);
    ApiResponse<String> unbindDriver(String carId);

    ApiResponse<String> bindDriver(String carId, String driverId);

    ApiResponse<String> bindDevice(String carId, String devcieId);
}
