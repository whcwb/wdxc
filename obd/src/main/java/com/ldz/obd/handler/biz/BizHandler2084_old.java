package com.ldz.obd.handler.biz;

import com.ldz.dao.obd.model.GpsObdMessageBean;
import com.ldz.obd.common.bean.MessageBean;
import com.ldz.obd.util.MsgDecoder;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 请求/上传 GPS+OBD 混合信息 返回值
 *28383838383838208400400100032804140900042243881611349123970060091794D0000001000000A543881611349123970060091794D0000001000000A5000000000000000000000000F129
 */
@Slf4j
@Component
public class BizHandler2084_old extends BizBaseHandler {

    Logger accessLog = LoggerFactory.getLogger("access_info");
	@Autowired
	private RedisTemplateUtil redisTemplate;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean bean = (MessageBean)msg;
		iotServer.online(ctx, bean.getDeviceId());
		
		MsgDecoder msgDecoder = new MsgDecoder();
		GpsObdMessageBean obd=new GpsObdMessageBean();
		obd.setDeviceId(bean.getDeviceId());
		byte[] bodyBytes=bean.getBodyBytes();

		/**
		 * 数据类型 1 0x00 或者 0x01,其中 0x00 表示盲区数据，0x01 表示实时数据
		 * 00表示盲区 10 表示实时数据 20未知状态
		 */
		int value=msgDecoder.parseIntFromBytes(bodyBytes, 0, 1);
		String dataType="20";
		if(value==0x00 ){
			dataType="00";
		}else if(value==0x01 ){
			dataType="10";
		}
		obd.setDataType(dataType);

		/**
		 * 行程ID 长度：2    该数据属于哪个
		 */
		obd.setSchedulingCode(msgDecoder.parseIntFromBytes(bodyBytes,1,2)+"");
		/**
		 * 创建日期 长度：3 日月年表示.年省略”20”。比如 2014 年 4 月 28 日为 0x28 0x04 0x14 BCD 码
		 */
		String creatorDate=msgDecoder.parseBcdStringFromBytes(bodyBytes,3,3);

		obd.setCreatorDate("20"+creatorDate.substring(4,6)+creatorDate.substring(2,4)+creatorDate.substring(0,2));
		/**
		 * 创建时间 长度：3 时分秒表示,为格林威治时间。比如 9 点 0 分 4 秒为 0x09 0x00 0x04 BCD 码
		 */
		obd.setCreatortime(msgDecoder.parseBcdStringFromBytes(bodyBytes,6,3));
		/**
		 * 纬度  长度:4 实际纬度乘以 10000 的值,DDMM.MMMM 格式，比如 2233.4567     则上传 0x22 0x33 0x45 0x67
		 * 纬度。得到的数据需要进行度分转换。22438816/10000=22度43.8816分(43.8816/60)=22.73136度
		 */
		String latitude=msgDecoder.parseBcdStringFromBytes(bodyBytes,9,4);
		obd.setLatitude(""+(Double.parseDouble(latitude.substring(0,2))+Double.parseDouble(latitude.substring(2))/10000/60));
		/**
		 * 经度 4.5   实际经度乘以 10000 的值,DDDMM.MMMM 格式11334.5678 则上传 0x11 0x33 0x45 0x67 0x
		 * //经度+位指示一共5个字节，最后一个字节97,7表示的是位状态数据
		 //经度得到的数据，只要前9个字符，数据需要进行度分转换。113491239/10000=113度49.1239分(49.1239/60)=113.8187316666666667度
		 //最后一个字节，表示的是位指示
		 */
		String longitude=msgDecoder.parseBcdStringFromBytes(bodyBytes,13,5);
		obd.setLongitude(""+(Double.parseDouble(longitude.substring(0,3))+Double.parseDouble(longitude.substring(3,9))/10000/60));

