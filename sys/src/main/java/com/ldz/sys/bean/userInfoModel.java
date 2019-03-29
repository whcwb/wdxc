package com.ldz.sys.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class userInfoModel {


	private String  yhid;

	private String xm;

	private String jgdm;

	private String type;

	/**
	 * 用户头像
	 */
	private String hImg;

	/**
	 * 性别
	 */
	private String gender;

	/**
	 * 出生日期
	 */
	private String birthDay;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * e-mail
	 */
	private String eMail;

	/**
	 * 学历
	 */
	private String eduRecoed;

	/**
	 * 驾照照片
	 */
	private String dImg;

	/**
	 * 驾驶证号码
	 */
	private String dCode;

	/**
	 * 驾驶证档案编号
	 */
	private String dNo;

	/**
	 * 驾驶证初领日期
	 */
	private String dDate;


}
