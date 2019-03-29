package com.ldz.dwq.handler;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ldz.dwq.common.adapter.MessageDecoder;
import com.ldz.dwq.common.adapter.MessageEncoder;
import com.ldz.dwq.common.bean.MessageBean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	//处理每个帧数据的最大长度 。最大接收10M的字符数据
	private static final int MAX_FRAME_LENGTH = 1024 * 1024 * 10;
	//消息内容中length值的数据长度
    private static final int LENGTH_FIELD_LENGTH = 4;
    //消息内容中length值的起始位置
    private static final int LENGTH_FIELD_OFFSET = 2;
    //length代表的body区域是否需要偏移处理后读取数据。比如body长度是40，如果协议将length写成44，因为length本身还占有4个字节，这样就需要调整一下，那么就需要-4
    private static final int LENGTH_ADJUSTMENT = 0;
    private static final int INITIAL_BYTES_TO_STRIP = 0;

	//读操作空闲35分钟
	public final static int READER_IDLE_TIME_SECONDS = 35;

	@Autowired
	private ServerChannelHandler serverHandler;

	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
        //帧尾固定加BB，通过识别，自动进行数据分隔处理，解决TCP粘包
		ByteBuf charsetBuf = Unpooled.copiedBuffer(MessageBean.end.getBytes());

		pipeline.addLast(new DelimiterBasedFrameDecoder(MAX_FRAME_LENGTH, charsetBuf));
		//解包转换类
        pipeline.addLast("decoder", new MessageDecoder(MAX_FRAME_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_ADJUSTMENT, INITIAL_BYTES_TO_STRIP, false));
        //封包转换类
        pipeline.addLast("encoder", new MessageEncoder());
        //设置心跳检测。单位为分钟
		pipeline.addLast(new ReadTimeoutHandler(READER_IDLE_TIME_SECONDS, TimeUnit.MINUTES));

        pipeline.addLast(serverHandler);
	}
}
