package com.ldz.dao.biz.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用于处理车辆/校巴 排班管理 条件查询
 */
@Getter
@Setter
public class PbClXlmodel {


	private String clcx;

	private String lulx;

	private Date date2;
	
	private String kssj;
	
	private String jssj;
	
	private String jgdm;
	
	private String sjxm;
	
}
