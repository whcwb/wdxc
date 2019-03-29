package com.ldz.dao.dwq.mapper;

import com.ldz.dao.dwq.model.TravelData;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface TravelDataMapper extends Mapper<TravelData>, InsertListMapper<TravelData> {
}
