package com.ldz.dwq.handler.biz;

import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.exception.BizException;
import com.ldz.dwq.handler.ServerChannelHandler;
import com.ldz.dwq.server.IotServer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class BizBaseHandler extends ChannelInboundHandlerAdapter {

	protected Logger accessLog = LoggerFactory.getLogger("access_info");
	protected Logger errorLog = LoggerFactory.getLogger("error_info");
	@Autowired
	protected IotServer iotServer;
	@Autowired
	protected RedisTemplateUtil redisDao;
	//当前终端ID
	protected String mId;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		errorLog.error("通道["+this.mId+"]业务处理异常", cause.fillInStackTrace());
	}

	/**
	 * 获取通道ID值，如果获取ID值不存在，则抛出业务异常
	 * @param channel
	 * @throws Exception
	 */
	public void readChannelId(Channel channel) throws Exception{
		this.mId = channel.attr(ServerChannelHandler.DEVICENO).get();
        if (StringUtils.isEmpty(this.mId)){
        	throw new BizException("未读取到在线终端ID");
        }
	}

	/**
	 * 通用标准应答协议
	 * @param messageBean
	 */
	public void sendCommonMsg(MessageBean messageBean){
		MessageBean sendData = new MessageBean();
		sendData.setCommand("S5");
		sendData.setImei(messageBean.getImei());
		//消息ID,上行协议,结果
		String data = messageBean.getMid() + "," + messageBean.getCommand() + ",1";
		sendData.setData(data);

		iotServer.sendMsg(sendData);
	}
}