		/**
		 * 位指示 GPS 是否定位,东西经及南北纬等.请参见
		 * 这里不清楚
		 */
		String indication=longitude.substring(9);
		/**
		 * 速度 1 当前 GPS 速度,以 km/h 为单位
		 */
		obd.setGpsTempo(msgDecoder.parseIntFromBytes(bodyBytes,18,1)+"");
		/**
		 * 方向 长度：1 当前方向,以度为单位,设备在上传对其进行了除 2 处理,所以,系统需 乘以 2,则还原为实际方向角度
		 * 公式 *2
		 */
		obd.setDirection(""+(msgDecoder.parseIntFromBytes(bodyBytes,19,1)*2));
		/**
		 * GPS 卫星个数 长度：1 当前 GPS 卫星的个数
		 */
		obd.setGpsCount(""+msgDecoder.parseIntFromBytes(bodyBytes,20,1));
		/**
		 * GSM 信号质量 长度：1 表明当前 GSM 的信号强弱,GSM 信号强度最大为 31
		 */
		obd.setGpsSignalIntensity(""+msgDecoder.parseIntFromBytes(bodyBytes,21,1));
		/**
		 * 里程 长度：4 当设备能读到原车里程的时候，该里程为仪表盘里程。否则为标定的 里程 + OBD 累加里程,单位 KM
		 */
		obd.setMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,22,4));
		/**
		 * 设备状态 长度：4 4 个字节，其中第一个字节暂时保留，第二个字节的定义如(表十)
		 *  所示，第三个字节的定义如(表十一)所示，第四个字节(表十二)所示 暂时没有处理
		 */
//        msgDecoder.parseIntFromBytes(bodyBytes,26,4)

		/**
		 * 负荷计算值。 长度：1  先进行10进制转换，然后使用公式：BYTE*100/255（165*100/255）=64（取整）
		 */
		double loadVlue=msgDecoder.parseIntFromBytes(bodyBytes,30,1);
		obd.setLoadValue(""+(loadVlue*100/255));
		/**
		 * 冷却液温度  长度：1 BYTE – 40，单位℃
		 */
		obd.setCoolantTemperature(""+(msgDecoder.parseIntFromBytes(bodyBytes,31,1)-40));
		/**
		 * 发动机转速  长度：2 ((BYTE1*256)+BYTE2)/4，(如 0x30 0x08 则 0x30 表示 BYTE1，
		 0x08 表示 BYTE2，以下类同，不再重复)，单位 RPM
		 */
		double engineSpeedByte1=msgDecoder.parseIntFromBytes(bodyBytes,32,1);
		double engineSpeedByte2=msgDecoder.parseIntFromBytes(bodyBytes,33,1);
		obd.setEngineSpeed(""+((engineSpeedByte1*256)+engineSpeedByte2)/4);
		/**
		 * OBD 车速 长度：1 单位 KM/H
		 */
		obd.setObdSpeed(""+msgDecoder.parseIntFromBytes(bodyBytes,34,1));
		/**
		 * 点火提前角 1 BYTE – 64，单位
		 */
		int ignitionAngleByte1=msgDecoder.parseIntFromBytes(bodyBytes,35,1);
		obd.setIgnitionAngle(""+(ignitionAngleByte1-64));

		/**
		 * 进气歧管绝对压力 1 BYTE，单位 kpa
		 */
		obd.setIntakePressure(""+msgDecoder.parseIntFromBytes(bodyBytes,36,1));

		/**
		 * 控制模块电压 1 BYTE/10，单位 V
		 */
		double controlVoltage=msgDecoder.parseIntFromBytes(bodyBytes,37,1);
		obd.setControlVoltage(""+(controlVoltage/10));
		/**
		 * 进气温度 1 BYTE-40，单位℃
		 */
		int intakeTemperature=msgDecoder.parseIntFromBytes(bodyBytes,38,1);
		obd.setIntakeTemperature(""+intakeTemperature);
		/**
		 * 空气流量 2 ((BYTE1*256)+BYTE2)/100，单位 g/s
		 */
		double intakeFlux1=msgDecoder.parseIntFromBytes(bodyBytes,39,1);
		double intakeFlux2=msgDecoder.parseIntFromBytes(bodyBytes,40,1);
		obd.setIntakeFlux(""+((intakeFlux1*256)+intakeFlux2)/100);
		/**
		 * 节气门相对位置 1 BYTE*100/255，单位%
		 */
		double jqmxdwz=msgDecoder.parseIntFromBytes(bodyBytes,41,1);
		obd.setJqmxdwz(""+(jqmxdwz*100/255));
		/**
		 * 长期燃油修正 1 (BYTE1-128)*100/128，单位%
		 */
		double chryxz=msgDecoder.parseIntFromBytes(bodyBytes,42,1);
		obd.setChryxz(""+((chryxz-128)*100/128));
		/**
		 * 空燃比系数 2 ((BYTE1*256)+BYTE2)*0.0000305
		 */
		double klbxs1=msgDecoder.parseIntFromBytes(bodyBytes,43,1);
		double klbxs2=msgDecoder.parseIntFromBytes(bodyBytes,44,1);
		obd.setKlbxs(""+((klbxs1*256)+klbxs2)*0.0000305);
		/**
		 * 节气门绝对位置 1 BYTE*100/255，单位%
		 */
		double jqmxjwz=msgDecoder.parseIntFromBytes(bodyBytes,45,1);
		obd.setJqmxjwz(""+(jqmxjwz*100/255));
		/**
		 * 燃油压力 1 BYTE*3,单位 kpa
		 */
		obd.setRyyl(""+msgDecoder.parseIntFromBytes(bodyBytes,46,1)*3);
		/**
		 * 瞬间油耗 L/H 2 ((BYTE1*256)+BYTE2)*0.1,单位 L/H
		 */
