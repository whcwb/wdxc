package com.ldz.service.biz.impl;

import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.dao.biz.mapper.ClGpsLsMapper;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.service.biz.interfaces.GpsLsService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GpsLsServiceImpl extends BaseServiceImpl<ClGpsLs, String> implements GpsLsService {
	@Autowired
	private ClGpsLsMapper entityMapper;

	@Override
	protected Mapper<ClGpsLs> getBaseMapper() {
		return entityMapper;
	}

	@Override
	protected Class<?> getEntityCls() {
		return ClGpsLs.class;
	}

	@Override
	public ApiResponse<String> saveEntity(ClGpsLs entity) {
		save(entity);
		return ApiResponse.saveSuccess();
	}

	public List<ClGpsLs> getGpsLs(gpsSJInfo gpsSJInfo){
		return entityMapper.getZdbhAllLsGps(gpsSJInfo);
	}

	@Override
	public long getMaxSpeed(String zdbh, String time) {
		return entityMapper.getMaxSpeed(zdbh,time);
	}

    @Override
    public Map<String, Double> getMSpeedAndAvgSpeed(String zdbh, String startTime, String endTime) {

		return entityMapper.getMSpeedAndAvgSpeed(zdbh, startTime,endTime);
    }

    @Override
    public List<Map<String, BigDecimal>> getJdAndWd(String zdbh, Date cjsj) {

		return entityMapper.getJdAndWd(zdbh,cjsj);
    }

}
