package com.ldz.dwqjt.server;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ldz.dwqjt.bean.DeviceBean;
import com.ldz.dwqjt.handler.ServerChannelHandler;
import com.ldz.dwqjt.handler.ServerChannelInitializer;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.util.exception.RuntimeCheckException;
import com.ldz.util.redis.RedisTemplateUtil;

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
	private RedisTemplateUtil redisDao;

	private Channel serverChannel;
	private int currentFlowId = 0;

	public final static String ONLINE_KEY = "ONLINE";
	public final static String PHONE_DEVICEID = "JT808_PHONE_DEVICEID-";
	public final static String PHONE_AUTHCODE = "JT808_PHONE_AUTHCODE-";
	//在线通道列表
	public static ChannelGroup onlineChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public int getFlowId(){
		if (currentFlowId >= 0xffff)
			currentFlowId = 0;
		return currentFlowId;
	}

	public void putPhoneDevice(String phone,String deviceId){
		String key = PHONE_DEVICEID+phone;
		redisDao.boundValueOps(key).set(deviceId);
		redisDao.boundValueOps(key).expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);
	}

	public String getDeviceIdByPhone(String phone){
		String key = PHONE_DEVICEID+phone;
		if (!redisDao.hasKey(key)) return null;
		String s = (String) redisDao.boundValueOps(key).get();
		return s;
	}
	public void putAuthCode(String phone,String authCode){
		String key = PHONE_AUTHCODE+phone;
		redisDao.boundValueOps(key).set(authCode);
		redisDao.boundValueOps(key).expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);
	}

	public String getAuthCodeByPhone(String phone){
		String key = PHONE_AUTHCODE+phone;
		if (!redisDao.hasKey(key)) return null;
		String s = (String) redisDao.boundValueOps(key).get();
		return s;
	}
	/**
	 * 通道channel ID获取在线终端ID
	 * @param cid
	 * @return
	 */
	public String getTid(String cid){
		String tid = null;
		//主动移除终端在线状态.Redis
		Iterator<Object> keys = redisDao.keys("*-"+cid+"-"+ONLINE_KEY).iterator();
		while(keys.hasNext()){
			String key = keys.next().toString();
			tid = key.split("-")[0];
			break;
		}

		return tid;
	}

	/**
	 * 查找一个终端是否在线
	 * @param tid
	 * @return
	 */
	public boolean isOnline(String tid){
		return redisDao.keys(tid+"-*-"+ONLINE_KEY).iterator().hasNext();
	}


	public void online(Channel channel,String tid){
		if (StringUtils.isBlank(tid)){
			return;
		}
		final String tmpId = tid;

		//将通道放入group中
		if (!IotServer.onlineChannels.contains(channel)){
			//查看相同的终端号之前是否有保留的通道，如果有则自动清理掉旧通道
			IotServer.onlineChannels.removeIf(c->{
				String deviceNo = c.attr(ServerChannelHandler.DEVICENO).get();
				if (StringUtils.isNotBlank(deviceNo) && tmpId.equals(deviceNo)){
					c.close();
					return true;
				}

				return false;
			});
			//为通道设置终端no属性值
			channel.attr(ServerChannelHandler.DEVICENO).set(tid);
			IotServer.onlineChannels.add(channel);
		}

		String cid = channel.id().asShortText();
		String time = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
		String onlineKey = tid+"-"+cid+"-"+IotServer.ONLINE_KEY;
		DeviceBean device = new DeviceBean();
		device.setImei(tid);
		device.setTime(time);
		redisDao.boundValueOps(onlineKey).set(device);
		redisDao.boundValueOps(onlineKey).expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);
	}
	/**
	 * 刷新在线列表
	 */
	public void online(ChannelHandlerContext ctx, String tid){
		online(ctx.channel(),tid);
	}

	/**
	 * 更新终端信息，
	 * @param ctx
	 * @param tid
	 * @param sbgn
	 */
	public void updateDevice(ChannelHandlerContext ctx, String tid, String sbgn){
		if (StringUtils.isBlank(tid)){
			return;
		}

		String cid = ctx.channel().id().asShortText();
		String time = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
		String onlineKey = tid+"-"+cid+"-"+IotServer.ONLINE_KEY;
		DeviceBean device = new DeviceBean();
		device.setImei(tid);
		device.setTime(time);
		device.setSbgn(sbgn);
		redisDao.boundValueOps(onlineKey).set(device);
		redisDao.boundValueOps(onlineKey).expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);
	}

	/**
	 * 向终端发送数据
	 * @param message
	 */
	public void sendMsg(PackageData message){
		/*if (message == null || StringUtils.isBlank(message.getImei())){
			throw new RuntimeCheckException("终端编号不存在！");
		}

		Channel onlineChannel = IotServer.onlineChannels.parallelStream().filter(channel -> {
			String id = channel.attr(ServerChannelHandler.DEVICENO).get();
			if (message.getImei().equals(id)){
				return true;
			}

			return false;
		}).findFirst().orElseThrow(()->new RuntimeCheckException("当前终端未在线！"));

		//指令发送
		onlineChannel.writeAndFlush(message);*/
	}

	public String getDeviceIdByChannel(Channel channel){
		Iterator<Channel> it = IotServer.onlineChannels.iterator();
		while(it.hasNext()){
			Channel c = it.next();
			if (c.id().equals(channel.id())){
				return c.attr(ServerChannelHandler.DEVICENO).get();
			}
		}
		return null;
	}

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
