package com.ldz.util.mapperprovider;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@tk.mybatis.mapper.annotation.RegisterMapper
public interface InsertListMapper<T> {

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口限制实体包含`id`属性并且必须为自增列
     *
     * @param recordList
     * @return
     */
    @Options(useGeneratedKeys = false, keyProperty = "id")
    @InsertProvider(type = InsertListProvider.class, method = "dynamicSQL")
    int insertList(List<T> recordList);
}
