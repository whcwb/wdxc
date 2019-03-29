package com.ldz.obd.handler.biz;

import com.ldz.obd.common.bean.MessageBean;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 4.1.4 请求设备重启 返回值
 *
 */
@Component
public class BizHandler0085 extends BizBaseHandler {

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean bean = (MessageBean)msg;
		super.channelRead(ctx, msg);
	}
}