//		double sjyh1=msgDecoder.parseIntFromBytes(bodyBytes,47,1);
//		double sjyh2=msgDecoder.parseIntFromBytes(bodyBytes,48,1);
//		obd.setSjyh(""+((sjyh1*256)+sjyh2)*0.1);


		int yh1 = Integer.parseInt(bean.getContent().substring(96,98),16);
		int yh2 = Integer.parseInt(bean.getContent().substring(98,100),16);
		obd.setSjyh(""+((yh1*256)+yh2)*0.001);


		/**
		 * 剩余油量 2 if((BYTE1&0X80)==0X80)
		 ((BYTE1-0X80)*256)+BYTE2)*0.1 单位%
		 Else (BYTE1*256+BYTE2)*0.1 单位 L
		 */
		double syyl;
		if((bodyBytes[48]&0X80)==0X80){
			syyl = (((bodyBytes[49]-0X80)*256)+bodyBytes[50])*0.1;
		}else{
			syyl = (bodyBytes[49] * 256 + bodyBytes[50]) * 0.1;
		}
		obd.setSyyl(""+syyl);

		/**
		 *基站 8 4 字节运营商代码(IMSI 前 5 位),2 字节 LAC,2 字节 CELL
		 */
//        String jz=msgDecoder.parseIntFromBytes(bodyBytes,51,8);
		/**
		 *耗油量 4 OBD 累计耗油量,单位 L
		 */
		obd.setHyl(""+msgDecoder.parseIntFromBytes(bodyBytes,59,4));
		/**
		 * 流水号 1 从 0 -255 一直循环累加，可以用来检测是否有包丢失
		 */
		obd.setSeq(""+msgDecoder.parseIntFromBytes(bodyBytes,63,1));
		RequestCommonParamsDto dto = new RequestCommonParamsDto();
		dto.setDeviceId(bean.getDeviceId());
		dto.setSczt("10");
//		dto.setEventType("30");
		dto.setSpeed(obd.getGpsTempo());
		if (dto.getSpeed().contains(".")){
			dto.setSpeed(""+Math.round(new Float(dto.getSpeed())));
		}
		dto.setFxj(obd.getDirection());
		dto.setLatitude(obd.getLatitude());
		dto.setLongitude(obd.getLongitude());
        String startTime = DateTime.now().withZone(DateTimeZone.UTC).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
        dto.setStartTime(startTime);
		System.out.println(obd.toString());
		redisTemplate.convertAndSend("gps",dto);
		redisTemplate.convertAndSend("obd_info",obd);
		redisTemplate.opsForList().leftPush("gps_obd",obd);//写入redis
		super.channelRead(ctx, msg);
	}

	private String parseDate(String date ,String time){
		int hours = Integer.parseInt(time.substring(0,2)) + 8;
		if (hours > 23) hours -= 24;
		String hour = StringUtils.leftPad(""+hours ,2,"0");

		return date.substring(0,4) + "-"+date.substring(4,6)+"-"+date.substring(6,8)
				+" "+hour+":"+time.substring(2,4)+":"+time.substring(4,6);
	}
}
