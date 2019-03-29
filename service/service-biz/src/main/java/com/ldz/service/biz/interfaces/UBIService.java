package com.ldz.service.biz.interfaces;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.ldz.dao.biz.model.ClXc;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.dao.biz.model.ClZdglReport;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

public interface UBIService extends BaseService<ClZdglReport, String> {
	
	/**
	 * 第三方接口调用绑定终端
	 * @param data
	 * @return
	 */
	public ApiResponse<String> bindDevice(ClZdgl data);
   
	/**
	 * UBI基础数据统计接口
	 * 里程总数，今日新增数量，本月新增数量，本年新增数量，
	 * 驾驶时长总数，今日新增数量，本月新增数量，本年新增数量
	 * @param zdlx
	 * @param zdbhs
	 * @return
	 */
	public ApiResponse<Map<String, Object>> totalBase(String zdbhs);
	
	/**
	 * 设备行驶里程查询接口。起始时间-当前的总里程数
	 * @param rangedate
	 * @param zdbhs
	 * @return
	 */
	public ApiResponse<List<Map<String, Object>>> totalMiles(String rangedate, String zdbhs);
	
	/**
	 * 设备平均里程查询接口
	 * @param rangedate
	 * @param zdbhs
	 * @return
	 */
	public ApiResponse<List<Map<String, Object>>> totalAvgmiles(String rangedate, String zdbhs);
	
	public ApiResponse<Map<String, Integer>> analysis(String zdbhs);

    ApiResponse<List<Map<String, Object>>> getRecentXc(String deviceId, Page<ClXc> page);
}
