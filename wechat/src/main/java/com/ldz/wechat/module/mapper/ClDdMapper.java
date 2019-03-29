package com.ldz.wechat.module.mapper;

import com.ldz.wechat.module.bean.DdTongjiTJ;
import com.ldz.wechat.module.model.ClDd;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ClDdMapper extends Mapper<ClDd> {
	List<ClDd> DdTongji(DdTongjiTJ dd);


    @Select({"<script> " +
        " INSERT INTO CL_DDLSB (ID, SJQRSJ, HCDZ, MDD, CPH, SJ, SJXM, ZJ, SC, DJ, LC, SCF, LCF, CK, CKZW, CKLXDH, ZWS, CKLX, YYSJ, DDZT, FKZT, FKFS, FKSJ, PJDJ, PJNR, CJSJ, CJR, XGSJ, XGR, JGDM, JGMC, CL_ID, GLF, SY, CLLX, WF, DZQRSJ, SHSJ, DZXM, FKBZ, DD_ID, XGCS, DDXGSJ, DDCJSJ, DDXGR)  " +
        " SELECT sys_guid(),SJQRSJ, HCDZ, MDD, CPH, SJ, SJXM, ZJ, SC, DJ, LC, SCF, LCF, CK, CKZW, CKLXDH, ZWS, CKLX, YYSJ, DDZT, FKZT, FKFS, FKSJ, PJDJ, PJNR, CJSJ, CJR, XGSJ, XGR, JGDM, JGMC, CL_ID, GLF, SY, CLLX, WF, DZQRSJ, SHSJ, DZXM, FKBZ, ID, (SELECT NVL(MAX(XGCS),0)+1 FROM CL_DDLSB B WHERE B.DD_ID=#{orderid}) XGCS, sysdate DDXGSJ, sysdate DDCJSJ, #{userid} AS DDXGR " +
        " FROM CL_DD  WHERE ID=#{orderid} " +
        " </script>"})
    void insertCopyOrder(@Param("orderid") String id, @Param("userid") String userId);

    @Select({
            "<script> " +
                    " SELECT ZWS,COUNT(1) COU FROM (SELECT D.* FROM CL_DD D WHERE  DDZT ='11' AND  TO_CHAR(D.YYSJ,'YYYY-MM-DD')>=TO_CHAR(SYSDATE,'YYYY-MM-DD')  " +
                    "  AND D.CLLX IN " +
                    " <foreach collection='list' item='item' open='(' close=')' separator=','> " +
                    "  #{item} " +
                    " </foreach> " +
                    "  ) " +
                    " group by ZWS " +
            " </script>"})
    List<Map<String,Object>> selectVeryDayOrder(List<String> list);

    @Select({
            "<script> " +
                    " SELECT COUNT(1) WCORDER  FROM (SELECT D.* FROM CL_DD D WHERE  DDZT ='13' AND  TO_CHAR(D.YYSJ,'YYYY-MM-DD')>=TO_CHAR(SYSDATE,'YYYY-MM-DD')  " +
                    "  AND D.CLLX IN " +
                    " <foreach collection='list' item='item' open='(' close=')' separator=','> " +
                    "  #{item} " +
                    " </foreach> " +
                    "  ) " +
                    " </script>"
    })
    Map<String,Object> selectVeryDayOrderCount(List<String> list);
}
