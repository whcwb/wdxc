package com.ldz.job.service;

import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.sys.base.BaseService;

public interface GpsLsService extends BaseService<ClGpsLs, String>{
	long getMaxSpeed(String zdbh, String time);
}
