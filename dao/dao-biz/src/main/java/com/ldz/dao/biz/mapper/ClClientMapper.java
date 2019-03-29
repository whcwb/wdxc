package com.ldz.dao.biz.mapper;

import com.ldz.dao.biz.model.ClClient;
import com.ldz.util.cache.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.cache.decorators.FifoCache;
import tk.mybatis.mapper.common.Mapper;

@CacheNamespace(implementation= MybatisRedisCache.class, eviction= FifoCache.class)
public interface ClClientMapper extends Mapper<ClClient> {
}
