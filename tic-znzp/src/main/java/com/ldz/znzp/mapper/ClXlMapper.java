package com.ldz.znzp.mapper;

import com.ldz.util.cache.MybatisRedisCache;
import com.ldz.znzp.model.ClXl;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.cache.decorators.FifoCache;
import tk.mybatis.mapper.common.Mapper;

@CacheNamespace(implementation=MybatisRedisCache.class, eviction=FifoCache.class)
public interface ClXlMapper extends Mapper<ClXl> {

}
