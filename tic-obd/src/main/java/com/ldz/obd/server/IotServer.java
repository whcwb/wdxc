package com.ldz.obd.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * socket服务端
 * @author Lee
 *
 */
@Configuration
@Slf4j
public class IotServer {
	
	@Autowired
	private ServerBootstrap server;
	@Autowired
	private InetSocketAddress tcpSocket;
	
	private Channel serverChannel;
	//在线通道列表
	//public static Map<String, Channel> liveChannels = new ConcurrentSkipListMap<String, Channel>();	
	public static ChannelGroup onlineChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@PostConstruct
	public void start() throws Exception{
		log.info("启动服务器 " + tcpSocket);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					serverChannel = server.bind(tcpSocket).sync().channel().closeFuture().sync().channel();	
				}catch(Exception e){
					
				}
			}
		}).start();
	}
	
	@PreDestroy
	public void stop() throws Exception{
		log.info("停止服务器 " + tcpSocket);
		serverChannel.close();
		serverChannel.parent().close();
	}
}
