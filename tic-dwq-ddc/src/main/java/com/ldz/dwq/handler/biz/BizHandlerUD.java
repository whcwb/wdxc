package com.ldz.dwq.handler.biz;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.redis.RedisTemplateUtil;

import io.netty.channel.ChannelHandlerContext;

/**
 * 终端定位数据上报命令解析
 * 数据格式：
	日期 				DDMMYY 221218(2018 年 12 月 22 日)
	时间 				hhmmss 151350(15 点 13 分 30 秒)
	是否定位 			A:定位 V:未定位 A
	纬度 				按照 DD.DDDDDD 格式定义 22.564025
	纬度标识 			N:南纬 S:北纬 N
	经度 				按照 DDD.DDDDDD 格式定义113.242329
	经度标识 			E:东经 W:西经 E
	速度 				公里/小时 5.21
	航向 				0-360 度 153
	海拔高度 			米 100
	卫星个数 			GPS 卫星个数 10
	GSM 				信号强度 百分比 100
	电源电压 			单位：伏(V) 10
	步数 				当天总步数 0
	翻滚次数 			缺省 0
	终端状态 			缺省 0
	基站个数 			基站个数，0 表示不上报基站信息6
	连接基站 TA 			GSM 时延，缺省 1
	MCC 				国家码 3 位数字 460
	MNC 				运营商 2 位数字 0
	基站列表-区域码 		区域码 10170
	基站列表-编号 		编号 3843
	基站列表-信号强度 	-110 到-50，值越大信号越好。 -65
	WIFI 				个数 WIFI 个数，0 表示无 WiFi 热点信息。3
	WIFI 				列表 SSID WIFI 热点名称 AP01
	WIFI 				列表 MAC WIFI 热点 MAC 地址 c8:f8:6d:7e:00:fd
	WIFI 				列表 SIGNAL WiFi 热点信号强度 -81
 * @author Lee
 *
 */
@Component
@SuppressWarnings("static-access")
public class BizHandlerUD extends BizBaseHandler {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		//221218,151350,V,,,,,,,,0,100,10.140000,0,0,0,6,1,460,0,10170,3843,-70,10170,3673,-74,10168,4062,-77,10170,3842,-87,10168,4063,-87,10170,3672,-87,2,AP01,c8:f8:6d:7e:00:fd,-81,AP00,50:bd:5f:20:d7:16,-83
		String[] dataArray = messageBean.getData().split(",");
		//是否定位。A:定位 V:未定位 A
		String dwzt = dataArray[2];
		if ("A".equals(dwzt)){
			GpsBean gps = new GpsBean();
			//接收终端时间是0时区的，需要先转换再存储
			String time = DateTime.now().parse(dataArray[0]+dataArray[1], DateTimeFormat.forPattern("ddMMyyHHmmss")).toString("yyyy-MM-dd HH:mm:ss");
			gps.setTime(time);
			//GPS定位
			gps.setWd(dataArray[3]);
			gps.setJd(dataArray[5]);
			gps.setSd(dataArray[7]);
			gps.setFx(dataArray[8]);
			gps.setHb(dataArray[9]);
			//将GPS存储到List集合中
			//redisDao.boundListOps(GpsBean.class.getName() + "-" + messageBean.getImei()).leftPush(gps);

			//推送GPS数据到biz中处理
			RequestCommonParamsDto dto = new RequestCommonParamsDto();
			String deviceId = iotServer.getDeviceIdByChannel(ctx.channel());
			gps.setDeviceId(deviceId);
//			String deviceId = "866401020000050";
			dto.setDeviceId(deviceId);
			dto.setLatitude(gps.getWd());
			dto.setLongitude(gps.getJd());
			dto.setStartTime(gps.getTime());
			dto.setEndTime(gps.getTime());
			dto.setFxj(gps.getFx());
			dto.setSpeed(StringUtils.isEmpty(gps.getSd()) ? "0" : gps.getSd());
			if (dto.getSpeed().contains(".")){
				dto.setSpeed(""+Math.round(new Float(dto.getSpeed())));
			}
			dto.setEventType(getEventType(dataArray[6]));
			dto.setSczt("10");
			redisDao.convertAndSend("gps", dto);
			redisDao.convertAndSend("dwq_info", gps);
	        redisTemplateUtil.opsForList().leftPush("dwq_gps",gps);
		}
		
		//读取数据成功后，向终端响应结果
		MessageBean sendData = new MessageBean();
		sendData.setMid(messageBean.getMid());
		sendData.setCommand(messageBean.getCommand());
		sendData.setImei(messageBean.getImei());

		iotServer.sendMsg(sendData);
	}

	/**
	 * 消息类型说明：
	 0 正常定位
	 1 休眠通知
	 2 SOS 告警
	 3 震动告警
	 4 摔倒告警
	 5 拆卸告警
	 6 拔出告警
	 7 急加速
	 8 急减速
	 9 急转弯
	 10 急变道
	 11 熄火
	 12 点火

	 * @param s
	 * @return
	 */
	private String getEventType(String s){
		switch (s){
			case "0":
				return null;
			case "1":
				return "DE01";
			case "2":
				return "DE02";
			case "3":
				return "DE03";
			case "4":
				return "DE04";
			case "5":
				return "DE05";
			case "6":
				return "DE06";
			case "7":
				return "10";
			case "8":
				return "20";
			case "9":
				return "30";
			case "10":
				return "DE10";
			case "11":
				return "60";
			case "12":
				return "50";
		}
		return null;
	}
}
