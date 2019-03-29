package com.ldz.obd.handler.biz;

import com.ldz.obd.common.bean.MessageBean;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 清除发动机系统的故障码 返回值
 *
 */
@Component
public class BizHandler3086 extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		MessageBean messageBean = (MessageBean)msg;
		System.out.println("BizHandler3086");
		System.out.println(messageBean.toString());
		super.channelRead(ctx, msg);
	}
}
