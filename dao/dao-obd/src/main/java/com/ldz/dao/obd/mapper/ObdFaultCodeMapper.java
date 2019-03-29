package com.ldz.dao.obd.mapper;

import com.ldz.dao.obd.model.ObdFaultCodeBean;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

public interface ObdFaultCodeMapper extends Mapper<ObdFaultCodeBean> , InsertListMapper<ObdFaultCodeBean> {
}
