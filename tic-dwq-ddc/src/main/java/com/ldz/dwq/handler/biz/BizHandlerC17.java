package com.ldz.dwq.handler.biz;

import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.ldz.dwq.bean.DeviceBean;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.handler.ServerChannelInitializer;
import com.ldz.dwq.server.IotServer;

import io.netty.channel.ChannelHandlerContext;

/**
 * 终端上报进入休眠模式请求命令解析
 * @author Lee
 *
 */
@Component
public class BizHandlerC17 extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		
		//读取数据成功后，向终端响应结果
		String cid = ctx.channel().id().asShortText();
		String onlineKey = messageBean.getImei()+"-"+cid+"-"+IotServer.ONLINE_KEY;
		//更新终端设备功能
		DeviceBean device = (DeviceBean)redisDao.boundValueOps(onlineKey).get();
		if (device != null){
			device.setTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
			redisDao.boundValueOps(onlineKey).set(device);
			redisDao.boundValueOps(onlineKey).expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);
			
			MessageBean sendData = new MessageBean();
			sendData.setCommand("S3");
			sendData.setImei(messageBean.getImei());
			//UTC时间,登录答复,时区
			String data = messageBean.getMid();
			sendData.setData(data);
			
			iotServer.sendMsg(sendData);
		}
	}
}
