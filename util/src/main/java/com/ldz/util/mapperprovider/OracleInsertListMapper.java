package com.ldz.util.mapperprovider;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

@tk.mybatis.mapper.annotation.RegisterMapper
public interface OracleInsertListMapper<T> {
	
	@Options(useGeneratedKeys = false, keyProperty = "id")
	@InsertProvider(type = OracleInsertListProvider.class, method = "dynamicSQL")
    int insertList(List<T> record);
}