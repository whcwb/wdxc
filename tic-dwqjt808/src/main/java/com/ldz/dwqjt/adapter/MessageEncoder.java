package com.ldz.dwqjt.adapter;

import com.ldz.dwqjt.jt808.util.BitOperator;
import com.ldz.dwqjt.jt808.util.Consts;
import com.ldz.dwqjt.jt808.util.JT808ProtocolUtils;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.jt808.vo.req.TerminalRegisterMsg;
import com.ldz.dwqjt.jt808.vo.resp.DeviceParamMsg;
import com.ldz.dwqjt.jt808.vo.resp.ServerCommonRespMsgBody;
import com.ldz.dwqjt.jt808.vo.resp.TerminalRegisterMsgRespBody;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Arrays;

/**
 * 数据协议编码，回复指令
 */
public class MessageEncoder extends MessageToByteEncoder<PackageData> {

	private BitOperator bitOperator;
	private JT808ProtocolUtils jt808ProtocolUtils;

	public MessageEncoder() {
		this.bitOperator = new BitOperator();
		this.jt808ProtocolUtils = new JT808ProtocolUtils();
	}

	/**
	 * 执行下发命令必须一条条执行，同一个通道不能同时下发多条指令
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, PackageData msg, ByteBuf out) throws Exception {
		if(null == msg){
            throw new Exception("发送数据不能为空");
        }


	}

	public byte[] encode4TerminalRegisterResp(TerminalRegisterMsg req, TerminalRegisterMsgRespBody respMsgBody,
			  int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		// 鉴权码(STRING) 只有在成功后才有该字段
		if (respMsgBody.getReplyCode() == TerminalRegisterMsgRespBody.success) {
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
		bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()), // 流水号(2)
		new byte[] { respMsgBody.getReplyCode() }, // 结果
		respMsgBody.getReplyToken().getBytes(Consts.string_charset)// 鉴权码(STRING)
		));
		} else {
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
		bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()), // 流水号(2)
		new byte[] { respMsgBody.getReplyCode() }// 错误代码
		));
		}

		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
		Consts.cmd_terminal_register_resp, msgBody, msgBodyProps, flowId);
		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);

		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);
		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}

		// public byte[] encode4ServerCommonRespMsg(TerminalAuthenticationMsg req,
		// ServerCommonRespMsgBody respMsgBody, int flowId) throws Exception {
	public byte[] encode4ServerCommonRespMsg(PackageData req, ServerCommonRespMsgBody respMsgBody, int flowId)
		throws Exception {
		byte[] msgBody = this.bitOperator.concatAll(Arrays.asList(//
		bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()), // 应答流水号
		bitOperator.integerTo2Bytes(respMsgBody.getReplyId()), // 应答ID,对应的终端消息的ID
		new byte[] { respMsgBody.getReplyCode() }// 结果
		));
		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
		Consts.cmd_common_resp, msgBody, msgBodyProps, flowId);
		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);
		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}

	private byte[] doEncode(byte[] headerAndBody, int checkSum) throws Exception {
		byte[] noEscapedBytes = this.bitOperator.concatAll(Arrays.asList(//
		new byte[] { Consts.pkg_delimiter }, // 0x7e
		headerAndBody, // 消息头+ 消息体
		bitOperator.integerTo1Bytes(checkSum), // 校验码
		new byte[] { Consts.pkg_delimiter }// 0x7e
		));
		// 转义
		return jt808ProtocolUtils.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 2);
	}

	public byte[] encode4DeviceParamRespMsg(PackageData req, DeviceParamMsg respMsgBody, int flowId) throws Exception{
		byte[] msgBody = this.bitOperator.concatAll(Arrays.asList(
				bitOperator.integerTo1Bytes(respMsgBody.getParamNum()),
				bitOperator.integerTo4Bytes(respMsgBody.getIpParams().getMsgId()),
				bitOperator.integerTo1Bytes(respMsgBody.getIpParams().getParamLen()),
				respMsgBody.getIpParams().getParam().getBytes(Consts.string_charset),
				bitOperator.integerTo4Bytes(respMsgBody.getPortParams().getMsgId()),
				bitOperator.integerTo1Bytes(respMsgBody.getPortParams().getParamLen()),
				bitOperator.integerTo4Bytes(respMsgBody.getPortParams().getParam())
				));
		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
				Consts.cmd_terminal_param_settings, msgBody, msgBodyProps, flowId);
		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);
		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}
	public byte[] encode4QueryDeviceParamRespMsg(PackageData req, int flowId) throws Exception{
//		String s = "020000001700000018";
		byte[] msgBody = this.bitOperator.concatAll(Arrays.asList(
				bitOperator.integerTo1Bytes(0x02),
				bitOperator.integerTo4Bytes(0x0017),
				bitOperator.integerTo4Bytes(0x0018)
		));
		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(req.getMsgHeader().getTerminalPhone(),
				Consts.cmd_terminal_param_query, msgBody, msgBodyProps, flowId);
		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);
		// 连接并且转义
		return this.doEncode(headerAndBody, checkSum);
	}

}
