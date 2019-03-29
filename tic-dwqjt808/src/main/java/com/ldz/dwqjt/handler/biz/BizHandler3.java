package com.ldz.dwqjt.handler.biz;

import com.alibaba.fastjson.JSON;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.jt808.vo.resp.ServerCommonRespMsgBody;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 // 7. 终端注销(终端注销数据消息体为空) ==> 平台通用应答
 */
@Component
public class BizHandler3 extends BizBaseHandler{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageData packageData = (PackageData) msg;
        final PackageData.MsgHeader header = packageData.getMsgHeader();
        accessLog.info(">>>>>[终端注销],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        try {
            final PackageData.MsgHeader reqHeader = packageData.getMsgHeader();
            ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody(reqHeader.getFlowId(), reqHeader.getMsgId(), ServerCommonRespMsgBody.success);
            int flowId = super.getFlowId();
            iotServer.online(packageData.getChannel(),packageData.getMsgHeader().getTerminalPhone());
            byte[] bs = messageEncoder.encode4ServerCommonRespMsg(packageData, respMsgBody, flowId);
            super.send2Client(packageData.getChannel(), bs);
            accessLog.info("<<<<<[终端注销],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        } catch (Exception e) {
            errorLog.error("<<<<<[终端注销]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(), e.getMessage());
            e.printStackTrace();
        }
    }
}
