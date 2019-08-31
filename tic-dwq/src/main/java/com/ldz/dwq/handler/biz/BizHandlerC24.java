package com.ldz.dwq.handler.biz;

import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.dao.dwq.model.GpsBeanNew;
import com.ldz.dao.dwq.model.GpsInfo;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 由终端自动更新，或者平台下发指令， 终端上报定位数据
 *
 * 上行格式
 * [C4,消息 ID,IMEI,ACK 标志,消息类型,电 量百分比,电压值,充电标志,计步数据, 里 程 数 ,UTC,MCC,MNC,GPS 定 位 个 数,GPS 定位参数[n],WIFI 定位参数,LBS 定位参数]
 *
 * 下行格式
 *
 * [S4,消息 ID,UTC,经度,纬度,时区]
 * 无经纬度时用 0 代替
 *
 * [C24,6,866401020000010,1,1,66,4.0,1,23 4,1532,2017-12-25 11:33:34,460,02,5,171225|113330|114.045172|22.678448|1.37|0|20|12|5,171225|113331|114.045222|22.678338|1.37|0|20|12|5,171225|113332|114.045332|22.678228|1.37|0|20|12|5,171225|113333|114.045442|22.678118|1.37|0|20|12|5,171225|113334|114.045552|22.678468|1.37|0|20|12|5,,9375#4292#-71|9375#4291#-83|9375#3932#-83]
 */
@Component
@Slf4j
@SuppressWarnings("static-access")
public class BizHandlerC24 extends BizBaseHandler {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
	Logger accessLog = LoggerFactory.getLogger("access_info");

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		MessageBean messageBean = (MessageBean)msg;
		String[] dataArray = messageBean.getData().split(",");
		GpsBeanNew data = new GpsBeanNew();
        String deviceId = messageBean.getImei();
        data.setDeviceId(deviceId);
		data.setXxlx(dataArray[4]);
		data.setLcs(dataArray[9]);
		//接收终端时间是0时区的，需要先转换再存储
		String time = DateTime.parse(dataArray[10], DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
		data.setTime(time);
		int gpsNum = 0;
		List<GpsInfo> gpsInfos = null;
		if (StringUtils.isNotBlank(dataArray[13])){
			gpsNum = Integer.parseInt(dataArray[13]);
			data.setGpsNum(dataArray[13]);
			String[] gpsInfoArray = new String[gpsNum];
			for (int i = 0;i<gpsNum;i++){
				gpsInfoArray[i] = dataArray[14 + i];
			}
			gpsInfos = new ArrayList<>(gpsInfoArray.length);
			for (String gpsInfo : gpsInfoArray) {
				if (StringUtils.isBlank(gpsInfo))continue;
				GpsInfo info = new GpsInfo();
				//GPS定位
				String[] gpsArray = gpsInfo.split("\\|");
				String d = "20"+gpsArray[0]+gpsArray[1];
				String t = DateTime.parse(d.trim(), DateTimeFormat.forPattern("yyyyMMddHHmmss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
				info.setTime(t);
				info.setJd(gpsArray[2]);
				info.setWd(gpsArray[3]);
				info.setSd(gpsArray[4]);
				info.setFx(gpsArray[5]);
				info.setHb(gpsArray[6]);
				info.setKjwxgs(gpsArray[7]);
				info.setSywxgs(gpsArray[8]);
				gpsInfos.add(info);
			}
			data.setGpsInfos(gpsInfos);
			data.setGpsParams(gpsInfos.toString());
		}else if (StringUtils.isNotBlank(dataArray[14 + gpsNum])){
			//WIFI定位
			data.setWifi(dataArray[14 + gpsNum]);
		}else if (StringUtils.isNotBlank(dataArray[15 + gpsNum])){
			//LBS定位
			data.setLbs(dataArray[15+gpsNum]);
		}
		if(StringUtils.isNotBlank(dataArray[14 +gpsNum])){
			data.setWifi(dataArray[14+gpsNum]);
		}
		if(StringUtils.isNotBlank(dataArray[15+gpsNum])){
			data.setLbs(dataArray[15+gpsNum]);
		}

		accessLog.info("C24:  dataArray ->"+ Arrays.toString(dataArray));
		accessLog.info("C24:  gpsNum ->"  + gpsNum);
		log.info("gpsInfo:"+data.toString());
		//将GPS存储到List集合中
		//redisDao.boundListOps(GpsBean.class.getName() + "-" + messageBean.getImei()).leftPush(gps);

		//推送GPS数据到biz中处理
		if (gpsInfos != null){
			if (gpsInfos.size() != 0){
				List<RequestCommonParamsDto> dtoList = new ArrayList<>(gpsInfos.size());
				boolean sendEvent = false;
				int eventType = Integer.parseInt(dataArray[4]);
				for (GpsInfo gpsInfo : gpsInfos) {
					RequestCommonParamsDto dto = new RequestCommonParamsDto();
					dto.setDeviceId(messageBean.getImei());
					dto.setLatitude(gpsInfo.getWd());
					dto.setLongitude(gpsInfo.getJd());
					dto.setStartTime(gpsInfo.getTime());
					dto.setEndTime(gpsInfo.getTime());
					dto.setFxj(gpsInfo.getFx());
					dto.setStarNum(gpsInfo.getKjwxgs());
					dto.setSpeed(StringUtils.isEmpty(gpsInfo.getSd()) ? "0" : gpsInfo.getSd());
					if (dto.getSpeed().contains(".")){
						dto.setSpeed(""+Math.round(new Float(dto.getSpeed())));
					}
					if (eventType > 2 && !sendEvent){
						dto.setEventType(getEventType(dataArray[4]));
						sendEvent = true;
					}
					if(StringUtils.equals(dto.getSpeed(), "0")){
						dto.setSczt("20");
					}else{
						dto.setSczt("10");
					}
					dto.setLbs(dataArray[15 + gpsNum]);
					dtoList.add(dto);
					log.info("推送到biz数据："+dto.toString());
					redisDao.convertAndSend("dwq_info", gpsInfo);
					GpsBean bean = new GpsBean();
					bean.setDeviceId(messageBean.getImei());
					bean.setFx(gpsInfo.getFx());
					bean.setGpsTime(gpsInfo.getTime());
					bean.setHb(gpsInfo.getHb());
					bean.setJd(gpsInfo.getJd());
					bean.setLcs(dataArray[9]);
					bean.setSd(gpsInfo.getSd());
					bean.setWd(gpsInfo.getWd());
					bean.setTime(gpsInfo.getTime());
					bean.setXxlx(dataArray[4]);
					bean.setWifi(dataArray[14 + gpsNum]);
					bean.setLbs(dataArray[15 + gpsNum]);
					accessLog.info(" gpsSize  != 0  ----> " + JsonUtil.toJson(bean));
					redisTemplateUtil.opsForList().leftPush("dwq_gps",bean);
				}
				redisDao.convertAndSend("gpsList", dtoList);
			}else{
				RequestCommonParamsDto dto = new RequestCommonParamsDto();
				dto.setDeviceId(messageBean.getImei());
				dto.setLatitude("-1");
				dto.setLongitude("-1");
				dto.setStartTime(time);
				dto.setEndTime(time);
				dto.setSpeed("0");
				dto.setFxj("0");
				dto.setEventType(getEventType(dataArray[4]));
				dto.setSczt("10");
				log.info("推送到biz数据："+dto.toString());
				redisDao.convertAndSend("gps", dto);
			}
		}else{
			RequestCommonParamsDto dto = new RequestCommonParamsDto();
			dto.setDeviceId(messageBean.getImei());
			dto.setLatitude(data.getWd());
			dto.setLongitude(data.getJd());
			dto.setStartTime(data.getTime());
			dto.setEndTime(data.getTime());
			dto.setFxj(data.getFx());
			dto.setLbs(data.getLbs());
			dto.setSpeed(StringUtils.isEmpty(data.getSd()) ? "0" : data.getSd());
			if (dto.getSpeed().contains(".")){
				dto.setSpeed(""+Math.round(new Float(dto.getSpeed())));
			}
			dto.setEventType(getEventType(dataArray[4]));
			if(StringUtils.equals(dto.getSpeed(), "0")){
				dto.setSczt("20");
			}else{
				dto.setSczt("10");
			}
			redisDao.convertAndSend("gps", dto);
			redisDao.convertAndSend("dwq_info", data);
			redisTemplateUtil.opsForList().leftPush("dwq_gps",data);
		}

		//ACK 标志：0 不需要 ACK，1 需要 ACK
		if ("1".equals(dataArray[3])){
			//读取数据成功后，向终端响应结果
			MessageBean sendData = new MessageBean();
			sendData.setCommand("S4");
			sendData.setImei(messageBean.getImei());
			sendData.setMid("24");
			//消息 ID,UTC,经度,纬度,时区。默认使用0时区，设备是使用的0时区
			String data1 = messageBean.getMid() + "," + DateTime.now().withZone(DateTimeZone.UTC).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss") + ",0,0,0";
			sendData.setData(data1);
			iotServer.sendMsg(sendData);
		}
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
