package com.ldz.dwq.server;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.google.common.util.concurrent.RateLimiter;

import com.ldz.dwq.config.NettyConfig;
import com.ldz.dwq.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Configuration
@EnableConfigurationProperties(NettyConfig.class)
public class NettyBaseServer {
	@Autowired
	private ServerChannelInitializer protocolInitalizer;
	//接收数据最大缓存数据长度
	private final static Integer RECEIVE_BUFFER_LENGTH = 1024 * 1024;
	//发送数据缓存长度
	private final static int SEND_BUFFER_LENGTH = 1024;
	
	//QPS每秒请求限速器
	@Bean
	@Primary
	public RateLimiter QPSLimiter(NettyConfig config){
    	return RateLimiter.create(config.getQpsLimiterValue());
	}

	@Bean
	@Primary
	public ServerBootstrap initServer(NettyConfig config){
		ServerBootstrap server = new ServerBootstrap();  
        //boss负责请求的accept操作;work负责请求的read、write和处理操作
    	server.group(bossGroup(config.getBossGroupCount()), workerGroup(config.getWokerGroupCount()))
        	.channel(NioServerSocketChannel.class)
        	.handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(protocolInitalizer)
            .option(ChannelOption.TCP_NODELAY, true)
    		.option(ChannelOption.SO_RCVBUF, RECEIVE_BUFFER_LENGTH)
    		.option(ChannelOption.SO_SNDBUF, SEND_BUFFER_LENGTH)
            .option(ChannelOption.SO_BACKLOG, config.getBacklog())  
            .option(ChannelOption.SO_KEEPALIVE, config.isKeepAlive());  
    	
    	return server;
	}
	
	//NioEventLoopGroup主要负责管理eventLoop的生命周期，eventLoop数量默认为处理器个数的两倍
	@Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
	@Primary
    public NioEventLoopGroup bossGroup(int bossCount) {  
        return new NioEventLoopGroup(bossCount);  
    }  
  
    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    @Primary
    public NioEventLoopGroup workerGroup(int wokerCount) {  
        return new NioEventLoopGroup(wokerCount);  
    } 
    
    @Bean
    @Primary
    public InetSocketAddress tcpPort(NettyConfig config){
    	return new InetSocketAddress(config.getPort());
    }
}
