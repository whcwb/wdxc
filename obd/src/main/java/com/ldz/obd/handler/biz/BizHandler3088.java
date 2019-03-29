package com.ldz.obd.handler.biz;

import com.ldz.dao.obd.model.ObdTravelItineraryBean;
import com.ldz.obd.common.bean.MessageBean;
import com.ldz.obd.util.JsonUtil;
import com.ldz.obd.util.MsgDecoder;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 行程报告上传
 *
 */
@Component
public class BizHandler3088 extends BizBaseHandler {
	@Autowired
	private RedisTemplateUtil redisTemplate;

	Logger accessLog = LoggerFactory.getLogger("access_info");
	@Value("${obdTravelItineraryList-key:obdTravelItineraryList_}")
	private String obdTravelItineraryListKey;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("BizHandler3088 handle message");
		MessageBean messageBean = (MessageBean) msg;
		ObdTravelItineraryBean obd=new ObdTravelItineraryBean();
        obd.setDeviceId(messageBean.getDeviceId());
		MsgDecoder msgDecoder = new MsgDecoder();
		byte[] bodyBytes = messageBean.getMessage().getBytes();
		int contentStart = 22;
		/**
		 * 1、行程ID 长度：2 表示该报告属于哪个行程
		 */
		obd.setSchedulingCode(msgDecoder.parseIntFromBytes(bodyBytes,contentStart,2)+"");
		/**
		 * 2、点火日期  长度：3		日月年表示.年省略”20”，BCD 码表示
		 */
//		String ignitionDate=msgDecoder.parseBcdStringFromBytes(bodyBytes,contentStart + 4,3);
		obd.setIgnitionDate("20"+getDate(messageBean.getContent().substring(4,10)));
		/**
		 * 3、点火时间		长度：3		时分秒表示,为格林威治时间。BCD 码表示
		 */
//		obd.setIgnitionTime(msgDecoder.parseBcdStringFromBytes(bodyBytes,5,3));
		obd.setIgnitionTime(getTime(messageBean.getContent().substring(10,16)));

		/**
		 * 4、熄火日期		长度：3  同上(点火时间和熄火时间为 GPS 时间，下面的时间为设备定时器累加的时间，单位 S，每次点火熄火时间相减的时间和下面累加的时间可能有几 S 的误差)
		 */
//		String flameoutDate=msgDecoder.parseBcdStringFromBytes(bodyBytes,8,3);
//		obd.setFlameoutDate("20"+flameoutDate.substring(4,6)+flameoutDate.substring(2,4)+flameoutDate.substring(0,2));
		obd.setFlameoutDate("20"+getDate(messageBean.getContent().substring(16,22)));

		/**
		 * 5、熄火时间		长度：3		同上
		 */
		obd.setFlameoutTime(getTime(messageBean.getContent().substring(22,28)));

		/**
		 * 6、该次行驶时间		长度：2		单位 S，该时间为设备定时器累加的时间，当 GPS 时间无效的时候，可用该时间显示行驶的时间
		 */
		obd.setTimeConsuming(""+msgDecoder.parseIntFromBytes(bodyBytes,14,2)) ;

