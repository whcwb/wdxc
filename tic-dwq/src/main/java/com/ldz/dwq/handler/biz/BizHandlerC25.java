package com.ldz.dwq.handler.biz;

import com.ldz.dao.dwq.model.MileDwq;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BizHandlerC25 extends BizBaseHandler {

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        MessageBean messageBean = (MessageBean) msg;
        String[] dataArray = messageBean.getData().split(",");

        MileDwq dwq = new MileDwq();
        String deviceId = iotServer.getDeviceIdByChannel(ctx.channel());
        dwq.setDeviceId(deviceId);
        //接收终端时间是0时区的，需要先转换再存储
        String startTime  = DateTime.now().parse(dataArray[3], DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
        String endTime  = DateTime.now().parse(dataArray[4], DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
        dwq.setImei(dataArray[2]);
        dwq.setStartTime(startTime);
        dwq.setEndTime(endTime);
        dwq.setDeviceId(dwq.getImei());
        dwq.setMileage(dataArray[5]);

        accessLog.info("send mile dwq " + JsonUtil.toJson(dwq));
        redisTemplateUtil.convertAndSend("mile_dwq", JsonUtil.toJson(dwq));

        //读取数据成功后，向终端响应结果
        MessageBean sendData = new MessageBean();
        sendData.setCommand("S5");
        sendData.setImei(messageBean.getImei());
        //消息 ID,UTC,经度,纬度,时区。默认使用0时区，设备是使用的0时区
        String data = messageBean.getMid() + "," + DateTime.now().withZone(DateTimeZone.UTC).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss") + ",0,0,0";
        sendData.setData(data);
        iotServer.sendMsg(sendData);
    }
}

