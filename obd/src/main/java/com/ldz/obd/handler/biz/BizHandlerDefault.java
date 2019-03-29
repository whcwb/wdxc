package com.ldz.obd.handler.biz;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 读取系统参数命令解析
 * @author Lee
 *
 */
@Component
public class BizHandlerDefault extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
	}
}
