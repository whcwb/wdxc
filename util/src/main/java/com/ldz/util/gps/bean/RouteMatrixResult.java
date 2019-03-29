package com.ldz.util.gps.bean;

import java.io.Serializable;
import java.util.Map;

public class RouteMatrixResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 220464249630345560L;
	//线路距离的文本描述.数值的单位为米。若没有计算结果，值为0
	private Map<String, String> distance;
	//路线耗时的文本描述.数值的单位为秒。若没有计算结果，值为0
	private Map<String, String> duration;
	
	public Map<String, String> getDistance() {
		return distance;
	}
	public void setDistance(Map<String, String> distance) {
		this.distance = distance;
	}
	public Map<String, String> getDuration() {
		return duration;
	}
	public void setDuration(Map<String, String> duration) {
		this.duration = duration;
	}
}