		/**
		 * 7、该次耗油量		长度：4		单位 ML
		 */
		obd.setOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,16,4));

		/**
		 * 8、该次里程		长度：4		单位 M
		 */
		obd.setMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,20,4));

		/**
		 * 9、最高速度		长度：1		KM/H
		 */
		obd.setMaximumSpeed(""+msgDecoder.parseIntFromBytes(bodyBytes,24,1));

		/**
		 * 10、发动机最高转速		长度：2
		 * ((BYTE1*256)+BYTE2)/4,单位 rpm
		 */
		double maximumRev1=msgDecoder.parseIntFromBytes(bodyBytes,25,1);
		double maximumRev2=msgDecoder.parseIntFromBytes(bodyBytes,26,1);
		obd.setMaximumRev(""+((maximumRev1*256)+maximumRev2)/4);

		/**
		 * 11、冷却液最高温度		长度：1		BYTE - 40
		 */
		int maximumTemperature=msgDecoder.parseIntFromBytes(bodyBytes,27,1);
		obd.setMaximumTemperature(""+(maximumTemperature-64));

		/**
		 * 12、急加速次数		长度：1		次
		 */
		obd.setRapidAccelerationCount(""+msgDecoder.parseIntFromBytes(bodyBytes,28,1));

		/**
		 * 13、急减速次数		长度：1		次
		 */
		obd.setRapidDecelerationCount(""+msgDecoder.parseIntFromBytes(bodyBytes,29,1));

		/**
		 * 14、超速行驶时间(>120km/h)		长度：2		单位 S
		 */
		obd.setOverspeedTime(""+msgDecoder.parseIntFromBytes(bodyBytes,30,2)) ;

		/**
		 * 15、超速行驶的里程		长度：4		单位 M
		 */
		obd.setOverspeedMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,32,4));

		/**
		 * 16、超速行驶的耗油量		长度：4		单位 ML.
		 */
		obd.setOverspeedOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,36,4));

		/**
		 * 17、高速行驶的时间(80km/h-120km/h)		长度：2		S
		 */
		obd.setHighspeedTime(""+msgDecoder.parseIntFromBytes(bodyBytes,40,2));

		/**
		 * 18、高速行驶的里程		长度：4		M
		 */
		obd.setHighspeedMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,42,4));

		/**
		 * 19、高速行驶的耗油量		长度：4		单位 ML.
		 */
		obd.setHighspeedOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,46,4));

		/**
		 * 20、中速行驶的时间(40km/h-80km/h)		长度：2		S
		 */
		obd.setIntermediateTime(""+msgDecoder.parseIntFromBytes(bodyBytes,50,2));

		/**
		 * 21、中速行驶的里程		长度：4		M
		 */
		obd.setIntermediateMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,52,4));

		/**
		 * 22、中速行驶的耗油量		长度：4		单位 ML.
		 */
		obd.setIntermediateOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,56,4));

		/**
		 * 23、低速行驶的时间(1km/h-40km/h)		长度：2		S
		 */
		obd.setLowspeedTime(""+msgDecoder.parseIntFromBytes(bodyBytes,60,2));

		/**
		 * 24、低速行驶的里程		长度：4		M
		 */
		obd.setLowspeedMileage(""+msgDecoder.parseIntFromBytes(bodyBytes,62,4));

		/**
		 * 25、低速行驶的耗油量		长度：4		单位 ML.
		 */
		obd.setLowspeedOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,66,4));

		/**
		 * 26、怠速的时间		长度：2		S
		 */
		obd.setIdlingTime(""+msgDecoder.parseIntFromBytes(bodyBytes,70,2));

		/**
		 * 27、怠速的耗油量		长度：4		ML
		 */
		obd.setIdlingOilWear(""+msgDecoder.parseIntFromBytes(bodyBytes,72,4));

		/**
		 * 28、急转弯次数		长度：1		次
		 */
		obd.setJzwCount(""+msgDecoder.parseIntFromBytes(bodyBytes,76,1));
		redisTemplate.boundListOps("travelReport").leftPushAll(obd);
//		redisTemplate.opsForList().leftPushAll("travelReport", obd);
		accessLog.info("save travelReport to redis");
		redisTemplate.convertAndSend("travelReport", JsonUtil.toJson(obd));
		super.channelRead(ctx, msg);
	}

	private String getDate(String s){
	    return s.substring(4,6) + "-" + s.substring(2,4) + "-" + s.substring(0,2);
    }
	private String getTime(String s){
	    int hour = Integer.parseInt(s.substring(0,2)) + 8;
	    if (hour > 24) hour -= 24;
	    return StringUtils.leftPad(""+hour,2,"0") + ":" + s.substring(2,4) + ":" + s.substring(4,6);
    }
}
