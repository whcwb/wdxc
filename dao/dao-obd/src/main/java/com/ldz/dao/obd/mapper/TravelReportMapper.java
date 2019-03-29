package com.ldz.dao.obd.mapper;

import com.ldz.dao.obd.model.ObdTravelItineraryBean;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * auther chenwei
 * create at 2018/11/9
 */
public interface TravelReportMapper extends Mapper<ObdTravelItineraryBean>, InsertListMapper<ObdTravelItineraryBean> {
}
