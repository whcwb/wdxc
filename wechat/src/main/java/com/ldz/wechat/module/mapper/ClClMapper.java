package com.ldz.wechat.module.mapper;

import com.ldz.wechat.module.model.ClCl;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ClClMapper extends Mapper<ClCl> {
	/*
	 * 通过车辆终端id找到对应的电子围栏
	 */
	ClCl seleByZdbh(String deviceId);

	/*
	 * 通过车辆id集合获取车俩信息
	 *
	 */
	List<ClCl> getAllClInfo(List<String> list);

	ClCl seleClInfoByZdbh(String zdbh);

}
