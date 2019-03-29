package com.ldz.wechat.module.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.SysYjfk;


public interface YjService extends BaseService<SysYjfk,String>{
	
	public ApiResponse<String> saveEntity(SysYjfk entity);
	
	public ApiResponse<String> updateEntity(SysYjfk entity);
}
