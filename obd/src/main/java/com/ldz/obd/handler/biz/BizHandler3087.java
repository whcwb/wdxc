package com.ldz.obd.handler.biz;

import com.ldz.dao.obd.model.ObdFaultCodeBean;
import com.ldz.obd.common.bean.MessageBean;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询发动机系统的故障码 返回值
 *
 */
@Component
public class BizHandler3087 extends BizBaseHandler {

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	private static final String[] codes = {"P0","P1","P2","P3","C0","C1","C2","C3","B0","B1","B2","B3","U0","U1","U2","U3"};

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean bean = (MessageBean)msg;
		String data = bean.getContent();
		int c = data.length() / 6;
		String code = "";
		for (int i = 0;i < c;i++){
			int t = i*6;
			String temp = data.substring(t+1,t + 4);
			int index = Integer.parseInt(""+data.charAt(t),16);
			code += codes[index] + temp+",";
		}
		ObdFaultCodeBean obdFaultCodeBean = new ObdFaultCodeBean();
		obdFaultCodeBean.setCreationTime(new Date());
		obdFaultCodeBean.setFaultCode(code);
		obdFaultCodeBean.setDeviceId(bean.getDeviceId());
		redisTemplateUtil.convertAndSend("engine_fault",obdFaultCodeBean);
		super.channelRead(ctx, msg);
	}
}
