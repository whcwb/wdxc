package com.ldz.obd.server;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.ldz.obd.common.bean.MessageBean;
import com.ldz.obd.exception.BizException;
import com.ldz.obd.handler.ServerChannelHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

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
	@Autowired
	private StringRedisTemplate stringRedis;
	
	private Channel serverChannel;
	
	public final static String ONLINE_KEY = "ONLINE";
	//在线通道列表
	public static ChannelGroup onlineChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * 查找一个终端是否在线
	 * @param tid
	 * @return
	 */
	public boolean isOnline(String tid){
		return stringRedis.keys(tid+"-*-"+ONLINE_KEY).iterator().hasNext();
	}
	
	/**
	 * 刷新在线列表
	 */
	public void online(ChannelHandlerContext ctx, String tid){
		if (StringUtils.isBlank(tid)){
			return;
		}
		//将16进制转换为10进制结果
		final String tmpId = tid;
		//将通道放入group中
		if (!IotServer.onlineChannels.contains(ctx.channel())){
			//查看相同的终端号之前是否有保留的通道，如果有则自动清理掉旧通道
			IotServer.onlineChannels.removeIf(channel->{
				String deviceNo = channel.attr(ServerChannelHandler.DEVICENO).get();
				if (StringUtils.isNotBlank(deviceNo) && tmpId.equals(deviceNo)){
					channel.close();
					return true;
				}
				
				return false;
			});
			//为通道设置终端no属性值
			ctx.channel().attr(ServerChannelHandler.DEVICENO).set(tid);
			IotServer.onlineChannels.add(ctx.channel());
		}
		
		String cid = ctx.channel().id().asShortText();
		String time = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
		String onlineKey = tid+"-"+cid+"-"+IotServer.ONLINE_KEY;
		stringRedis.boundValueOps(onlineKey).set(time);
		stringRedis.boundValueOps(onlineKey).expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);
	}
	
	public void sendMsg(String facId, MessageBean message){
		Channel onlineChannel = IotServer.onlineChannels.parallelStream().filter(channel -> {
			String id = channel.attr(ServerChannelHandler.DEVICENO).get();
			if (facId.equals(id)){
				return true;
			}
			
			return false;
		}).findFirst().orElseThrow(()->new BizException("当前终端未在线！"));
		
		//指令发送
		onlineChannel.writeAndFlush(message);
	}
	
	@PostConstruct
	public void start() throws Exception{
		log.info("启动服务器 " + tcpSocket);
		System.out.println("启动服务器 " + tcpSocket);
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
		System.out.println("启动服务器 " + tcpSocket);
		serverChannel.close();
		serverChannel.parent().close();
	}
}
