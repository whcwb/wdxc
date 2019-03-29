package com.ldz.dwqjt.handler.biz;

import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.jt808.vo.req.EventMsg;
import com.ldz.dwqjt.jt808.vo.resp.DeviceParamMsg;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.JsonUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 // 5. 终端鉴权 ==> 平台通用应答
 */
@Component
public class BizHandler259 extends BizBaseHandler{

    @Autowired
    private  DeviceParamMsg paramMsg;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageData packageData = (PackageData) msg;
        final PackageData.MsgHeader header = packageData.getMsgHeader();
        accessLog.info(">>>>>[终端事件上传],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        try {

            EventMsg eventMsg = msgDecoder.toEventMsg(packageData);
            String sTime = DateUtils.getNowTime();

            RequestCommonParamsDto dto = new RequestCommonParamsDto();
            dto.setDeviceId(header.getTerminalPhone());
            dto.setLatitude("-1");
            dto.setLongitude("-1");
            dto.setStartTime(sTime);
            dto.setEndTime(sTime);
            dto.setFxj("0");
            dto.setSpeed("0");
            dto.setStarNum("0");
            dto.setSczt("10");
            if(eventMsg.getEventType() == 2){
                dto.setEventType("50");
                redisDao.convertAndSend("gps",dto);
                accessLog.info("<<<<<[终端事件上传成功], 事件内容 ： {} ", JsonUtil.toJson(dto));
            }else if (eventMsg.getEventType() == 4){
                dto.setEventType("60");
                redisDao.convertAndSend("gps",dto);
                accessLog.info("<<<<<[终端事件上传成功], 事件内容 ： {} ", JsonUtil.toJson(dto));
            }else{
                accessLog.info("<<<<<[终端事件上传转换异常],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
            }

            accessLog.info("<<<<<[终端事件上传],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        } catch (Exception e) {
            errorLog.error("<<<<<[终端事件上传]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(), e.getMessage());
            e.printStackTrace();
        }
    }
}
