package com.ldz.dao.device.mapper;

import com.ldz.dao.device.bean.gpsSJInfo;
import com.ldz.dao.device.model.ClGpsLs;
import com.ldz.util.mapperprovider.InsertListMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ClGpsLsMapper extends Mapper<ClGpsLs>, InsertListMapper<ClGpsLs> {
	
	List<ClGpsLs> getZdbhAllLsGps(gpsSJInfo info);

	@Select(" SELECT  IFNULL(MAX(cast(YXSD as SIGNED INTEGER)),0) from cl_gps_ls where ZDBH = #{zdbh} and DATE_FORMAT(cjsj,'%Y-%m-%d') like '${time}%' ")
	long getMaxSpeed(@Param("zdbh") String zdbh, @Param("time") String time);
}