package com.ldz.dwq.handler.biz;

import java.util.concurrent.TimeUnit;

import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ldz.dwq.bean.DeviceBean;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.handler.ServerChannelInitializer;
import com.ldz.dwq.server.IotServer;

import io.netty.channel.ChannelHandlerContext;

/**
 * 终端信息同步命令解析
 * @author Lee
 *
 */
@Component
public class BizHandlerC2 extends BizBaseHandler {
	
	@Autowired
	private RedisTemplateUtil redisDao;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		
		String[] dataArray = messageBean.getData().split(",");
		//设备功能。需转换为二进制（从低到高）
		/**
			0 WIFI 定位 0 为缺省，1 为支持
			1 震动功能 0 为缺省，1 为支持
			2 计步功能 0 为缺省，1 为支持
			3 马达功能 0 为缺省，1 为支持
			4 蜂鸣器功能 0 为缺省，1 为支持
			5 喇叭功能 0 为缺省，1 为支持
		 */
		String sbgn = dataArray[dataArray.length - 1];
		String bSbgn = Integer.toBinaryString(Integer.parseInt(sbgn));
		bSbgn = StringUtils.leftPad(bSbgn, 6, "0");
		bSbgn = StringUtils.reverse(bSbgn);
		
		//读取数据成功后，向终端响应结果
		String cid = ctx.channel().id().asShortText();
		String onlineKey = messageBean.getImei()+"-"+cid+"-"+IotServer.ONLINE_KEY;
		//更新终端设备功能
		DeviceBean device = (DeviceBean)redisDao.boundValueOps(onlineKey).get();
		if (device != null){
			device.setTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
			device.setSbgn(bSbgn);
			redisDao.boundValueOps(onlineKey).set(device);
			redisDao.boundValueOps(onlineKey).expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);
			
			sendCommonMsg(messageBean);
		}
	}
}
