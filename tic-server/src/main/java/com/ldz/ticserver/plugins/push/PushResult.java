package com.ldz.ticserver.plugins.push;

public class PushResult {
	public static int SUCCESS = 2;// 成功
	public static int ERROR = 3;// 错误
	public static int NULLCODE = 5;// 未知

	private int code = SUCCESS;
	private Object resultData; // 推送返回数据
	private String resultMsg;// 推送返回消息
	private Object pushData;// 推送数据

	public Object getPushData() {
		return pushData;
	}

	public void setPushData(Object pushData) {
		this.pushData = pushData;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getResultData() {
		return resultData;
	}

	public void setResultData(Object resultData) {
		this.resultData = resultData;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
