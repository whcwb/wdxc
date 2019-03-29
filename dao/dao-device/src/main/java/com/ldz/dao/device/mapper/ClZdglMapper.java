package com.ldz.dao.device.mapper;

import com.ldz.dao.device.model.ClZdgl;
import com.ldz.util.mapperprovider.InsertListMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

// @CacheNamespace(implementation=MybatisRedisCache.class)
public interface ClZdglMapper extends Mapper<ClZdgl> , InsertListMapper<ClZdgl> {
    @Select("SELECT Z.* FROM CL_ZDGL Z  left join CL_CL C on  Z.ZDBH=C.ZDBH where ( Z.JGDM like '${jgdm}%'   and Z.ZT='00' AND IFNULL(C.CL_ID,'1') ='1') or z.zdbh=#{zdbh} ")
    @Results({
            @Result(property = "zdbh", column = "ZDBH"),
            @Result(property = "xh", column = "XH"),
            @Result(property = "mc", column = "MC"),
            @Result(property = "cs", column = "CS"),
            @Result(property = "zt", column = "ZT"),
            @Result(property = "cjr", column = "CJR"),
            @Result(property = "cjsj", column = "CJSJ"),
            @Result(property = "xgr", column = "XGR"),
            @Result(property = "zdLx",column = "ZD_LX")
    })
    List<ClZdgl> getUnboundList(@Param("jgdm") String jgdm, @Param("zdbh") String zdbh);



    @Select(" select * from CL_ZDGL where sfyy is null")
    List<ClZdgl>  getZDNotYy();
/*    @Insert("<script>" +
            "INSERT ALL INTO CL_ZDGL(ZDBH,MC,XH,PZLMD,SPSCMS,CMD,ZXZT,JSLMD,GPSXT,CJR,CJSJ) values"+
            " <foreach collection='list' item='item' index='index' separator=' INTO CL_ZDGL ' >  " +
            " (#{item.zdbh},#{item.mc},#{item.xh},#{item.pzlmd},#{item.spscms},#{item.cmd},#{item.zxzt},#{item.jslmd},#{item.gpsxt},#{item.cjr},#{item.cjsj}) " +
            " </foreach> " +
            " select 1 from dual "+
            "</script>")
    void saveBatch(@Param("list")List<ClZdgl> list);*/

}
