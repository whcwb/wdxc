package com.ldz.dwqjt.handler.biz;

import com.ldz.dwqjt.adapter.MessageDecoder;
import com.ldz.dwqjt.adapter.MessageEncoder;
import com.ldz.dwqjt.handler.ServerChannelHandler;
import com.ldz.dwqjt.jt808.service.codec.MsgDecoder;
import com.ldz.dwqjt.jt808.service.codec.MsgEncoder;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.server.IotServer;
import com.ldz.util.exception.RuntimeCheckException;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 1.原包：
 * 7E 02 00 00 26 12 34 56 78 90 12 00 (7D 02) 00 00 00 01 00 00 00 02 00 BA 7F 0E 07 E4 F1 1C 00 28 00 3C 00 00 18 10 15 10 10 10 01 04 00 00 00 64 02 02 00 (7D 01) 13 7E
 *
 * 2.进行反转义
 * 7D 02 ->7E
 * 7D 01 ->7D
 * 反转义后
 * 7E 02 00 00 26 12 34 56 78 90 12 00 7E 00 00 00 01 00 00 00 02 00 BA 7F 0E 07 E4 F1 1C 00 28 00 3C 00 00 18 10 15 10 10 10 01 04 00 00 00 64 02 02 00 7D 13 7E
 *
 * 3.拆解
 * 7E                  --头标识
 * 02 00               --数据头->消息ID
 * 00 26               --数据头->消息体属性
 * 12 34 56 78 90 12   --数据头->终端手机号
 * 00 7E               --数据头->消息流水号
 * 00 00 00 01         --消息体->报警标志
 * 00 00 00 02         --消息体->状态位标志
 * 00 BA 7F 0E         --消息体->纬度
 * 07 E4 F1 1C         --消息体->经度
 * 00 28               --消息体->海拔高度
 * 00 3C               --消息体->速度
 * 00 00               --消息体->方向
 * 18 10 15 10 10 10   --消息体->GPS时间
 * 01                  --消息体->附加信息->里程
 * 04                  --消息体->附加信息->长度
 * 00 00 00 64         --消息体->附加信息->数据
 * 02                  --消息体->附加信息->油量
 * 02                  --消息体->附加信息->长度
 * 00 7D               --消息体->附加信息->数据
 * 13                  --检验码
 * 7E                  --尾标识
 */
@Sharable
public class BizBaseHandler extends ChannelInboundHandlerAdapter {

	protected Logger accessLog = LoggerFactory.getLogger("access_info");
	protected Logger errorLog = LoggerFactory.getLogger("error_info");
	@Autowired
	protected IotServer iotServer;
	@Autowired
	protected RedisTemplateUtil redisDao;
	//当前终端ID
	protected String mId;
	protected MessageEncoder messageEncoder = new MessageEncoder();
	@Autowired
	protected MsgDecoder msgDecoder;
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		errorLog.error("通道["+this.mId+"]业务处理异常", cause.fillInStackTrace());
	}

	public static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	public void send2Client(Channel channel, byte[] arr) throws InterruptedException {
		String s = bytesToHexString(arr);
		accessLog.debug("发送数据："+s);
		ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(arr)).sync();
		if (!future.isSuccess()) {
			errorLog.error("发送数据出错:{}", future.cause());
		}
	}

	protected int getFlowId() {
		int flowId = iotServer.getFlowId();
		return flowId;
	}

	/**
	 * 通用标准应答协议
	 * @param messageBean
	 */
	public void sendCommonMsg(PackageData messageBean){
		/*MessageBean sendData = new MessageBean();
		sendData.setCommand("S5");
		sendData.setImei(messageBean.getImei());
		//消息ID,上行协议,结果
		String data = messageBean.getMid() + "," + messageBean.getCommand() + ",1";
		sendData.setData(data);

		iotServer.sendMsg(sendData);*/
	}
}
