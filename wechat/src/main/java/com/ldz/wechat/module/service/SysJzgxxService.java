package com.ldz.wechat.module.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.module.model.SysJzgxx;

import java.util.Map;

public interface SysJzgxxService {

	/*
	 * 通过身份证,姓名获取到教职工信息
	 */

	ApiResponse<String> findJzg(String idCard,String name);

	SysJzgxx findById(String id);

    ApiResponse<Map<String,Object>> getUserInfo();
}
