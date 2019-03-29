package com.ldz.wechat.module.controller;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.module.bean.NearbyStation;
import com.ldz.wechat.module.model.ClClyxjl;
import com.ldz.wechat.module.model.ClXl;
import com.ldz.wechat.module.model.ClZd;
import com.ldz.wechat.module.model.SysHdyx;
import com.ldz.wechat.module.service.HdService;
import com.ldz.wechat.module.service.XlService;
import com.ldz.wechat.module.service.ZdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("put/xl")
public class ClXlCtrl {
    @Autowired
    private XlService wxXlService;

    @Autowired
    private HdService wxHdService;
    @Autowired
	private ZdService zdservice;
    @RequestMapping("getStationList")
	public ApiResponse<List<ClZd>> getStationList(String xlId){
    	return ApiResponse.success(wxXlService.getStationList(xlId));
	}

    @RequestMapping("getStationGpsList")
	public ApiResponse<List<Map<String,Object>>> getStationGpsList(String xlId){
    	return wxXlService.getStationGpsList(xlId);
	}

	@RequestMapping("getBusPositions")
	public ApiResponse<List<ClClyxjl>> getBusPositions(String xlId){
    	return wxXlService.getBusPositions(xlId);
	}
	/**
	 * 查询所有线路
	 * @return
	 */
	@RequestMapping(value="/getAll", method={RequestMethod.POST})
	public ApiResponse<List<ClXl>> getAll(String lx){
		return ApiResponse.success(wxXlService.getAll(lx));
	}

	/**
	 * 获取线路下站点和车辆的信息
	 * @param xlid	线路ID
	 * @return
	 */
	@RequestMapping(value = "/getzdcl", method = {RequestMethod.POST})
	public ApiResponse<Map<String,Object>> getBySiteVehicleList(String xlid) {
		return wxXlService.getBySiteVehicleList(xlid);
	}

	@RequestMapping("getNextCars")
	public ApiResponse<List<Integer>> getNextCars(String xlId,String zdId){
		return wxXlService.getNextCars(xlId,zdId);
	}
	/**
	 * 获取活动图片列表 也就是广告位的图片列表
	 */
	@RequestMapping(value = "/getadsense", method = {RequestMethod.POST})
	public ApiResponse<List<SysHdyx>> getAdsenseList() {
		return ApiResponse.success(wxHdService.getAdsenseList());
	}


	@RequestMapping("getStationInfo")
	public ApiResponse<Map<String,Object>> getStationInfo(String lng,String lat){
		return zdservice.getStationInfo(lng,lat);
	}
//
//	@RequestMapping("getNearbyStations")
//	public ApiResponse<List<NearbyStation>> getNearbyStations(String lng, String lat){
//		return ApiResponse.success(zdservice.getNearbyStations(lng,lat));
//	}

}
