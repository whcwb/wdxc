package com.ldz.util.gps.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Baidu算路服务返回结果bean
 * @author Lee
 *
 */
public class RouteMatrixBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5399907718030128789L;

	/**
	 * 状态码
	 * 0：成功
	 * 1：服务器内部错误
	 * 2：参数错误
	 * 其他错误
	 */
	private Integer status;
	//返回信息.对status的中文描述
	private String message;
	//返回的结果
	private List<RouteMatrixResult> result;
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<RouteMatrixResult> getResult() {
		return result;
	}
	
	public void setResult(List<RouteMatrixResult> result) {
		this.result = result;
	}
}
