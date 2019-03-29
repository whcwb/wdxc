package com.ldz.obd.handler.biz;

import com.ldz.obd.common.bean.MessageBean;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 设置/查询 车型 返回值
 * @author Lee
 *
 */
@Component
public class BizHandler3083 extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("BizHandler3083 handle message");
		MessageBean messageBean = (MessageBean) msg;
		System.out.println(messageBean.toString());
		super.channelRead(ctx, msg);
	}
}
