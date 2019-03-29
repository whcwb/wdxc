package com.ldz.obd.handler.biz;

import com.ldz.obd.common.bean.FlameInOut;
import com.ldz.obd.common.bean.MessageBean;
import com.ldz.obd.util.DataUtil;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 4.4.5 点火熄火报告
 *
 */
@Component
public class BizHandler3089 extends BizBaseHandler {

	@Autowired
	private RedisTemplateUtil redisTemplate;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean bean = (MessageBean)msg;
		FlameInOut data = new FlameInOut();
		data.setDeviceId(bean.getDeviceId());
		String conetent = bean.getContent();
		data.setType("0"+conetent.charAt(1));
		data.setTravelId(conetent.substring(2,6));
		Date date = DataUtil.parseDate(conetent.substring(6,18));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		data.setTime(format.format(date));
		if (bean.getContentLength() > 9){
			String[] position = DataUtil.parsePosition(conetent);
			data.setLat(position[0]);
			data.setLat(position[0]);
		}

		RequestCommonParamsDto dto = new RequestCommonParamsDto();
		dto.setDeviceId(data.getDeviceId());
		dto.setLatitude(data.getLat());
		dto.setLongitude(data.getLng());
		dto.setStartTime(data.getTime());
		dto.setEventType("01".equals(data.getType()) ? "50" : "60");
		dto.setSczt("01".equals(data.getType()) ? "10" : "20");
		redisTemplate.convertAndSend("gps", dto);
		super.channelRead(ctx, msg);
	}
}
