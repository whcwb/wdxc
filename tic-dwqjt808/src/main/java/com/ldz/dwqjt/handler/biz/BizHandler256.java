package com.ldz.dwqjt.handler.biz;

import com.alibaba.fastjson.JSON;
import com.ldz.dwqjt.jt808.service.codec.MsgDecoder;
import com.ldz.dwqjt.jt808.vo.PackageData;
import com.ldz.dwqjt.jt808.vo.req.TerminalAuthenticationMsg;
import com.ldz.dwqjt.jt808.vo.req.TerminalRegisterMsg;
import com.ldz.dwqjt.jt808.vo.resp.TerminalRegisterMsgRespBody;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 // 6. 终端注册 ==> 终端注册应答
 */
@Component
public class BizHandler256 extends BizBaseHandler{

    private static final String DEVICEID = "DEVICEID-";
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PackageData packageData = (PackageData) msg;
        final PackageData.MsgHeader header = packageData.getMsgHeader();
        accessLog.info(">>>>>[终端注册],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        try {
            TerminalRegisterMsg terminalRegisterMsg = msgDecoder.toTerminalRegisterMsg(packageData);

            accessLog.debug("终端注册:{}", JSON.toJSONString(msg, true));

            iotServer.putPhoneDevice(terminalRegisterMsg.getMsgHeader().getTerminalPhone(),terminalRegisterMsg.getTerminalRegInfo().getTerminalId());
            // 检查终端编号是否存在
            TerminalRegisterMsgRespBody respMsgBody = new TerminalRegisterMsgRespBody();
            if (!redisDao.hasKey(DEVICEID+terminalRegisterMsg.getMsgHeader().getTerminalPhone())){
                errorLog.error("终端不存在："+terminalRegisterMsg.getMsgHeader().getTerminalPhone());
                respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.success);
//                respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.terminal_not_found);
            }else{
                respMsgBody.setReplyCode(TerminalRegisterMsgRespBody.success);
            }
            respMsgBody.setReplyFlowId(terminalRegisterMsg.getMsgHeader().getFlowId());
            String chars = "qwertyuiopasdfghjklzxcvbnmZXCVBNMASDFGHJKLQEWRTYUIOP";
            String replyToken = RandomStringUtils.random(16,chars);
            respMsgBody.setReplyToken(replyToken);
            iotServer.putAuthCode(terminalRegisterMsg.getMsgHeader().getTerminalPhone(),replyToken);
            int flowId = super.getFlowId();
            byte[] bs = messageEncoder.encode4TerminalRegisterResp(terminalRegisterMsg, respMsgBody, flowId);
            super.send2Client(terminalRegisterMsg.getChannel(), bs);
            accessLog.info("<<<<<[终端注册],phone={},flowid={}", header.getTerminalPhone(), header.getFlowId());
        } catch (Exception e) {
            errorLog.error("<<<<<[终端注册]处理错误,phone={},flowid={},err={}", header.getTerminalPhone(), header.getFlowId(), e.getMessage());
            e.printStackTrace();
        }
    }
}
