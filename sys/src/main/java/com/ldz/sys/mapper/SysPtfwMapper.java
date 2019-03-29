package com.ldz.sys.mapper;

import com.ldz.sys.model.SysFw;
import com.ldz.util.cache.MybatisRedisCache;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.cache.decorators.FifoCache;
import tk.mybatis.mapper.common.Mapper;

public interface SysPtfwMapper extends Mapper<SysFw> {
}