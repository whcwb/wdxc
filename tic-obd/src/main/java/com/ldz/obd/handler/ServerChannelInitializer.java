package com.ldz.obd.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	//读操作空闲5分钟
	public final static int READER_IDLE_TIME_SECONDS = 5;
	
	@Autowired
	private ServerChannelHandler serverHandler;
	
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();


        //设置心跳检测。单位为分钟
		pipeline.addLast(new ReadTimeoutHandler(READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES));

        pipeline.addLast(serverHandler);
	}
}
