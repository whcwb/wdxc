package com.ldz.dao.dwq.mapper;

import com.ldz.dao.dwq.model.GpsBean;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface DwqGpsMapper extends Mapper<GpsBean>, InsertListMapper<GpsBean> {
}
