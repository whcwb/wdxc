package com.ldz.znzp.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.znzp.bean.ZnzpOnlineBean;
import com.ldz.znzp.exception.IotException;
import com.ldz.znzp.model.ClZnzp;
import com.ldz.znzp.server.IotServer;
import com.ldz.znzp.service.ClService;
import com.ldz.znzp.service.HdService;
import com.ldz.znzp.service.XlService;
import com.ldz.znzp.service.ZnzpService;
import com.ldz.znzp.util.NettyUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Component
@Sharable
@SuppressWarnings("rawtypes")
public class ServerChannelHandler extends ChannelInboundHandlerAdapter{

	Logger accessLog = LoggerFactory.getLogger("access_info");
	
	Logger log = LoggerFactory.getLogger("error_info");
	
	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	@Autowired
	private StringRedisTemplate redisDao;
	@Autowired
	@Qualifier("redisOtherDB")
	private RedisTemplateUtil redisDaoOtherDB;
	@Autowired
	private Executor executor;
	//线路Service
	@Autowired
	private XlService xlService;
	@Autowired
	private ClService clService;

	@Autowired
	private HdService hdService;
	@Autowired
	private ZnzpService znzpService;
	@Autowired
	SnowflakeIdWorker idWorker;
	@Autowired
	private NettyUtil nettyUtil;



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
		}else{
			// 推送站牌离线事件
			Set<String> keys = redisDao.keys("*-" + ctx.channel().id()+"-*");
			keys.stream().forEach(s -> {
				List<String> list = Arrays.asList(s.split("-"));
				redisDaoOtherDB.convertAndSend("offline",list.get(0));
			});
		}
		
        //当连接关闭时，移除在线列表
      	closeChannel(ctx);
    }
	
	/**
	 * 接收发送的消息，最后需要手工release接收数据的ByteBuf
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf receiveMsg = (ByteBuf)msg;
		String tid = "";
		boolean online = false;
		boolean defaultReturn = true;
		try{
			int pkgDataLen = receiveMsg.readableBytes();
	        //从流中读取到指定长度的数据内容
	        ByteBuf dataLenBuf = receiveMsg.readBytes(pkgDataLen);
	        //将接收到的字节数据转换为指定编码的字符串
	        String dataStr = dataLenBuf.toString(Charset.forName("GBK"));
	        accessLog.debug("通道["+ctx.channel().id().asShortText()+"]接收数据:"+dataStr);
	        //帧头-1个字节，$
	        String headStr = dataStr.substring(0, 1);
	        if (!"$".equals(headStr)){
	        	throw new IotException("消息帧头不正确");
	        }
	        //包内容-N个字节，json字符串
	        String jsonData = dataStr.substring(6);
	        Map jsonMap = mapper.readValue(jsonData, Map.class);
	        //指令
	        String command = jsonMap.get("command").toString();
	        //智能站牌ID
	        tid = jsonMap.get("tid").toString();

	        switch (command){
				case "login":
					//设备第一次联网后，将主动向服务器发送登录指令。收到登录指令后，服务器向终端发送线路信息：routeInfo
					online = true;
					defaultReturn = false;
					getRouterInfo(ctx, tid);
					break;
				case "reporting":
					// 校车运行状态反馈
					report(ctx,tid);
					break;
				case "tick":
					//心跳包，智能站牌主动发送指令
					online = true;
					break;
				default:
					defaultReturn = false;
			}
		}finally{
			//从InBound里读取的ByteBuf要手动释放
			ReferenceCountUtil.release(msg);
		}
		//刷新设备在线标记
		if (online){
			online(ctx, tid);
		}
		
		if (defaultReturn){
			Map<String, String> sendData = new ConcurrentHashMap<String, String>();
			sendData.put("command", "tick");
			sendData.put("tid", tid);
			writeDataByte(ctx, sendData);
		}
	}

	/**
	 * 刷新智能站牌在线列表
	 */
	private void online(ChannelHandlerContext ctx, String tid) {
		ClZnzp znzp = znzpService.findById(tid);
		if( !ObjectUtils.isEmpty(znzp) && !StringUtils.equals(znzp.getZxZt(),"00")){
			znzp.setZxZt("00");
			znzpService.update(znzp);
		}
		String cid = ctx.channel().id().asShortText();
		String time = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
		redisDao.boundValueOps(tid+"-"+cid+"-"+ZnzpOnlineBean.class.getSimpleName()).set(time);
		redisDao.boundValueOps(tid+"-"+cid+"-"+ZnzpOnlineBean.class.getSimpleName()).expire(ServerChannelInitializer.READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES);

	}


	/**
	 * 智能站牌上线登录（login）成功后，向站牌返回线路详情
	 * 异步处理，不阻塞主线程
	 * @param ctx
	 * @param tid
	 */
	private void getRouterInfo(ChannelHandlerContext ctx, String tid){
		executor.execute(new Runnable() {
			@Override
			public void run() {
				//返回线路信息
				xlService.getRouterInfo(ctx, tid);

				//返回当前站点需要显示的活动信息
				try{
					Thread.sleep(1000*3);//暂时3秒
				}catch(Exception e){}

				hdService.sendActivityNews(ctx,tid);


			}
		});
	}

	/**
	 * 获取车辆运行状态
	 * @param ctx
	 */
	private void report(ChannelHandlerContext ctx, String tid){
		executor.execute(new Runnable() {
			@Override
			public void run() {
				//返回车辆运行状态
				clService.report(tid);
			}
		});
	}

	/**
	 * 服务器向终端发起上传数据请求，根据服务器请求的终端ID，从在线列表中找到该终端，使用终端通道发送数据请求命令
	 * @param ctx
	 * @param receiveMsg
	 */
	private void sendData(ChannelHandlerContext ctx, Map<String, String> receiveMsg){
		ApiResponse<String> result = new ApiResponse<String>();
		/*Channel existChannel = IotServer.liveChannels.get(receiveMsg.getTid());
		if (existChannel != null){
			IotMessageBean sendMsg = new IotMessageBean();
			sendMsg.setTid(receiveMsg.getTid());
			sendMsg.setCmdType(receiveMsg.getCmdType());
			sendMsg.setData(Utils.asciiToValue(receiveMsg.getData()));
			//等待结果执行完成
			ChannelFuture future = writeDataByteByChannel(existChannel, sendMsg);
			try {
				//使用阻塞方式等待结果执行
				future.await();
			} catch (InterruptedException e) {
				
			}
			
			if(future.isSuccess()) {
				result.setMessage("请求发送完成");
			} else {
				result = ApiResponse.fail("请求发送失败");
			}
		}else{
			//终端不在线
			result = ApiResponse.fail("终端不在线");
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			ctx.write(Unpooled.copiedBuffer(mapper.writeValueAsString(result), CharsetUtil.UTF_8));
		} catch (JsonProcessingException e) {
			
		}
		
		ctx.flush();*/
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
		Iterator<String> keys = redisDao.keys("*-"+ctx.channel().id().asShortText()+"-"+ZnzpOnlineBean.class.getSimpleName()).iterator();
		List<String> delKeys = new ArrayList<String>();
		while(keys.hasNext()){
			String key = keys.next();
			delKeys.add(key);
		}
		redisDao.delete(delKeys);
	}
}
