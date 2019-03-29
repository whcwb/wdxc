package com.ldz.dwq.handler.biz;

import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.dwq.bean.DeviceBean;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.handler.ServerChannelInitializer;
import com.ldz.dwq.server.IotServer;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 终端上报设备实时温度数据，平台下发 温度预警值。
 */
@Component
@Slf4j
@SuppressWarnings("static-access")
public class BizHandlerC23 extends BizBaseHandler {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		String[] dataArray = messageBean.getData().split(",");
		String imei = dataArray[2];
		String tempurature = dataArray[3];

		log.info("imei: "+imei);
		log.info("tempurature: "+tempurature);

		//读取数据成功后，向终端响应结果
		String cid = ctx.channel().id().asShortText();
		String onlineKey = messageBean.getImei()+"-"+cid+"-"+ IotServer.ONLINE_KEY;
		//更新终端设备功能
		DeviceBean device = (DeviceBean)redisDao.boundValueOps(onlineKey).get();
		if (device != null){
			MessageBean sendData = new MessageBean();
			sendData.setCommand("S23");
			sendData.setImei(messageBean.getImei());
			sendData.setMid("23");
			//预警时间
			String data = "85";
			sendData.setData(data);
			iotServer.sendMsg(sendData);
		}
	}
}
