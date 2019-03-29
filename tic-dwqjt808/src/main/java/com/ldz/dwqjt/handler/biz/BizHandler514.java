package com.ldz.dwqjt.handler.biz;

import com.alibaba.fastjson.JSON;
import com.ldz.dwqjt.handler.ServerChannelHandler;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.jt808.vo.req.BatchLocationInfoUploadMsg;
import com.ldz.dwqjt.jt808.vo.req.LocationInfoUploadMsg;
import com.ldz.dwqjt.jt808.vo.resp.ServerCommonRespMsgBody;
import com.ldz.util.bean.RequestCommonParamsDto;
import io.netty.channel.ChannelHandlerContext;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 位置数据包：
 * // 7E 02 00 00 26 12 34 56 78 90 12 00 7D 02 00 00 00 01 00 00 00 02 00 BA 7F 0E 07 E4 F1 1C 00 28 00 3C 00 00 18 10 15 10 10 10 01 04 00 00 00 64 02 02 00 7D 01 13 7E
 *
 // 3. 位置信息汇报 ==> 平台通用应答
 */
@Component
public class BizHandler514 extends BizBaseHandler{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageData packageData = (PackageData) msg;
        final PackageData.MsgHeader header = packageData.getMsgHeader();
        accessLog.info(">>>>>[位置信息],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        try {
            BatchLocationInfoUploadMsg batchLocationInfoUploadMsg = msgDecoder.toBatchLocationInfoUploadMsg(packageData);
            accessLog.debug("位置 信息:{}", JSON.toJSONString(batchLocationInfoUploadMsg, true));
            List<RequestCommonParamsDto> dtoList = new ArrayList<>(batchLocationInfoUploadMsg.getGpsList().size());
            for (LocationInfoUploadMsg gps : batchLocationInfoUploadMsg.getGpsList()) {
                dtoList.add(parseDto(gps));
            }
            redisDao.convertAndSend("gpsList", dtoList);
            ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody(header.getFlowId(), header.getMsgId(), ServerCommonRespMsgBody.success);
            int flowId = super.getFlowId();
            byte[] bs = messageEncoder.encode4ServerCommonRespMsg(batchLocationInfoUploadMsg, respMsgBody, flowId);
            super.send2Client(batchLocationInfoUploadMsg.getChannel(), bs);
            accessLog.info("<<<<<[位置信息],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        } catch (Exception e) {
            errorLog.error("<<<<<[位置信息]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(), e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleGps(LocationInfoUploadMsg locationInfoUploadMsg){
        final PackageData.MsgHeader header = locationInfoUploadMsg.getMsgHeader();
        try {
            accessLog.debug("位置 信息:{}", JSON.toJSONString(locationInfoUploadMsg, true));
            //推送GPS数据到biz中处理
            RequestCommonParamsDto dto = new RequestCommonParamsDto();
            String time = new DateTime(locationInfoUploadMsg.getTime()).toString("yyyy-MM-dd HH:mm:ss");
            dto.setDeviceId(locationInfoUploadMsg.getMsgHeader().getTerminalPhone());
            dto.setLatitude(""+locationInfoUploadMsg.getLatitude());
            dto.setLongitude(""+locationInfoUploadMsg.getLongitude());
            dto.setStartTime(time);
            dto.setEndTime(time);
            dto.setFxj(""+locationInfoUploadMsg.getDirection());
            dto.setSpeed(locationInfoUploadMsg.getSpeed() == 0f ? "0" : ""+locationInfoUploadMsg.getSpeed());
            if (dto.getSpeed().contains(".")){
                dto.setSpeed(""+Math.round(new Float(dto.getSpeed())));
            }
            dto.setStarNum(""+locationInfoUploadMsg.getStarNum());
            /*//通道当前状态。
            Integer s = ctx.channel().attr(ServerChannelHandler.STATUS).get();
            if (statusField != s){
            	//处理点火熄火事件上报
            	if (statusField == 1){
            		eventType = "50";
            	}else{
            		eventType = "60";
            	}
            	ctx.channel().attr(ServerChannelHandler.STATUS).set(statusField);
            }*/
            //2019年1月25日。转换后的结果，高位在前，低位在后，需要做一个反转
            String alarmCode = StringUtils.rightPad(StringUtils.reverse(Integer.toBinaryString(locationInfoUploadMsg.getWarningFlagField())),32,'0');
            dto.setSczt("10");
            handleEvent(alarmCode,dto);
            accessLog.info("<<<<<[位置信息],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        } catch (Exception e) {
            errorLog.error("<<<<<[位置信息]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(), e.getMessage());
            e.printStackTrace();
        }
    }

    private RequestCommonParamsDto parseDto(LocationInfoUploadMsg locationInfoUploadMsg){
        RequestCommonParamsDto dto = new RequestCommonParamsDto();
        String time = new DateTime(locationInfoUploadMsg.getTime()).toString("yyyy-MM-dd HH:mm:ss");
        dto.setDeviceId(locationInfoUploadMsg.getMsgHeader().getTerminalPhone());
        dto.setLatitude(""+locationInfoUploadMsg.getLatitude());
        dto.setLongitude(""+locationInfoUploadMsg.getLongitude());
        dto.setStartTime(time);
        dto.setEndTime(time);
        dto.setFxj(""+locationInfoUploadMsg.getDirection());
        dto.setSpeed(locationInfoUploadMsg.getSpeed() == 0f ? "0" : ""+locationInfoUploadMsg.getSpeed());
        if (dto.getSpeed().contains(".")){
            dto.setSpeed(""+Math.round(new Float(dto.getSpeed())));
        }
        dto.setStarNum(""+locationInfoUploadMsg.getStarNum());
            /*//通道当前状态。
            Integer s = ctx.channel().attr(ServerChannelHandler.STATUS).get();
            if (statusField != s){
            	//处理点火熄火事件上报
            	if (statusField == 1){
            		eventType = "50";
            	}else{
            		eventType = "60";
            	}
            	ctx.channel().attr(ServerChannelHandler.STATUS).set(statusField);
            }*/
        //2019年1月25日。转换后的结果，高位在前，低位在后，需要做一个反转
        String alarmCode = StringUtils.rightPad(StringUtils.reverse(Integer.toBinaryString(locationInfoUploadMsg.getWarningFlagField())),32,'0');
        dto.setSczt("10");
        handleEvent(alarmCode,dto);
        return dto;
    }

    private void handleEvent(String s,RequestCommonParamsDto dto){
        Map<Integer,String> codeMap = new HashMap<>();
        codeMap.put(9,"10"); // 急加速
        codeMap.put(10,"20"); // 急减速
        codeMap.put(11,"31"); // 急左转弯
        codeMap.put(14,"32"); // 急右转弯
        codeMap.put(12,"33"); // 急左变道
        codeMap.put(15,"34"); // 急右变道

//        boolean hasEvent = false;
        for (int i = 0;i<s.length();i++){
            if (!codeMap.containsKey(i))continue;
            char c = s.charAt(i);
            if (c != '1')continue;
//            hasEvent = true;
            dto.setEventType(codeMap.get(i));
//            redisDao.convertAndSend("gps", dto);
        }
//        if (!hasEvent){
//            redisDao.convertAndSend("gps", dto);
//        }
    }


}
