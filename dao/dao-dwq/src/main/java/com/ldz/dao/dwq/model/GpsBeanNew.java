package com.ldz.dao.dwq.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@Table(name = "CL_GPS_DWQ")
public class GpsBeanNew implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	private String deviceId;
	//gps时间
	private String time;
	//消息类型
	/**
	0 正常定位
	1 休眠通知
	2 SOS 告警
	3 震动告警
	4 摔倒告警
	5 拆卸告警
	6 拔出告警
	7 急加速
	8 急减速
	9 急转弯
	10 急变道
	11 熄火
	12 点火
	 */
	private String xxlx;
	//里程数
	private String lcs;
	//经度.精确到小数点后 6 位，负号表示西半球
	private String jd;
	//纬度.精确到小数点后 6 位，负号表示南半球
	private String wd;
	//速度.单位：公里/小时
	private String sd;
	//方向.取值范围：0-359.9
	private String fx;
	//海拔高度
	private String hb;
	//WiFi定位信息.SSID#MAC#RSSI|SSID#MAC#RSSI|…
	private String wifi;
	//LBS定位信息.LAC#CI#Dbm|LAC#CI#Dbm|…。
	private String lbs;
	// GPS 定 位 个 数(13)
	private String gpsNum;
	// GPS 定位参数[n](逗号分隔）(14)
	private String gpsParams;
	@Transient
	private List<GpsInfo> gpsInfos;
}
