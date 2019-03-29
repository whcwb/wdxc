package com.ldz.obd.handler.biz;

import com.ldz.obd.common.bean.MessageBean;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 设置/查询 GPS+OBD混合信息上传间隔  返回值
 *
 */
@Component
public class BizHandler2083 extends BizBaseHandler {

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean bean = (MessageBean)msg;
		super.channelRead(ctx, msg);
	}
}
