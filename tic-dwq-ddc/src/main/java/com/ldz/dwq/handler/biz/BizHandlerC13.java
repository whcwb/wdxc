package com.ldz.dwq.handler.biz;

import com.ldz.dao.dwq.model.GpsBean;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.util.bean.RequestCommonParamsDto;

import io.netty.channel.ChannelHandlerContext;

/**
 * 终端 GPS 定位数据补充上报命令解析
 * @author Lee
 *
 */
@Component
@SuppressWarnings("static-access")
public class BizHandlerC13 extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		
		String[] dataArray = messageBean.getData().split(",");
		GpsBean gps = new GpsBean();
		//接收终端时间是0时区的，需要先转换再存储
		String time = DateTime.now().parse(dataArray[3], DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");		
		gps.setTime(time);
		if (StringUtils.isNotBlank(dataArray[4])){
			String[] gpsArray = dataArray[4].split("\\|");
			gps.setJd(gpsArray[0]);	
			gps.setWd(gpsArray[1]);
			gps.setSd(gpsArray[2]);
			if (gpsArray.length > 3){
				gps.setFx(gpsArray[3]);
				gps.setHb(gpsArray[4]);	
			}
		}
		//将GPS存储到List集合中
		//redisDao.boundListOps(GpsBean.class.getName() + "-" + messageBean.getImei()).leftPush(gps);
		//推送GPS数据到biz中处理
		RequestCommonParamsDto dto = new RequestCommonParamsDto();
		String deviceId = iotServer.getDeviceIdByChannel(ctx.channel());
		dto.setDeviceId("dwq"+deviceId);
		dto.setLatitude(gps.getWd());
		dto.setLongitude(gps.getJd());
		dto.setStartTime(gps.getTime());
		dto.setEndTime(gps.getTime());
//				dto.setEventType("dwq"+dataArray[6]);
		dto.setSczt("10");
		redisDao.convertAndSend("gps", dto);
		
		//读取数据成功后，向终端响应结果
		sendCommonMsg(messageBean);
	}
}
