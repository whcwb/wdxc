package com.ldz.dao.biz.mapper;

import com.ldz.dao.biz.model.ClXc;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ClXcMapper extends Mapper<ClXc> {

    // 删除 某个终端编号某段时间的行程
    @Delete(" delete from CL_XC where CL_ZDBH = #{zdbh} and XC_KSSJ >= #{startTime} and XC_KSSJ <= #{endTime}")
    void delXc(@Param("zdbh")String zdbh, @Param("startTime") String startTime, @Param("endTime") String endTime);
}