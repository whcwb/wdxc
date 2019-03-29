package com.ldz.dao.biz.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class gpsSJInfo {
    //开始时间
	private String startTime;
	// 结束时间
	private String endTime;
	//终端编号
	private String zdbh;
	//点火  50
	private String ignition;
	//熄火 60
	private String brennschluss;
}
