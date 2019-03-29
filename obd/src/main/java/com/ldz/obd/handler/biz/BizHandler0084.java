package com.ldz.obd.handler.biz;

import com.ldz.obd.common.bean.MessageBean;
import com.ldz.util.bean.RequestCommonParamsDto;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 4.1.3 恢复出厂设置 返回值
 *
 */
@Component
public class BizHandler0084 extends BizBaseHandler {

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean bean = (MessageBean)msg;

		super.channelRead(ctx, msg);
	}
}
