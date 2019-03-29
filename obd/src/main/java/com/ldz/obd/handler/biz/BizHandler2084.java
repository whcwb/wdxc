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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
public class BizHandler2084 extends BizBaseHandler {

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
		String content = bean.getContent();
		String[] strs = new String[32];
		Integer[] lengths = new Integer[]{2,4,6,6,8,9,1,2,2,2,2,8,8,2,2,4,2,2,2,2,2,4,2,2,4,2,2,4,4,16,8,2};
		int t = 0;
		for (int i = 0; i < lengths.length; i++) {
			strs[i] = content.substring(t,t + lengths[i]);
			t += lengths[i];
		}
//		for (int i = 0; i < strs.length; i++) {
//			System.out.println((i+1)+"-"+strs[i].length()+":"+strs[i]);
//		}
		/**
		 * 数据类型 1 0x00 或者 0x01,其中 0x00 表示盲区数据，0x01 表示实时数据
		 * 00表示盲区 10 表示实时数据 20未知状态
		 */
		obd.setDataType(strs[0]);

		/**
		 * 行程ID 长度：2    该数据属于哪个
		 */
		obd.setSchedulingCode(strs[1]);
//		obd.setSchedulingCode(msgDecoder.parseIntFromBytes(bodyBytes,1,2)+"");
		/**
		 * 创建日期 长度：3 日月年表示.年省略”20”。比如 2014 年 4 月 28 日为 0x28 0x04 0x14 BCD 码
		 */
		String[] time = formatCreateTime("20" + strs[2], strs[3]);
		obd.setCreatorDate(time[0]);
		/**
		 * 创建时间 长度：3 时分秒表示,为格林威治时间。比如 9 点 0 分 4 秒为 0x09 0x00 0x04 BCD 码
		 */
		obd.setCreatortime(time[1]);
		/**
		 * 纬度  长度:4 实际纬度乘以 10000 的值,DDMM.MMMM 格式，比如 2233.4567     则上传 0x22 0x33 0x45 0x67
		 * 纬度。得到的数据需要进行度分转换。22438816/10000=22度43.8816分(43.8816/60)=22.73136度
		 */
		String latitude = strs[4];
		obd.setLatitude(""+(Double.parseDouble(latitude.substring(0,2))+Double.parseDouble(latitude.substring(2))/10000/60));
		/**
		 * 经度 4.5   实际经度乘以 10000 的值,DDDMM.MMMM 格式11334.5678 则上传 0x11 0x33 0x45 0x67 0x
		 * //经度+位指示一共5个字节，最后一个字节97,7表示的是位状态数据
		 //经度得到的数据，只要前9个字符，数据需要进行度分转换。113491239/10000=113度49.1239分(49.1239/60)=113.8187316666666667度
		 //最后一个字节，表示的是位指示
		 */
		String longitude = strs[5];
		obd.setLongitude(""+(Double.parseDouble(longitude.substring(0,3))+Double.parseDouble(longitude.substring(3,9))/10000/60));

		/**
		 * 位指示 GPS 是否定位,东西经及南北纬等.请参见
		 * 这里不清楚
		 */
		obd.setIndication(strs[6]);
		/**
		 * 速度 1 当前 GPS 速度,以 km/h 为单位
		 */
		obd.setGpsTempo(""+hex2Int(strs[7]));
		/**
		 * 方向 长度：1 当前方向,以度为单位,设备在上传对其进行了除 2 处理,所以,系统需 乘以 2,则还原为实际方向角度
		 * 公式 *2
		 */
		obd.setDirection(""+hex2Int(strs[8])*2);
		/**
		 * GPS 卫星个数 长度：1 当前 GPS 卫星的个数
		 */
		obd.setGpsCount(""+hex2Int(strs[9]));
		/**
		 * GSM 信号质量 长度：1 表明当前 GSM 的信号强弱,GSM 信号强度最大为 31
		 */
		obd.setGpsSignalIntensity(""+ hex2Int(strs[10]));
		/**
		 * 里程 长度：4 当设备能读到原车里程的时候，该里程为仪表盘里程。否则为标定的 里程 + OBD 累加里程,单位 KM
		 */
		obd.setMileage(""+hex2Int(strs[11]));
		/**
		 * 设备状态 长度：4 4 个字节，其中第一个字节暂时保留，第二个字节的定义如(表十)
		 *  所示，第三个字节的定义如(表十一)所示，第四个字节(表十二)所示 暂时没有处理
		 */
//        msgDecoder.parseIntFromBytes(bodyBytes,26,4)

		/**
		 * 负荷计算值。 长度：1  先进行10进制转换，然后使用公式：BYTE*100/255（165*100/255）=64（取整）
		 */
		int loadVlue = hex2Int(strs[13]);
		obd.setLoadValue(""+(loadVlue*100/255));
		/**
		 * 冷却液温度  长度：1 BYTE – 40，单位℃
		 */
		obd.setCoolantTemperature(""+(Integer.parseInt(strs[14]) - 40));
		/**
		 * 发动机转速  长度：2 ((BYTE1*256)+BYTE2)/4，(如 0x30 0x08 则 0x30 表示 BYTE1，
		 0x08 表示 BYTE2，以下类同，不再重复)，单位 RPM
		 */
		String es = strs[15];
		double engineSpeedByte1 = hex2Int(""+es.substring(0,2));
		double engineSpeedByte2 = hex2Int(""+es.substring(2,4));
		obd.setEngineSpeed(""+((engineSpeedByte1*256)+engineSpeedByte2)/4);
		/**
		 * OBD 车速 长度：1 单位 KM/H
		 */
		obd.setObdSpeed(""+hex2Int(strs[16]));
		/**
		 * 点火提前角 1 BYTE – 64，单位
		 */
		obd.setIgnitionAngle(""+(hex2Int(strs[17])-64));

