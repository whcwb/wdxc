package com.ldz.obd.handler.biz;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ldz.obd.exception.BizException;
import com.ldz.obd.handler.ServerChannelHandler;
import com.ldz.obd.server.IotServer;
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
	//当前终端ID
	protected String mId;
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		errorLog.error("通道["+this.mId+"]业务处理异常", cause.fillInStackTrace());
	}
	
	/**
	 * 获取通道ID值，如果获取ID值不存在，则抛出业务异常
	 * @param channel
	 * @throws Exception
	 */
	public void readChannelId(Channel channel) throws Exception{
		this.mId = channel.attr(ServerChannelHandler.DEVICENO).get();
        if (StringUtils.isEmpty(this.mId)){
        	throw new BizException("未读取到在线终端ID，无法解析实时数据"); 
        }
	}
}
