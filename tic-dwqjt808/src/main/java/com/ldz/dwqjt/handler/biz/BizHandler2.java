package com.ldz.dwqjt.handler.biz;

import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.jt808.vo.resp.ServerCommonRespMsgBody;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 *
 // 1. 终端心跳-消息体为空 ==> 平台通用应答
 */
@Component
public class BizHandler2  extends BizBaseHandler{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageData packageData = (PackageData) msg;
        final PackageData.MsgHeader header = packageData.getMsgHeader();
        accessLog.info(">>>>>[终端心跳],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        try {
            final PackageData.MsgHeader reqHeader = packageData.getMsgHeader();
            ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody(reqHeader.getFlowId(), reqHeader.getMsgId(), ServerCommonRespMsgBody.success);
            int flowId = super.getFlowId();
            byte[] bs = messageEncoder.encode4ServerCommonRespMsg(packageData, respMsgBody, flowId);
            super.send2Client(packageData.getChannel(), bs);
            accessLog.info("<<<<<[终端心跳],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        } catch (Exception e) {
            errorLog.error("<<<<<[终端心跳]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(), e.getMessage());
            e.printStackTrace();
        }
    }
}
