package com.ldz.dao.biz.mapper;

import java.util.List;

import com.ldz.dao.biz.bean.PbClXlmodel;
import com.ldz.dao.biz.bean.PbInfo;
import com.ldz.dao.biz.bean.XbXlPb;

import com.ldz.dao.biz.bean.clpbInfo;
import tk.mybatis.mapper.common.Mapper;

public interface PbInfoMapper extends Mapper<PbInfo> {
	/*
	 * 通过日期,线路类型,车辆车型获取所有排班的信息
	 */
	
	List<PbInfo> selectBydate(PbClXlmodel pnbclxlmodel);
	/*
	 * 通过日期,线路类型获取所有线路的排班情况
	 */
	List<XbXlPb> selectXbPb(PbClXlmodel pnbclxlmodel);
	
	List<PbInfo> findXlCl(clpbInfo clpbInfo);
	
	List<PbInfo> bancheTj(PbClXlmodel pnbclxlmodel);
}