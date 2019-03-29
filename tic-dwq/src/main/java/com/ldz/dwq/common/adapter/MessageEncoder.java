package com.ldz.dwq.common.adapter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.handler.ServerChannelHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 数据协议bean结构
 * 报文协议，ASCII格式
 * 	内容		字节数	值范围		说明
 起始位	1		[		1帧数据的起始位
 长度位	2		[8-65535]	从起始位到结束位的字节总数
 地址位	1		[0-255]		气象站通讯地址，0-万能地址
 命令位	2		[0-65535]	不同的命令都有对应的命令位
 参数位	N		[0-255]		数据参数，N的范围不定[0-65527]
 检验位	1					本命令的校验位
 结束位	1		]		1帧数据的结束位
 */
public class MessageEncoder extends MessageToByteEncoder<MessageBean> {

	/**
	 * 执行下发命令必须一条条执行，同一个通道不能同时下发多条指令
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, MessageBean msg, ByteBuf out) throws Exception {
		if(null == msg){
            throw new Exception("发送数据不能为空");
        }

        String string = msg.getCmdData();
        String deviceNo = ctx.channel().attr(ServerChannelHandler.DEVICENO).get();
        if (StringUtils.isNotBlank(deviceNo)){
        	CountDownLatch tmpCd = ctx.channel().attr(ServerChannelHandler.SYNC).get();
        	if (tmpCd != null){
        		//如果超过10秒还未执行完，认为上一次任务执行完成，可以再执行新的命令
        		tmpCd.await(10, TimeUnit.SECONDS);
        	}else{
        		//说明上一次任务执行完，本次可以直接执行任务
        		ctx.channel().attr(ServerChannelHandler.SYNC).set(new CountDownLatch(1));
        	}
        }

        if (StringUtils.isNotEmpty(string)){
        	out.writeBytes(string.getBytes());
        }
	}
}
