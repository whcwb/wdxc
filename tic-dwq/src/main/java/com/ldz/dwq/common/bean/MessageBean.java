package com.ldz.dwq.common.bean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据协议bean结构
 * 报文协议，ascii字节
 * 协议以”[”为数据包开头，以”]”为数据包结尾
 * 上行协议类型以 C（Client）开头，下行协议类型以 S（Server）开头
 */
@Getter
@Setter
@ToString
public class MessageBean implements Serializable {
											 // 内容
	public static final String start = "["; // 起始位
	public static final String end   = "]"; // 结束位
	private String command;					 // 命令位
	private String imei = "";				// 终端imei号
	private String mid;					 	// 消息ID
	private String data;					//接收到的数据内容，根据具体命令进行具体解析

	private static final long serialVersionUID = -5292455834862999992L;

	/**
	 * 根据数据内容拼接指令内容
	 * @return
	 */
	public String getCmdData(){
		//指令码不能为空
		if (StringUtils.isEmpty(command)){
			return null;
		}

		//最终的发送指令数据
		if (StringUtils.isEmpty(mid)){
			return start + command + "," + data + end;
		}else{
			return start + command + "," + mid + "," + data + end;
		}
	}
}
