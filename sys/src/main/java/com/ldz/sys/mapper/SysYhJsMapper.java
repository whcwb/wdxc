package com.ldz.sys.mapper;

import com.ldz.sys.model.SysYhJs;
import com.ldz.util.cache.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cache.decorators.FifoCache;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@CacheNamespace(implementation=MybatisRedisCache.class, eviction=FifoCache.class)
public interface SysYhJsMapper extends Mapper<SysYhJs> {
    @Select("SELECT JS_ID from SYS_YH_JS where YH_ID = #{yhId}" )
    List<String> findJsIdByYhId(@Param("yhId") String yhId);
}