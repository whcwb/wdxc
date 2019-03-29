package com.ldz.dwqjt.handler.biz;

import com.alibaba.fastjson.JSON;
import com.ldz.dao.dwq.model.TravelData;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 行程数据包：
 * // 7E 02 00 00 26 12 34 56 78 90 12 00 7D 02 00 00 00 01 00 00 00 02 00 BA 7F 0E 07 E4 F1 1C 00 28 00 3C 00 00 18 10 15 10 10 10 01 04 00 00 00 64 02 02 00 7D 01 13 7E
 *
 // 3. 行程信息汇报 ==> 平台通用应答
 */
@Component
public class BizHandler515 extends BizBaseHandler{
    @Autowired
    private RedisTemplateUtil redisTemplate;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageData packageData = (PackageData) msg;
        final PackageData.MsgHeader header = packageData.getMsgHeader();
        accessLog.info(">>>>>[行程数据包],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        try {
            TravelData travelData = this.msgDecoder.toTravelDataReq(packageData);
            accessLog.info("反解析对象结果："+travelData.toString());
            redisTemplate.opsForList().leftPushAll("travelData", travelData);
            accessLog.info("save travelData to redis");
            redisTemplate.convertAndSend("travelData", JsonUtil.toJson(travelData));
            // 发送点火熄火数据包

//            String sTime = travelData.getStartTime();
//            String eTime = travelData.getEndTime();

//            RequestCommonParamsDto dto1 = new RequestCommonParamsDto();
//            dto1.setDeviceId(travelData.getDeviceId());
//            dto1.setLatitude("-1");
//            dto1.setLongitude("-1");
//            dto1.setStartTime(sTime);
//            dto1.setEndTime(sTime);
//            dto1.setFxj("0");
//            dto1.setSpeed("0");
//            dto1.setStarNum("0");
//            dto1.setEventType("50");
//            dto1.setSczt("10");
//            redisDao.convertAndSend("gps", dto1);
//            accessLog.info("发送点火数据包："+dto1.toString());
//
//            RequestCommonParamsDto dto2 = new RequestCommonParamsDto();
//            dto2.setDeviceId(travelData.getDeviceId());
//            dto2.setLatitude("-1");
//            dto2.setLongitude("-1");
//            dto2.setStartTime(eTime);
//            dto2.setEndTime(eTime);
//            dto2.setFxj("0");
//            dto2.setSpeed("0");
//            dto2.setStarNum("0");
//            dto2.setEventType("60");
//            dto2.setSczt("10");
//            redisDao.convertAndSend("gps", dto2);
//            accessLog.info("发送熄火数据包："+dto2.toString());

            accessLog.debug("行程数据包:{}", JSON.toJSONString(travelData, true));
        } catch (Exception e) {
            errorLog.error("<<<<<[行程数据包]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(), e.getMessage());
            e.printStackTrace();
        }
    }
}
