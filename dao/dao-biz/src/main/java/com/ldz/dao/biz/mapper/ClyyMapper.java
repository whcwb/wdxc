package com.ldz.dao.biz.mapper;

import com.ldz.dao.biz.model.Clyy;
import com.ldz.util.mapperprovider.InsertListMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ClyyMapper extends Mapper<Clyy>,InsertListMapper<Clyy> {
    @Delete("delete from CL_YY where ZDBH = #{ZDBH} and LOCTIME>=#{startTime} and LOCTIME<=#{endTime}")
    void removeLists(@Param("ZDBH") String zdbh, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
