package com.ldz.dwq.handler;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import com.ldz.dwq.common.tools.SpringContextUtil;
import com.ldz.dwq.handler.biz.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.RateLimiter;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.exception.BizException;
import com.ldz.dwq.server.IotServer;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

@Component
@Sharable
public class ServerChannelHandler extends ChannelInboundHandlerAdapter{

	Logger accessLog = LoggerFactory.getLogger("access_info");
	Logger log = LoggerFactory.getLogger("error_info");
	//业务处理handler在通道中的名称
	final static String PIPELINE_BIZ_HANDLER_NAME = "serviceHandler";
	//通道终端no属性字段
	public final static AttributeKey<String> DEVICENO = AttributeKey.newInstance("deviceno");
	//同步执行属性
	public final static AttributeKey<CountDownLatch> SYNC = AttributeKey.newInstance("sync");
	//命令队列属性字段。因为同一个通道同一时间只能有一个命令执行，命令不能同时执行，所以需要队列方式来保存需要执行的命令
	public final static AttributeKey<LinkedBlockingQueue<MessageBean>> CMDQUEUE = AttributeKey.newInstance("queue");
	@Autowired
	private StringRedisTemplate stringRedis;
	//限速器
	@Autowired
	private RateLimiter QPSLimiter;

	// 连接成功后，向server发送消息
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//通道连接建立后，获取终端设备的ID

	}

	/**
	 * 接收发送的消息，最后需要手工release接收数据的ByteBuf
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try{
			MessageBean messageBean = (MessageBean)msg;
			//accessLog.info("新消息："+messageBean.toString());
			CountDownLatch cd = ctx.channel().attr(ServerChannelHandler.SYNC).get();
			if (cd != null){
				//执行获取数据成功，解除发送命令锁定状态
				cd.countDown();
				//重置同步标记为null，表示命令接收成功
				ctx.channel().attr(ServerChannelHandler.SYNC).set(null);
			}
			if (!QPSLimiter.tryAcquire()){
				//超过每秒限流值，将不处理该消息内容
				return;
			}

			//先移除通道内的业务handler，根据实际命令码，加载新的handler进行业务处理
			if (ctx.pipeline().get(PIPELINE_BIZ_HANDLER_NAME) != null){
				ctx.pipeline().remove(PIPELINE_BIZ_HANDLER_NAME);
			}

			//解析数据内容
			String handlerName = "bizHandler" + messageBean.getCommand();
			// 根据命令类型判断是否有相应的处理器
			if (!SpringContextUtil.containBean(handlerName)){
				log.error("未知命令类型："+messageBean.getCommand());
				return;
			}
			//接收命令成功，更新终端在线状态

			// 使用spring根据命令类型获取相应处理器
			BizBaseHandler handler = (BizBaseHandler) SpringContextUtil.getBean(handlerName);
			ctx.pipeline().addLast(PIPELINE_BIZ_HANDLER_NAME, handler);

			//将消息向下传递
			ctx.fireChannelRead(msg);
		}finally{
			//手工release接收数据的ByteBuf，防止内存溢出
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//当连接关闭时，移除在线列表
		closeChannel(ctx);
	}

	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (cause instanceof BizException){
			return;
		}else if (cause instanceof ReadTimeoutException){

		}else{
	        log.error("通道["+ctx.channel().attr(ServerChannelHandler.DEVICENO).get()+"]数据处理异常", cause.fillInStackTrace());
		}
        //当连接关闭时，移除在线列表
      	closeChannel(ctx);
    }

	/**
	 * 关闭通道，同时移除当前通道在线列表信息
	 * @param ctx
	 */
	public void closeChannel(ChannelHandlerContext ctx){
		String deviceNo = ctx.channel().attr(DEVICENO).get();
		//从group中移除关闭的通道
		IotServer.onlineChannels.remove(ctx.channel());
		//关闭通道
		ctx.close();
		if (StringUtils.isNotBlank(deviceNo)){
			//主动移除终端在线状态.Redis
			stringRedis.delete(deviceNo+"-"+ctx.channel().id().asShortText()+"-"+IotServer.ONLINE_KEY);
		}
	}
}
