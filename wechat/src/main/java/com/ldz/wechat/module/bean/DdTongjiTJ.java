package com.ldz.wechat.module.bean;

import lombok.Getter;
import lombok.Setter;

@Getter  
@Setter  //用于订单统计查询条件模型
public class DdTongjiTJ {
   //机构代码
	private String  jgdm;
	private String  jgdmlike;
	//开始时间
	private String  kssj;
	//结束时间
	private String jssj;
	//司机id
	private String sj;
	private String sjxm;
}
