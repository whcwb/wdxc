package com.ldz.dao.obd.mapper;

import com.ldz.dao.obd.model.GpsObdMessageBean;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * auther chenwei
 * create at 2018/11/13
 */
public interface GpsObdMapper extends Mapper<GpsObdMessageBean>, InsertListMapper<GpsObdMessageBean> {
}
