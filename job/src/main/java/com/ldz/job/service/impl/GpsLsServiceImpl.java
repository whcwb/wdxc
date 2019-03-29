package com.ldz.job.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.dao.biz.mapper.ClGpsLsMapper;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.job.service.GpsLsService;
import com.ldz.sys.base.BaseServiceImpl;

import tk.mybatis.mapper.common.Mapper;

@Service
public class GpsLsServiceImpl extends BaseServiceImpl<ClGpsLs, String> implements GpsLsService {
	@Autowired
	private ClGpsLsMapper entityMapper;


	@Override
	protected Mapper<ClGpsLs> getBaseMapper() {
		return entityMapper;
	}


	@Override
	public long getMaxSpeed(String zdbh, String time) {
		return entityMapper.getMaxSpeed(zdbh,time);
	}
}
