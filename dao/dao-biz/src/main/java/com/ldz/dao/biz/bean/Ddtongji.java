package com.ldz.dao.biz.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ddtongji {
    //订单总数
	private  Integer ddzsCount;
	//已审核总数
	private  Integer yshCount;
	//未审核总数
	private  Integer wshCount;
	//已取消总数
	private  Integer yqxCount;
	//待司机确认总数
	private  Integer dsjCount;
	//待队长确认总数
	private  Integer ddzCount;
	//财务确认总数
	private  Integer cwCount;
	//机构代码
	private String jgdm;
	//机构名称
	private String jgmc;

	private String sj;

	private String sjname;

	private String fkze;
}
