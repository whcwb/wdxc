package com.ldz.dwq.handler.biz;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import com.ldz.dwq.common.bean.MessageBean;

import io.netty.channel.ChannelHandlerContext;

/**
 * 终端登录请求命令解析
 * @author Lee
 *
 */
@Component
public class BizHandlerC1 extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;

		iotServer.online(ctx, messageBean.getImei());
		//读取数据成功后，向终端响应结果
		MessageBean sendData = new MessageBean();
		sendData.setCommand("S1");
		sendData.setImei(messageBean.getImei());
		//UTC时间,登录答复,时区。默认使用0时区，设备是使用的0时区
		String data = messageBean.getMid() + "," + DateTime.now().withZone(DateTimeZone.UTC).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss") + ",1,0";
		sendData.setData(data);

		iotServer.sendMsg(sendData);
	}
}
