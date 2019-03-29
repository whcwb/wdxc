package com.ldz.dwq.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 定位器对象
 * @author Lee
 *
 */
@Getter
@Setter
@ToString
public class DeviceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//设备编号
	private String imei;
	//设备上线时间
	private String time;
	//设备功能。二进制值
	/**
		0 WIFI 定位 0 为缺省，1 为支持
		1 震动功能 0 为缺省，1 为支持
		2 计步功能 0 为缺省，1 为支持
		3 马达功能 0 为缺省，1 为支持
		4 蜂鸣器功能 0 为缺省，1 为支持
		5 喇叭功能 0 为缺省，1 为支持
	 */
	private String sbgn;
}
