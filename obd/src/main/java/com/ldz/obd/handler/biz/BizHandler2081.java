package com.ldz.obd.handler.biz;

import com.ldz.obd.common.bean.MessageBean;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 设置/查询 GPS位置信息上传通道和间隔 返回值
 *
 */
@Component
public class BizHandler2081 extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("BizHandler2081");
		MessageBean messageBean = (MessageBean)msg;
		super.channelRead(ctx, msg);
	}
}
