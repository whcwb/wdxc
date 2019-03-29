package com.ldz.wechat.module.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.ClJsy;

import java.util.Map;

public interface ClJsyService extends BaseService<ClJsy,String> {

	/*
	 * 通过驾驶员身份证号码,驾驶员姓名获取驾驶员
	 */
	ApiResponse<Map<String,Object>> findJsy(String sfzhm, String xm);
	/*
	 * 修改驾驶员信息
	 */
	ApiResponse<String> updatejsy(ClJsy jsy);

	ClJsy findById(String id);

    ApiResponse<Map<String, Object>> getInfo();
}
