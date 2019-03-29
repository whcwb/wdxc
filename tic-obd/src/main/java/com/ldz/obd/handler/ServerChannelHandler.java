package com.ldz.obd.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldz.obd.bean.PackageData;
import com.ldz.obd.exception.IotException;
import com.ldz.obd.server.IotServer;
import com.ldz.obd.service.QueryService;
import com.ldz.obd.service.codec.MsgDecoder;
import com.ldz.obd.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 通道业务处理
 */
@Component
@Sharable
@SuppressWarnings("rawtypes")
public class ServerChannelHandler extends ChannelInboundHandlerAdapter{
	Logger accessLog = LoggerFactory.getLogger("access_info");
	Logger log = LoggerFactory.getLogger("error_info");
	
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@Autowired
	private QueryService queryService;

	@Autowired
	private StringRedisTemplate redisDao;

	@Autowired
	private NettyUtil nettyUtil;
	private MsgDecoder decoder = new MsgDecoder();

	// 连接成功后，向server发送消息 
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//将新建立的通道放入group中
		IotServer.onlineChannels.add(ctx.channel());
		super.channelActive(ctx);
	}
	
	// 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		//当连接关闭时，移除在线列表
		closeChannel(ctx);
		super.channelInactive(ctx);
	}
	
	//通道内发生异常数据
	@Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if (!(cause instanceof ReadTimeoutException)){
			log.error("通道["+ctx.channel().id().asShortText()+"]数据处理异常", cause.fillInStackTrace());
		}
		
        //当连接关闭时，移除在线列表
      	closeChannel(ctx);
    }
	
	/**
	 * 接收发送的消息，最后需要手工release接收数据的ByteBuf
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		boolean online = false;
		String equipmentId=null;//设备ID
		try{
			ByteBuf buf = (ByteBuf) msg;

			if (buf.readableBytes() <= 0) {//验证报文是不是为空
				return;
			}
			byte[] bs = new byte[buf.readableBytes()];
			buf.readBytes(bs);

			//将接收到的字节数据转换为指定编码的字符串
			String dataStr = DatatypeConverter.printHexBinary(bs);
			String regex = "(.{2})";
			dataStr = dataStr.replaceAll (regex, "$1 ");
			accessLog.debug("通道["+ctx.channel().id().asShortText()+"]接收数据:"+dataStr);

			if(bs[0]!=0x28){
				throw new IotException("第一个字节验证失败"+bs[0]);
			}
			if(bs[bs.length-1]!=0x29){
				throw new IotException("最后一个字节验证失败");
			}

			//处理转义字符
			bs= messageFigurativeSense(bs);

			PackageData packageMsg = this.decoder.queueElement2PackageData(bs);

			packageMsg.setCheckType(true);// TODO: 2018/4/11 测试，将校验暂时注掉，因为目前测试报文都是造的，以获取报文为主

			if(packageMsg.getCheckType()){
				packageMsg.setCtx(ctx);//在接收到报文后，为以后给客户端发送消息。
				// 处理客户端消息
				this.processClientMsg(packageMsg);
				online=true;
				equipmentId=packageMsg.getEquipmentId();
			}else{
				throw new IotException("校验失败");
			}

		}catch (Exception e){
			e.printStackTrace();
		}finally{
			//从InBound里读取的ByteBuf要手动释放
			ReferenceCountUtil.release(msg);
		}
		//刷新设备在线标记
		if (online){
			online(ctx, equipmentId);
		}
//
//		if (defaultReturn){
//			Map<String, String> sendData = new ConcurrentHashMap<String, String>();
//			sendData.put("command", "tick");
//			sendData.put("tid", tid);
//			writeDataByte(ctx, sendData);
//		}
	}


	private void processClientMsg(PackageData msg) {
		// 请求/上传 GPS+OBD 混合信息  0x20 0x04 –> 0x20 0x
		if (msg.getOrderCode() == 0x2084) {
			accessLog.debug("    请求/上传 GPS+OBD 混合信息");
			if(msg.getBodyLength()!=0x0040){
				throw new IotException("请求/上传 GPS+OBD 混合信息正文长度异常正常是 64字节 请求来的长度是："+msg.getBodyLength());
			}
			// 请求/上传 GPS+OBD 混合信息
			getGpsObdMessage(msg);
		} else if (msg.getOrderCode() == 0x3088) {//0x30 0x88  4.4.4 行程报告上传
			accessLog.debug("    4.4.4 行程报告上传");
			// 行程报告上传
			if(msg.getBodyLength()!=0x004D){
				throw new IotException("行程报告上传正文长度异常正常是 77字节 请求来的长度是："+msg.getBodyLength());
			}
			uploadTravelItineraryMessage(msg);
		}else if(msg.getOrderCode() == 0x3087){//0x30 0x87 查询发动机系统的故障码
			accessLog.debug("    查询发动机系统的故障码");
			//查询发动机系统的故障码
			if(msg.getBodyLength()%3!=0){//指令的内容长度一定是 3个倍数
				throw new IotException("发动机系统的故障码上传正文长度异常正常是 3的倍数 请求来的长度是："+msg.getBodyLength());
			}
			uploadFaultCodeMessage(msg);
		}else if(msg.getOrderCode() == 0x008E){//0x00 0x8E 4.1.12 唤醒/休眠报告
			accessLog.debug("    4.1.12 唤醒/休眠报告");
			// 当前设备是处理：唤醒/休眠报告
			if(msg.getBodyLength()!=0x0008){
				throw new IotException("唤醒/休眠报告 正文长度异常正常是 8字节 请求来的长度是："+msg.getBodyLength());
			}
//			deviceOnLineType  设备开机状态。
			deviceOnLineType(msg);



		}
		else {
			accessLog.debug("    该,消息业务ID："+msg.getOrderCode());
			log.error("该,消息ID={}", msg.getOrderCode());
		}
	}

	/**
	 * 设备在线(开机)状态
	 * @param msg
	 */
	private void deviceOnLineType(PackageData msg) {
		queryService.deviceOnLineType(msg);
	}

	/**
	 * 发动机故障码上传
	 * @param msg
	 */
	private void uploadFaultCodeMessage(PackageData msg) {
		queryService.uploadFaultCodeMessage(msg);
	}

	private void uploadTravelItineraryMessage(PackageData msg) {
		queryService.uploadTravelItineraryMessage(msg);
	}

	/**
	 * 请求/上传 GPS+OBD 混合信息
	 */
	private void getGpsObdMessage(PackageData msg){
		queryService.getGpsObdMessage(msg);
//		executor.execute(new Runnable() {
//			@Override
//			public void run() {
//
//			}
//		});
	}

	/**
	 * 刷新在线设备
	 */
	private void online(ChannelHandlerContext ctx, String tid){
		String cid = ctx.channel().id().asShortText();
		String time = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
//		redisDao.boundValueOps(tid+"-"+cid+"-ticobd").set(time);
//		redisDao.boundValueOps(tid+"-"+cid+"-ticobd").expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);
	}


	/**
	 * 向客户端发送数据，发送的数据必须转换成ByteBuf数组    
	 * @param ctx
	 */
	private void writeDataByte(ChannelHandlerContext ctx, Map<String, String> sendData){
		nettyUtil.sendData(ctx,sendData);
	}
	
	/**
	 * 向客户端发送数据，发送的数据必须转换成ByteBuf数组    
	 */
	private ChannelFuture writeDataByteByChannel(Channel channel, Map<String, String> sendData){
        //发送数据同时刷新通道，数据发送完后，由netty自行负责释放该ByteBuf对象
		return channel.writeAndFlush(sendData);
	}
	
	/**
	 * 关闭通道，同时移除当前通道在线列表信息
	 * @param ctx
	 */
	public void closeChannel(ChannelHandlerContext ctx){
		//从group中移除关闭的通道
		IotServer.onlineChannels.remove(ctx.channel());	
		//关闭通道
		ctx.close();
		//主动移除终端在线状态
		Iterator<String> keys = redisDao.keys("*-"+ctx.channel().id().asShortText()+"-ticobd").iterator();
		List<String> delKeys = new ArrayList<String>();
		while(keys.hasNext()){
			String key = keys.next();
			delKeys.add(key);
		}
		redisDao.delete(delKeys);
	}
	/**
	 * 报文转义
	 * 0x3D 0x15 => 0x28
	 * 0x3D 0x14=> 0x29
	 * 0x3D 0x00=> 0x3D
	 * @param bs
	 * @return
	 */
	private byte[] messageFigurativeSense(byte[] bs){
		byte[] ret=new byte[bs.length];
		int k=0;
		for(int i=0;i<bs.length;i++){
			if(bs[i]==0x3D){
				if(bs[i+1]==0x15){
					ret[k]=0x28;
					i++;
				}else if(bs[i+1]==0x14){
					ret[k]=0x29;
					i++;
				}else if(bs[i+1]==0x00){
					ret[k]=0x3D;
					i++;
				}else{
					ret[k]=bs[i];
				}
			}else{
				ret[k]=bs[i];
			}
			k++;
		}
		byte[] rets=null;
		if(k>1){
			rets=new byte[k];
			System.arraycopy(ret, 0, rets, 0, k);
		}
		return rets;
	}

}
