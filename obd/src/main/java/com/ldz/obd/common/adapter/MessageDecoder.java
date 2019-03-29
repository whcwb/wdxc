package com.ldz.obd.common.adapter;


import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ldz.obd.common.bean.MessageBean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * 数据解码器，将接收到的字节数据，按照协议格式封装成bean结构
 * 报文协议，ASCII格式
 * 	内容		字节数		说明
 开始符	        1		固定为 0x28,即 ASCII 的‘(’符
 产品id	        6		设备的ID号,固定为6字节长度(即12个ASCII字符， BCD码表示，
                             2 位表示产品编号， 两位表示生产日期的年， 2013 年即用 13 表示，
                             4 位表示月日，还剩 4 位表示该日期生产的编号，从 0000-9999。
                             当然该 ID 也可以根据客户要求设置成其他形式，比如直接采用
                             SIM 卡号，但是长度必须不超过 12 位，且必须都是数

 命令字	        2		一个字节命令类型(表一)，一个字节表示命令编号，具体参见协议
 指令长度	    2		命令内容的长度(转义前
 命令内容	    N		1：下行指令第一个字节表示操作方式(设置为 0x01,查询为 0x00),
                         第二个字节开始为不定字节的参数，查询指令一般不带参数。
                         2：上行指令只包括不定字节的参数
 校验和	        1		从产品 ID 号到命令内容的所有字节的异或和(转义前)
 结束符	        1		固定为 0x29,即 ASCII 的”)”符.
 *
 */
public class MessageDecoder extends DelimiterBasedFrameDecoder {

    Logger accessLog = LoggerFactory.getLogger("access_info");

    /**
     *
     * @param maxFrameLength 解码时，处理每个帧数据的最大长度 
     */
    public MessageDecoder(int maxFrameLength, ByteBuf delimiter) {
    	super(maxFrameLength, delimiter);
	}

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in == null) {
            return null;
        }
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        String dataStr = ByteBufUtil.hexDump(bytes).toUpperCase();
        accessLog.info("通道["+ctx.channel().id().asShortText()+"]接收数据报文:"+dataStr);
        //对‘(’，‘)’以及 0x3D 这三个字符进行转义，转义方法为字符前加 0x3D,然后与 0x3D 进行异或，0x3D 本身也需要转义。如‘(’转义为 0x3D 0x15, ‘)’转义为 0X3D,0X14
        bytes = doEscape4Receive(bytes, 0, bytes.length);
        return new MessageBean(bytes);
    }
    
    /**
     * 接收消息时转义<br>
     * <p>
     * <pre>
     * 0x3d14 <====> 0x29
     * 0x3d15 <====> 0x28
     * </pre>
     *
     * @param bs    要转义的字节数组
     * @param start 起始索引
     * @param end   结束索引
     * @return 转义后的字节数组
     * @throws Exception
     */
    public byte[] doEscape4Receive(byte[] bs, int start, int end) throws Exception {
        if (start < 0 || end > bs.length)
            throw new ArrayIndexOutOfBoundsException("doEscape4Receive error : index out of bounds(start=" + start
                    + ",end=" + end + ",bytes length=" + bs.length + ")");
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            for (int i = 0; i < start; i++) {
                baos.write(bs[i]);
            }
            for (int i = start; i < end - 1; i++) {
                if (bs[i] == 0x3d && bs[i + 1] == 0x14) {
                    baos.write(0x29);
                    i++;
                } else if (bs[i] == 0x3d && bs[i + 1] == 0x15) {
                    baos.write(0x28);
                    i++;
                } else if (bs[i] == 0x3d && bs[i + 1] == 0x00) {
                    baos.write(0x3d);
                    i++;
                } else {
                    baos.write(bs[i]);
                }
            }
            for (int i = end - 1; i < bs.length; i++) {
                baos.write(bs[i]);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (baos != null) {
                baos.close();
                baos = null;
            }
        }
    }

}
