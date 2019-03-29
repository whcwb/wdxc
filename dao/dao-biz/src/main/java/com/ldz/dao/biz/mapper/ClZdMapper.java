package com.ldz.dao.biz.mapper;

import com.ldz.dao.biz.bean.DdClModel;
import com.ldz.dao.biz.model.ClZd;
import com.ldz.util.cache.MybatisRedisCache;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cache.decorators.FifoCache;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@CacheNamespace(implementation=MybatisRedisCache.class, eviction=FifoCache.class)
public interface ClZdMapper extends Mapper<ClZd> {
    /**
     *
     * @param id
     * @return
     */
    @Select("SELECT Z.FW AS FW,Z.ID AS ID,Z.MC AS MC,(SELECT COUNT(1) FROM CL_CLYXJL JL WHERE JL.ZT <> 'off' AND  JL.XL_ID = #{id} AND JL.ZD_ID=Z.ID AND TO_CHAR(JL.CJSJ,'yyyy-MM-dd') >= TO_CHAR(sysdate,'yyyy-MM-dd') ) AS CLSL  FROM CL_XLZD T,CL_ZD Z WHERE T.ZD_ID=Z.ID AND T.ZT='00' AND Z.ZT ='00' AND  T.XL_ID= #{id} ORDER BY T.XH ASC")
    @Results({
            @Result(property = "zdId", column = "ID"),
            @Result(property = "zdName", column = "MC"),
            @Result(property = "vehicleCount", column = "CLSL"),
            @Result(property = "vehicleScope", column = "FW")
    })
    List<DdClModel> getBySiteVehicleList(@Param("id") String id);
    
    
    //批量获取站点
    List<ClZd> getAllClzd(@Param("clzds") List<String> clzds);




}