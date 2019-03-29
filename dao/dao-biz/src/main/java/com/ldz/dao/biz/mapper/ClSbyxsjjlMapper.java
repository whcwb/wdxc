package com.ldz.dao.biz.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ldz.util.mapperprovider.InsertListMapper;
import org.apache.ibatis.annotations.Param;

import com.ldz.dao.biz.bean.SafedrivingModel;
import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.dao.biz.model.ClSbyxsjjl;

import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface ClSbyxsjjlMapper extends Mapper<ClSbyxsjjl> , InsertListMapper<ClSbyxsjjl> {
	
	List<ClSbyxsjjl> historyTrajectory(gpsSJInfo gpssjinfo);
	
/*	
	List<ClSbyxsjjl> gpsInit();*/
	
	List<SafedrivingModel> Safedriving(Map<String,Object> param);
	
	List<ClSbyxsjjl> findByCphAndTime(@Param("cph") String cph, @Param("start") Date start, @Param("end") Date end);

	@Select(" select * from CL_SBYXSJJL where zdbh = #{zdbh} and sjlx = '50' order by cjsj desc limit 1")
	ClSbyxsjjl getOneForOnline(@Param("zdbh") String deviceId);
}