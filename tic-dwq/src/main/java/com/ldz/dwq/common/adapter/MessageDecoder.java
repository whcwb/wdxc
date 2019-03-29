package com.ldz.dwq.common.adapter;


import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.exception.BizException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 数据解码器，将接收到的字节数据，按照协议格式封装成bean结构
 * 报文协议，ASCII格式
 * 	内容		字节数	值范围		说明
 起始位	1		0xAA		1帧数据的起始位
 长度位	2		[8-65535]	从起始位到结束位的字节总数
 地址位	1		[0-255]		气象站通讯地址，0-万能地址
 命令位	2		[0-65535]	不同的命令都有对应的命令位
 参数位	N		[0-255]		数据参数，N的范围不定[0-65527]
 检验位	1					本命令的校验位
 结束位	1		0xBB		1帧数据的结束位
 *
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    Logger accessLog = LoggerFactory.getLogger("access_info");
	//判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是 byte+byte+int = 1+1+4 = 6
    private static final int HEADER_SIZE = 8;

    /**
     *
     * @param maxFrameLength 解码时，处理每个帧数据的最大长度
     * @param lengthFieldOffset 该帧数据中，存放该帧数据的长度的数据的起始位置
     * @param lengthFieldLength 记录该帧数据长度的字段本身的长度
     * @param lengthAdjustment 修改帧数据长度字段中定义的值，可以为负数
     * @param initialBytesToStrip 解析的时候需要跳过的字节数
     * @param failFast 为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
     */
    public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
    	super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
	}

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }
        int length = in.readableBytes();
        ByteBuf dataBuf = in.readBytes(length);
        String dataStr = dataBuf.toString(Charset.forName("GBK")).toUpperCase();
        accessLog.info("通道["+ctx.channel().id().asShortText()+"]接收数据:"+dataStr);
        if (length < HEADER_SIZE) {
            throw new Exception("可读信息段比头部信息小，拒绝通道连接,长度："+length);
        }
        int currentIndex = 0;
        int currentLength = MessageBean.start.length();
        String startStr = dataStr.substring(currentIndex,currentIndex + currentLength);
        currentIndex += currentLength;
        if (!MessageBean.start.equals(startStr)){
            throw new BizException("消息头不正确："+startStr);
        }

        MessageBean msg = new MessageBean();
        currentLength = 4;
        //通道接收数据
        String paramLengthStr = dataStr.substring(currentIndex);
        String[] dataArray = paramLengthStr.split(",");
        msg.setCommand(dataArray[0]);
        msg.setMid(dataArray[1]);
        msg.setImei(dataArray[2]);
        msg.setData(paramLengthStr);

        return msg;
    }

}
