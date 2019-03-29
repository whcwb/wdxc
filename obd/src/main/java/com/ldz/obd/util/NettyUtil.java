package com.ldz.obd.util;

import com.ldz.obd.common.bean.ApiResponse;
import com.ldz.obd.common.bean.MessageBean;
import com.ldz.obd.exception.BizException;
import com.ldz.obd.handler.ServerChannelHandler;
import com.ldz.obd.server.IotServer;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class NettyUtil {
    @Autowired
    private IotServer iotServer;
    @Autowired
    private StringRedisTemplate stringRedis;

    public final static String ONLINE_KEY = "ONLINE";

    /**
     * 通道channel ID获取在线终端ID
     * @param cid
     * @return
     */
    public String getDevicedByChannelId(String cid){
        String tid = null;
        //主动移除终端在线状态.Redis
        Iterator<String> keys = stringRedis.keys("*-"+cid+"-"+ONLINE_KEY).iterator();
        while(keys.hasNext()){
            String key = keys.next();
            tid = key.split("-")[0];
            break;
        }

        return tid;
    }

    public ApiResponse<String> sendMessage(MessageBean bean){
        Channel channel = findByDeviceId(bean.getDeviceId());
        if (channel == null){
            return ApiResponse.fail("终端不在线");
        }
        channel.writeAndFlush(bean.getMessage());
        return ApiResponse.success();
    }
    public Channel findByDeviceId(String deviceId){
        for (Channel channel : IotServer.onlineChannels) {
            if (channel.attr(ServerChannelHandler.DEVICENO).equals(deviceId)){
                return channel;
            }
        }
        return null;
    }

}
