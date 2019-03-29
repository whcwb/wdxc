package com.ldz.dao.biz.bean;

import java.util.List;

import com.ldz.dao.biz.model.ClCl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  用于编辑校巴/车辆 线路排班管理模型
 */
@Getter
@Setter
@ToString
public class XbXlPb {

	/**
	 * 线路id
	 */
	private String id;

	/**
	 * 线路名称
	 */
	private String xlmc;
	
	/**
	 * 线路运行开始时间
	 */
	private String yxkssj;
	
	/**
	 * 线路运行结束时间
	 */
	private String yxjssj;
	/**
	 * 车辆id集合
	 */
	private String clidlist;
	/**
	 * 车辆对象集合
	 */
	private List<ClCl> clList;
	/**
	 * 司机姓名
	 */
	private String sjxm;
	
}
