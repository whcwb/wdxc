package com.ldz.wechat.module.mapper;

import com.ldz.wechat.module.bean.DdClModel;
import com.ldz.wechat.module.model.ClXl;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ClXlMapper extends Mapper<ClXl> {

    @Select("SELECT Z.FW AS FW,Z.ID AS ID,Z.MC AS MC,(SELECT COUNT(1) FROM CL_CLYXJL JL WHERE JL.ZT <> 'off' AND  JL.XL_ID = #{id} AND JL.ZD_ID=Z.ID AND TO_CHAR(JL.CJSJ,'yyyy-MM-dd') >= TO_CHAR(sysdate,'yyyy-MM-dd') ) AS CLSL ,Z.JD AS ZD_JD,Z.WD AS ZD_WD FROM CL_XLZD T,CL_ZD Z WHERE T.ZD_ID=Z.ID AND T.ZT='00' AND Z.ZT ='00' AND  T.XL_ID= #{id} ORDER BY T.XH ASC")
    @Results({
            @Result(property = "zdId", column = "ID"),
            @Result(property = "zdName", column = "MC"),
            @Result(property = "vehicleCount", column = "CLSL"),
            @Result(property = "jd", column = "ZD_JD"),
            @Result(property = "wd", column = "ZD_WD"),
            @Result(property = "vehicleScope", column = "FW")
    })
    List<DdClModel> getBySiteVehicleList(@Param("id") String id);
}
