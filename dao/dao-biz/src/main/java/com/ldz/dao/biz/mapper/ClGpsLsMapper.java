package com.ldz.dao.biz.mapper;

import com.ldz.dao.biz.bean.gpsSJInfo;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.util.mapperprovider.InsertListMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ClGpsLsMapper extends Mapper<ClGpsLs>, InsertListMapper<ClGpsLs> {
	
	List<ClGpsLs> getZdbhAllLsGps(gpsSJInfo info);

	@Select(" SELECT  IFNULL(MAX(cast(YXSD as SIGNED INTEGER)),0) from cl_gps_ls where ZDBH = #{zdbh} and DATE_FORMAT(cjsj,'%Y-%m-%d') like '${time}%' ")
	long getMaxSpeed(@Param("zdbh") String zdbh, @Param("time") String time);
	@Select("SELECT MAX(CONVERT(YXSD,DECIMAL)) max , AVG(CONVERT(YXSD,DECIMAL)) avg from cl_gps_ls where CJSJ >= #{startTime} and CJSJ <= #{endTime} and zdbh = #{zdbh}")
    Map<String, Double> getMSpeedAndAvgSpeed(@Param("zdbh") String zdbh,@Param("startTime") String startTime,@Param("endTime") String endTime);
	@Select(" select bdjd , bdwd from cl_gps_ls where cjsj >= #{cjsj} and zdbh = #{zdbh} order by cjsj asc")
    List<Map<String, BigDecimal>> getJdAndWd(@Param("zdbh") String zdbh, @Param("cjsj") Date cjsj);

	@Delete( " delete from CL_GPS_LS where ZDBH = #{zdbh} and cjsj >=#{sTime} and cjsj <= #{eTime} " )
	void delGps(@Param("zdbh") String zdbh, @Param("sTime") Date sTime,@Param("eTime") Date endTime);
}