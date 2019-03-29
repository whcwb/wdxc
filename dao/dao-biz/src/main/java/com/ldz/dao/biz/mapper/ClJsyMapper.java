package com.ldz.dao.biz.mapper;

import com.ldz.dao.biz.bean.ClJsyModel;
import com.ldz.dao.biz.model.ClJsy;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ClJsyMapper extends Mapper<ClJsy> {
    // 订单状态  10-订单创建；11-订单确认(待派单)；12-订单驳回；13-已派单；20-司机确认(行程结束)；30-队长确认; 40-财务已收
    @Select("<script> SELECT J.ZT,J.SFZHM,J.XM,J.CDBH,J.SJH,L.CX,L.CPH,L.ZKL " +
            ",(SELECT COUNT(1) FROM CL_DD D WHERE DATE_FORMAT(D.YYSJ,'%Y-%m-%d')=DATE_FORMAT(SYSDATE(),'%Y-%m-%d') AND J.SFZHM=D.SJ AND D.DDZT='20') ENDORDERCOUNT " +
            ",(SELECT COUNT(1) FROM CL_DD D WHERE DATE_FORMAT(D.YYSJ,'%Y-%m-%d')=DATE_FORMAT(SYSDATE(),'%Y-%m-%d') AND J.SFZHM=D.SJ AND D.DDZT='13') STARTORDERCOUNT " +
            "   FROM CL_JSY J,CL_CL L WHERE J.SFZHM=L.SJ_ID AND L.ZT='00'" +
            " <if test='zkl != null'> " +
            "  AND L.ZKL >= #{zkl}" +
            " </if>  " +
            " AND J.SFZHM NOT IN (SELECT SJ FROM CL_PB WHERE DATE_FORMAT(PBSJ,'%Y-%m-%d')=DATE_FORMAT(SYSDATE(),'%Y-%m-%d')) " +
            " <if test='list != null'> " +
            "  AND L.CX IN " +
            " <foreach collection='list' item='item' open='(' close=')' separator=','> " +
            "  #{item} " +
            "</foreach> " +
            " </if>  " +
            " <if test='xm != null'> " +
            " and J.xm=like '%'#{xm}'%' " +
            " </if>  " +
            "  " +
            " </script>  ")
    @Results({
            @Result(property = "sfzhm", column = "SFZHM"),
            @Result(property = "xm", column = "XM"),
            @Result(property = "cdbh", column = "CDBH"),
            @Result(property = "sjh", column = "SJH"),

            @Result(property = "cx", column = "CX"),
            @Result(property = "zt", column = "ZT"),
            @Result(property = "cph", column = "CPH"),
            @Result(property = "zkl", column = "ZKL"),
            @Result(property = "endOrderCount", column = "ENDORDERCOUNT"),
            @Result(property = "startOrderCount", column = "STARTORDERCOUNT"),
    })
    List<ClJsyModel> getDispatchDriver(@Param("xm") String xm, @Param("list") List<String> li, @Param("zkl") String zkl);
}
