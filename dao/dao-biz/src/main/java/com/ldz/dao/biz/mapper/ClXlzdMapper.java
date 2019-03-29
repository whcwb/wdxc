package com.ldz.dao.biz.mapper;

import com.ldz.dao.biz.model.ClXlzd;
import com.ldz.util.cache.MybatisRedisCache;
import com.ldz.util.mapperprovider.InsertListMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.cache.decorators.FifoCache;
import tk.mybatis.mapper.common.Mapper;

@CacheNamespace(implementation=MybatisRedisCache.class, eviction=FifoCache.class)
public interface ClXlzdMapper extends Mapper<ClXlzd> , InsertListMapper<ClXlzd> { //OracleInsertListMapper
}