		/**
		 * 进气歧管绝对压力 1 BYTE，单位 kpa
		 */
		obd.setIntakePressure(""+hex2Int(strs[18]));

		/**
		 * 控制模块电压 1 BYTE/10，单位 V
		 */
		obd.setControlVoltage(""+(hex2Int(strs[19])/10));
		/**
		 * 进气温度 1 BYTE-40，单位℃
		 */
		obd.setIntakeTemperature(""+(hex2Int(strs[20]) - 40));
		/**
		 * 空气流量 2 ((BYTE1*256)+BYTE2)/100，单位 g/s
		 */
		String k = strs[21];
		double intakeFlux1 = hex2Int(k.substring(0,2));
		double intakeFlux2 = hex2Int(k.substring(2,4));
		obd.setIntakeFlux(""+((intakeFlux1*256)+intakeFlux2)/100);
		/**
		 * 节气门相对位置 1 BYTE*100/255，单位%
		 */
		double jqmxdwz = hex2Int(strs[22]);
		obd.setJqmxdwz(""+(jqmxdwz*100/255));
		/**
		 * 长期燃油修正 1 (BYTE1-128)*100/128，单位%
		 */
		double chryxz = hex2Int(strs[23]);
		obd.setChryxz(""+((chryxz-128)*100/128));
		/**
		 * 空燃比系数 2 ((BYTE1*256)+BYTE2)*0.0000305
		 */
		String klbxs = strs[24];
		double klbxs1 = hex2Int(klbxs.substring(0,2));
		double klbxs2 = hex2Int(klbxs.substring(2,4));
		obd.setKlbxs(""+((klbxs1*256)+klbxs2)*0.0000305);
		/**
		 * 节气门绝对位置 1 BYTE*100/255，单位%
		 */
		double jqmxjwz=hex2Int(strs[25]);
		obd.setJqmxjwz(""+(jqmxjwz*100/255));
		/**
		 * 燃油压力 1 BYTE*3,单位 kpa
		 */
		obd.setRyyl(""+hex2Int(strs[26])*3);
		/**
		 * 瞬间油耗 L/H 2 ((BYTE1*256)+BYTE2)*0.1,单位 L/H
		 */
//		double sjyh1=msgDecoder.parseIntFromBytes(bodyBytes,47,1);
//		double sjyh2=msgDecoder.parseIntFromBytes(bodyBytes,48,1);
//		obd.setSjyh(""+((sjyh1*256)+sjyh2)*0.1);


		int yh1 = hex2Int(strs[27].substring(0,2));
		int yh2 = hex2Int(strs[27].substring(2,4));
		obd.setSjyh(""+((yh1*256)+yh2)*0.1);


		/**
		 * 剩余油量 2 if((BYTE1&0X80)==0X80)
		 ((BYTE1-0X80)*256)+BYTE2)*0.1 单位%
		 Else (BYTE1*256+BYTE2)*0.1 单位 L
		 */
		double syyl;
		int b1 = hex2Int(strs[28].substring(0,2));
		int b2 = hex2Int(strs[28].substring(2,4));
		byte by1 = bodyBytes[49];
		byte by2 = bodyBytes[50];
		if((bodyBytes[49]&0X80)==0X80){
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
		obd.setHyl(""+hex2Int(strs[30]));
		/**
		 * 流水号 1 从 0 -255 一直循环累加，可以用来检测是否有包丢失
		 */
		obd.setSeq(""+hex2Int(strs[31]));

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
        String startTime = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
        dto.setStartTime(startTime);
		System.out.println(obd.toString());
		redisTemplate.convertAndSend("gps",dto);
		redisTemplate.convertAndSend("obd_info",obd);
		redisTemplate.opsForList().leftPush("gps_obd",obd);//写入redis
		super.channelRead(ctx, msg);
	}

	private int hex2Int(String s){
		return Integer.parseInt(s,16);
	}

	private String parseDate(String date ,String time){
		int hours = Integer.parseInt(time.substring(0,2)) + 8;
		if (hours > 23) hours -= 24;
		String hour = StringUtils.leftPad(""+hours ,2,"0");

		return date.substring(0,4) + "-"+date.substring(4,6)+"-"+date.substring(6,8)
				+" "+hour+":"+time.substring(2,4)+":"+time.substring(4,6);
	}

	private String[] formatCreateTime(String createDate, String createTime){
		DateTimeFormatter yyyyMMdd = DateTimeFormat.forPattern("yyyyMMdd");
		DateTime time = yyyyMMdd.parseDateTime(createDate);
		int hours = Integer.parseInt(createTime.substring(0,2)) + 8;
		if (hours > 23) {
			hours -= 24;
			DateTime dateTime = time.plusDays(1);
			createDate =  dateTime.toString("yyyyMMdd");
		}
		String[] s = new String[2];
		s[0] = createDate;
		String hour = StringUtils.leftPad(""+hours ,2,"0");
		s[1] = hour + createTime.substring(2,6);

		return s;
	}

}
