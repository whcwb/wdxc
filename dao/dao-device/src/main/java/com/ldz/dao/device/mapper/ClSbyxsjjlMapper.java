package com.ldz.dao.device.mapper;

import com.ldz.dao.device.bean.SafedrivingModel;
import com.ldz.dao.device.bean.gpsSJInfo;
import com.ldz.dao.device.model.ClSbyxsjjl;
import com.ldz.util.mapperprovider.InsertListMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClSbyxsjjlMapper extends Mapper<ClSbyxsjjl> , InsertListMapper<ClSbyxsjjl> {
	
	List<ClSbyxsjjl> historyTrajectory(gpsSJInfo gpssjinfo);
	
/*	
	List<ClSbyxsjjl> gpsInit();*/
	
	List<SafedrivingModel> Safedriving(Map<String, Object> param);
	
	List<ClSbyxsjjl> findByCphAndTime(@Param("cph") String cph, @Param("start") Date start, @Param("end") Date end);
	
}