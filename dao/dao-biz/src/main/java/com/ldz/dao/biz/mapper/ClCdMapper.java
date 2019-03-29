package com.ldz.dao.biz.mapper;

import com.ldz.dao.biz.model.ClCd;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface ClCdMapper extends Mapper<ClCd> {
    /**
     * 撤销车辆的车队编号
     * @param clId
     */
    @Update("<script>" +
            " UPDATE CL_CL SET CDBH = NULL " +
            " where CDBH =#{clid} " +
            "</script>")
    void cancelClMotorcade(@Param("clid")String clId);
    /**
     * 撤销驾驶员的车队编号
     * @param clId
     */
    @Update("<script>" +
            " UPDATE CL_JSY SET CDBH = NULL " +
            " where CDBH =#{clid} " +
            "</script>")
    void cancelJsyMotorcade(@Param("clid")String clId);

}
