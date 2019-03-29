package com.ldz.obd.server;

import com.ldz.obd.bean.NettyConfigBean;
import com.ldz.obd.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableConfigurationProperties(NettyConfigBean.class)
public class NettyBaseServer {
	@Autowired
	private ServerChannelInitializer protocolInitalizer;
	//接收数据最大缓存数据长度
	private final static Integer RECEIVE_BUFFER_LENGTH = 1024 * 1024;
	//发送数据缓存长度
	private final static int SEND_BUFFER_LENGTH = 1024;

	@Bean
	public ServerBootstrap initServer(NettyConfigBean config){
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
    public NioEventLoopGroup bossGroup(int bossCount) {  
        return new NioEventLoopGroup(bossCount, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "TIC-BOSS_" + index.incrementAndGet());
            }
        });  
    }  
  
    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")  
    public NioEventLoopGroup workerGroup(int wokerCount) {  
        return new NioEventLoopGroup(wokerCount, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "TIC-WORKER_" + index.incrementAndGet());
            }
        });  
    } 
    
    @Bean
    public InetSocketAddress tcpPort(NettyConfigBean config){
    	return new InetSocketAddress(config.getPort());
    }
}
