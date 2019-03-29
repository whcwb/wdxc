package com.ldz.sys.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.mapper.SysHsgsMapper;
import com.ldz.sys.model.SysHsgs;
import com.ldz.sys.service.SysHsgsService;
import com.ldz.util.bean.ApiResponse;

import tk.mybatis.mapper.common.Mapper;
@Service
public class SysHsgsServiceImpl extends BaseServiceImpl<SysHsgs,String> implements SysHsgsService{
    @Autowired
    private SysHsgsMapper hsgsmapper;
  
	@Override
	protected Mapper<SysHsgs> getBaseMapper() {
		return hsgsmapper;
	}

	 @Override
	    public ApiResponse<String> validAndSave(SysHsgs e){
	        e.setId(genId());
	        e.setCjr(getOperateUser());
	        e.setCjsj(new Date());
	        save(e);
	        return ApiResponse.success();
	    }

	    @Override
	    public ApiResponse<String> validAndUpdate(SysHsgs e){
	        update(e);
	        return ApiResponse.success();
	    }
}
