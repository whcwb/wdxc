package com.ldz.biz.api;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.dao.biz.model.ClXc;
import com.ldz.service.biz.interfaces.SbyxsjjlService;
import com.ldz.service.biz.interfaces.UBIService;
import com.ldz.service.biz.interfaces.XcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.redis.RedisTemplateUtil;

/*
 * 为UBI系统提供数据接口
 *
 */
@RestController
@RequestMapping("/ubi/")
public class UBIApi {

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;
	@Autowired
	private UBIService ubiService;
	@Autowired
	private SbyxsjjlService service;
	
	/**
	 * 终端绑定接口，验证调用者发送的终端号是否正确并且已经在系统中登记，同时记录调用方发送的绑定信息
	 * 1、验证终端编号是否属于接口调用者名下；
	 * 2、验证终端编号是否已经入库；
	 * 3、记录发送过来的json数据值，暂时只做记录，无实际用途
	 * @param data
	 * @return
	 */
	@PostMapping("/bindDevice")
	public ApiResponse<String> bindDevice(@RequestBody ClZdgl data) {
		return ubiService.bindDevice(data);
	}

	/**
	 * 统计内容如下：
	 * 里程总数，今日新增数量，本月新增数量，本年新增数量，
	 * 驾驶时长总数，今日新增数量，本月新增数量，本年新增数量
	 * @param zdbhs	终端编号。如果需要指定查询就可以传终端编号值，支持多个使用','分隔
	 * @return
	 */
	@PostMapping("/total-base")
	public ApiResponse<Map<String, Object>> totalBase(@RequestParam(required=false, value="zdbhs")String zdbhs) {
		return ubiService.totalBase(zdbhs);
	}

	/**
	 * 设备行驶里程查询接口
	 * 参数：设备号，起始时间
	 * 返回：起始时间-当前的总里程数
	 * @param rangedate	起始时间。格式：2018-12-01,2018-12-02，开始和结束时间使用逗号分隔
	 * @param zdbhs		设备号。可多个查询，多个使用','分隔
	 * @return
	 */
	@PostMapping("/total-miles")
	public ApiResponse<List<Map<String, Object>>> totalMiles(@RequestParam(required=false, value="rangedate")String rangedate, @RequestParam(value="zdbhs")String zdbhs) {
		return ubiService.totalMiles(rangedate, zdbhs);
	}
	
	/**
	 * 设备平均里程
	 * 参数：类型：周|月|天
	 * 返回：周平均里程，月平均里程，每天分时段平均里程
	 * @param rangedate	起始时间。格式：2018-12-01,2018-12-02，开始和结束时间使用逗号分隔
	 * @param zdbhs		设备号。可多个查询，多个使用','分隔
	 * @return
	 */
	@PostMapping("/total-avgmiles")
	public ApiResponse<List<Map<String, Object>>> totalAvgMiles(@RequestParam(required=false, value="rangedate")String rangedate, @RequestParam(required=false, value="zdbhs")String zdbhs) {
		return ubiService.totalAvgmiles(rangedate, zdbhs);
	}
	
	@PostMapping("/analysis")
	public ApiResponse<Map<String, Integer>> analysis(@RequestParam(required=false, value="zdbhs")String zdbhs) {
		return ubiService.analysis(zdbhs);
	}

	/**
	 * 根据终端编号查询最近的三段行程
	 */
	@PostMapping("/recentXc")
	public ApiResponse<List<Map<String,Object>>> getRecentXc(String deviceId, Page<ClXc> page){
		return ubiService.getRecentXc(deviceId, page);
	}
	/**
	 * 查询本地库中的百度鹰眼历史数据
	 * @param gpssjinfo
	 * @return
	 */
	@PostMapping("/yyguiji")
	public ApiResponse<List<com.ldz.util.bean.Point>> getYyGuiJi(gpsSJInfo gpssjinfo){
		return service.getYyGuiJi(gpssjinfo);
	}


}
