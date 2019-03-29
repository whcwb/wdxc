package com.ldz.obd.handler.biz;

import com.ldz.obd.common.bean.DeviceAlarm;
import com.ldz.obd.common.bean.MessageBean;
import com.ldz.obd.util.DataUtil;
import com.ldz.util.commonUtil.DateUtils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 4.1.5 设备报警 返回值
 *
 */
@Component
public class BizHandler0088 extends BizBaseHandler {

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean bean = (MessageBean)msg;
		DeviceAlarm alarm = new DeviceAlarm();
		alarm.setDeviceId(bean.getDeviceId());
		alarm.setCreateTime(DateUtils.getDateStr(DataUtil.parseDate(bean.getMessage()),"yyyy-MM-dd HH:mm:ss"));
		String[] position = DataUtil.parsePosition(bean.getMessage());
		alarm.setLng(position[0]);
		alarm.setLat(position[1]);

		System.out.println(alarm.toString());
		super.channelRead(ctx, msg);
	}
}
