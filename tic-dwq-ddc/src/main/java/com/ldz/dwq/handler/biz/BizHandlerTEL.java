package com.ldz.dwq.handler.biz;

import org.springframework.stereotype.Component;

import com.ldz.dwq.common.bean.MessageBean;

import io.netty.channel.ChannelHandlerContext;

/**
 * 终端登录请求命令解析
 * @author Lee
 *
 */
@Component
public class BizHandlerTEL extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		
		iotServer.online(ctx, messageBean.getImei());
		//读取数据成功后，向终端响应结果
		MessageBean sendData = new MessageBean();
		sendData.setMid(messageBean.getMid());
		sendData.setCommand(messageBean.getCommand());
		sendData.setImei(messageBean.getImei());
		
		iotServer.sendMsg(sendData);
	}
}
