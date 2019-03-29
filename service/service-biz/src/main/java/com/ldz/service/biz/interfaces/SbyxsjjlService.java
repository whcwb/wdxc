package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.bean.ClLsGjInfo;
import com.ldz.dao.biz.bean.CsTxTj;
import com.ldz.dao.biz.bean.SafedrivingModel;
import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.dao.biz.model.ClSbyxsjjl;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.TrackPointsForReturn.Point;

import java.util.List;

public interface SbyxsjjlService extends BaseService<ClSbyxsjjl,String>{
    ApiResponse<String> saveEntity(ClSbyxsjjl entity);
    ApiResponse<List<ClLsGjInfo>> historyTrajectory(gpsSJInfo gpssjinfo);
	ApiResponse<List<ClGpsLs>> getGuiJiGps(gpsSJInfo gpssjinfo);
	
	ApiResponse<List<SafedrivingModel>> getSafeDrivig();
	//获取时间段内超数值
	ApiResponse<CsTxTj> getcs(String cph, String day);
	
	 ApiResponse<List<Point>> baiduGuiJi(gpsSJInfo gpssjinfo);

	ApiResponse<List<com.ldz.util.bean.Point>> getYyGuiJi(gpsSJInfo gpssjinfo);

	ClSbyxsjjl getOneForOnline(String deviceId);

}
