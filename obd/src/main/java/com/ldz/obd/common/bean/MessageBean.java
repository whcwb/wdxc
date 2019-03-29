package com.ldz.obd.common.bean;

import java.io.Serializable;
import java.util.Arrays;

import io.netty.buffer.ByteBufUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.DatatypeConverter;

/**
 * 数据协议bean结构
 * 报文协议，ASCII格式
 * 	内容		字节数		说明
 开始符	        1		固定为 0x28,即 ASCII 的‘(’符
 产品id	        6		设备的ID号,固定为6字节长度(即12个ASCII字符， BCD码表示，
						 2 位表示产品编号， 两位表示生产日期的年， 2013 年即用 13 表示，
						 4 位表示月日，还剩 4 位表示该日期生产的编号，从 0000-9999。
						 当然该 ID 也可以根据客户要求设置成其他形式，比如直接采用
						 SIM 卡号，但是长度必须不超过 12 位，且必须都是数

 命令字	        2		一个字节命令类型(表一)，一个字节表示命令编号，具体参见协议
 指令长度	    2		命令内容的长度(转义前
 命令内容	    N		1：下行指令第一个字节表示操作方式(设置为 0x01,查询为 0x00),
						 第二个字节开始为不定字节的参数，查询指令一般不带参数。
						 2：上行指令只包括不定字节的参数
 校验和	        1		从产品 ID 号到命令内容的所有字节的异或和(转义前)
 结束符	        1		固定为 0x29,即 ASCII 的”)”符.
 */
@Getter
@Setter
public class MessageBean implements Serializable {
											 // 内容		字节数	值范围		说明
	public static final String head = "28"; // 起始位	1		[		1帧数据的起始位
	public static final String tail = "29"; // 结束位	1		]		1帧数据的结束位
	private String deviceId;				// 设备id号
	private String commandType;					 // 命令位	2		[0-65535]	不同的命令都有对应的命令位
	private int contentLength;
	private String content = "";					 // 参数位	N		[0-255]		数据参数，N的范围不定[0-65527]
	private String checksum;					 // 检验位	1					本命令的校验位
	private String sum;
	private byte[] bytes;
	private byte[] bodyBytes;
	private static final long serialVersionUID = -5292455834862999992L;

	private String message;

	public MessageBean(){

	}
	public MessageBean(String deviceId,String commandType,String content){
		this.deviceId = deviceId;
		this.commandType = commandType;
		this.content = content;
		if (this.deviceId.length() > 12){
			int l = this.deviceId.length();
			this.deviceId = this.deviceId.substring(l - 12,l);
		}
	}
	public MessageBean(byte[] bytes){
		if (bytes.length < 13){
			return;
		}
		this.bytes = bytes;
		this.message = DatatypeConverter.printHexBinary(bytes);
		int contentLengthStart = 18;
		int contentLengthEnd = 22;
		int commandTypeStart = 14;
		this.contentLength = message.length() - 26;// 整个消息的长度减去头和尾的固定长度，就是内容长度
		this.deviceId = message.substring(2,commandTypeStart);
		this.commandType = message.substring(commandTypeStart,contentLengthStart);
//		this.contentLength = Integer.parseInt(message.substring(contentLengthStart,contentLengthEnd),16);
		int contentEnd = contentLengthEnd + this.contentLength;
		this.content = message.substring(contentLengthEnd,contentEnd);
		this.checksum = message.substring(contentEnd,contentEnd +2);
		this.sum = ""+getXor(bytes);
	}

	public static void main(String[] args) {
		System.out.println(Integer.parseInt("40",16));
	}

	/**
	 * 逐字节做异或运算
	 * @param datas
	 * @return
	 */
	public byte getXor(byte[] datas){
	    byte temp=datas[0];
	    for (int i = 1; i <datas.length; i++) {
	        temp ^=datas[i];
	    }

	    return temp;
	}


	public String getMessage(){
		String m = head + StringUtils.leftPad(this.deviceId,12,"0") + this.commandType + StringUtils.leftPad(Integer.toHexString(this.content.length()),4,"0") + this.content;
		byte checksum = getXor(m.getBytes());
		m += checksum;
		m = m.toUpperCase();
		return m + tail;
	}

	public byte[] getBodyBytes(){
		byte[] bodyBytes = new byte[this.bytes.length - 13];
		for (int i = 11 ;i < this.bytes.length - 2;i++){
			bodyBytes[i - 11] = this.bytes[i];
		}
		return bodyBytes;
	}

	@Override
	public String toString() {
		return "MessageBean{" +
				"deviceId='" + deviceId + '\'' +
				", commandType='" + commandType + '\'' +
				", contentLength=" + contentLength +
				", content='" + content + '\'' +
				", checksum='" + checksum + '\'' +
				", sum='" + sum + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
