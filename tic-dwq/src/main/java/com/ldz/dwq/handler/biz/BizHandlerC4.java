package com.ldz.dwq.handler.biz;

import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.util.commonUtil.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.redis.RedisTemplateUtil;

import io.netty.channel.ChannelHandlerContext;

import javax.swing.text.AbstractDocument;
import java.util.Scanner;

/**
 * 终端定位数据上报命令解析
 * 数据格式：
 * C4,消息 ID,IMEI,ACK 标志,消息类型,电量百分比,电压值：伏(V),充电标志,计步数据,里程数 ,UTC,MCC,MNC,GPS 定位参数,WIFI 定位参数,LBS 定位参数
 *
 * ACK 标志：0 不需要 ACK，1 需要 ACK
 *
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

 * 充电标志：0 为未充电，1 为正在充电
 *
 * GPS 定位参数：经度|纬度|速度|方向|海拔高度。
 * 经度 精确到小数点后 6 位，负号表示西半球
 * 纬度 精确到小数点后 6 位，负号表示南半球
 * 速度 单位：公里/小时
 * 方向 取值范围：0-359.9
 * 海拔高度 取值范围：-9999.9 - 99999.9
 *
 * WIFI 定位参数：SSID#MAC#RSSI|SSID#MAC#RSSI|…。最多不超过 10 个。
 * SSID MAC RSSI
 * 热点名称 MAC 地址 信号强度
 *
 * LBS 定位参数：LAC#CI#Dbm|LAC#CI#Dbm|…。最多不超过 6 个。
 * 基站位置区域码（LAC），基站小区识别码（CI），信号强度（Dbm）
 *
 * [C4,6,866401020000010,1,1,66,4.0,1,234,1532,2017-12-25 11:33:34,460,02,171225|113332|114.045072|22.678468|1.37|0|20|12|5,,9375#4292#-71|9375#4291#-83|9375#3932#-83]
 * @author Lee
 *
 */
@Component
@SuppressWarnings("static-access")
public class BizHandlerC4 extends BizBaseHandler {

    Logger accessLog = LoggerFactory.getLogger("access_info");

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		String[] dataArray = messageBean.getData().split(",");
        accessLog.info("dataArray -- > {}"  , JsonUtil.toJson(dataArray));
		GpsBean gps = new GpsBean();
		gps.setXxlx(dataArray[4]);
		gps.setLcs(dataArray[9]);
		//接收终端时间是0时区的，需要先转换再存储
		String time = DateTime.parse(dataArray[10], DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
		gps.setTime(time);
		if (StringUtils.isNotBlank(dataArray[13])){
			//GPS定位
			String[] gpsArray = dataArray[13].split("\\|");
			String d = "20"+gpsArray[0]+gpsArray[1];
			String t = DateTime.parse(d.trim(), DateTimeFormat.forPattern("yyyyMMddHHmmss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
			gps.setGpsTime(t);
			gps.setJd(gpsArray[2]);
			gps.setWd(gpsArray[3]);
			gps.setSd(gpsArray[4]);
			if (gpsArray.length > 5){
				gps.setFx(gpsArray[5]);
				gps.setHb(gpsArray[6]);
			}
		}else if (StringUtils.isNotBlank(dataArray[14])){
			//WIFI定位
			gps.setWifi(dataArray[14]);
		}else if (StringUtils.isNotBlank(dataArray[15])){
			//LBS定位
			gps.setLbs(dataArray[15]);
		}
		if(StringUtils.isNotBlank(dataArray[14])){
		    gps.setWifi(dataArray[14]);
        }
		if(StringUtils.isNotBlank(dataArray[15])){
		    gps.setLbs(dataArray[15]);
        }
		//将GPS存储到List集合中
		//redisDao.boundListOps(GpsBean.class.getName() + "-" + messageBean.getImei()).leftPush(gps);

		//推送GPS数据到biz中处理
		RequestCommonParamsDto dto = new RequestCommonParamsDto();
		String deviceId = iotServer.getDeviceIdByChannel(ctx.channel());
		gps.setDeviceId(messageBean.getImei());
//		String deviceId = "866401020000050";
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
		dto.setEventType(getEventType(dataArray[4]));
		dto.setSczt("10");
		dto.setLbs(gps.getLbs());
		redisDao.convertAndSend("gps", dto);
		redisDao.convertAndSend("dwq_info", gps);
        redisTemplateUtil.opsForList().leftPush("dwq_gps",gps);

		//ACK 标志：0 不需要 ACK，1 需要 ACK
		if ("1".equals(dataArray[3])){
			//读取数据成功后，向终端响应结果
			MessageBean sendData = new MessageBean();
			sendData.setCommand("S4");
			sendData.setImei(messageBean.getImei());
			//消息 ID,UTC,经度,纬度,时区。默认使用0时区，设备是使用的0时区
			String data = messageBean.getMid() + "," + DateTime.now().withZone(DateTimeZone.UTC).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss") + ",0,0,0";
			sendData.setData(data);

//			iotServer.sendMsg(sendData);
		}
		ctx.writeAndFlush("success");
		ctx.close();

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

	public static void main(String[] args) {
        String dd= "[C24,27,864767040063652,1,0,0,10.00,0,0,0,2019-08-26 04:40:40,460,0,1,190826|044040|114.045048|22.678618|0.000|254.31|-79.6|12|5,,9375#4242#-85]";
        String[] dataArray = dd.split(",");
        GpsBean gps = new GpsBean();
        gps.setXxlx(dataArray[4]);
        gps.setLcs(dataArray[9]);
        //接收终端时间是0时区的，需要先转换再存储
        String time = DateTime.parse(dataArray[10], DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
        gps.setTime(time);
        if (StringUtils.isNotBlank(dataArray[13])){
            //GPS定位
            String[] gpsArray = dataArray[13].split("\\|");
            String d = "20"+gpsArray[0]+gpsArray[1];
            String t = DateTime.parse(d.trim(), DateTimeFormat.forPattern("yyyyMMddHHmmss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
            gps.setGpsTime(t);
            gps.setJd(gpsArray[2]);
            gps.setWd(gpsArray[3]);
            gps.setSd(gpsArray[4]);
            if (gpsArray.length > 5){
                gps.setFx(gpsArray[5]);
                gps.setHb(gpsArray[6]);
            }
        }else if (StringUtils.isNotBlank(dataArray[14])){
            //WIFI定位
            gps.setWifi(dataArray[14]);
        }else if (StringUtils.isNotBlank(dataArray[15])){
            //LBS定位
            gps.setLbs(dataArray[15]);
        }
    }
}
