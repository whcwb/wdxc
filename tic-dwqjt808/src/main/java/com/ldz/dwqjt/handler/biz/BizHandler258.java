package com.ldz.dwqjt.handler.biz;

import com.alibaba.fastjson.JSON;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.jt808.vo.resp.DeviceParamMsg;
import com.ldz.dwqjt.jt808.vo.req.TerminalAuthenticationMsg;
import com.ldz.dwqjt.jt808.vo.resp.ServerCommonRespMsgBody;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 // 5. 终端鉴权 ==> 平台通用应答
 */
@Component
public class BizHandler258 extends BizBaseHandler{

    @Autowired
    private  DeviceParamMsg paramMsg;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageData packageData = (PackageData) msg;
        final PackageData.MsgHeader header = packageData.getMsgHeader();
        accessLog.info(">>>>>[终端鉴权],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        try {
            TerminalAuthenticationMsg authenticationMsg = new TerminalAuthenticationMsg(packageData);

            accessLog.debug("终端鉴权:{}", JSON.toJSONString(msg, true));

            ServerCommonRespMsgBody respMsgBody = new ServerCommonRespMsgBody();

            String authCode = iotServer.getAuthCodeByPhone(authenticationMsg.getMsgHeader().getTerminalPhone());
            if (authCode == null || !authCode.equals(authenticationMsg.getAuthCode())){
//                respMsgBody.setReplyCode(ServerCommonRespMsgBody.failure);
                respMsgBody.setReplyCode(ServerCommonRespMsgBody.success);
            }else{
                respMsgBody.setReplyCode(ServerCommonRespMsgBody.success);
            }
            respMsgBody.setReplyFlowId(authenticationMsg.getMsgHeader().getFlowId());
            respMsgBody.setReplyId(authenticationMsg.getMsgHeader().getMsgId());
            int flowId = super.getFlowId();
            byte[] bs = messageEncoder.encode4ServerCommonRespMsg(authenticationMsg, respMsgBody, flowId);
            super.send2Client(authenticationMsg.getChannel(), bs);


//            Thread.sleep(1000);
//            // 发送终端参数
//            paramMsg.setReplyFlowId(authenticationMsg.getMsgHeader().getFlowId());
//            byte[] bytes = messageEncoder.encode4DeviceParamRespMsg(authenticationMsg, paramMsg, flowId);
//            super.send2Client(authenticationMsg.getChannel(),bytes);
//
//            Thread.sleep(1000);
//            byte[] querySetBytes = messageEncoder.encode4QueryDeviceParamRespMsg(authenticationMsg, flowId);
//            super.send2Client(authenticationMsg.getChannel(),querySetBytes);


            accessLog.info("<<<<<[终端鉴权],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        } catch (Exception e) {
            errorLog.error("<<<<<[终端鉴权]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(), e.getMessage());
            e.printStackTrace();
        }
    }
}
