package com.ldz.wechat.handler;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ldz.util.commonUtil.JsonUtil;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.WxCpXmlOutMessage;

/**
 * * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class LogHandler extends AbstractHandler {
  @Override
  public WxCpXmlOutMessage handle(WxCpXmlMessage wxMessage,
                                  Map<String, Object> context, WxCpService WxCpService,
                                  WxSessionManager sessionManager) {
    this.logger.info("\n接收到请求消息，内容：{}", JsonUtil.toJson(wxMessage));
    return null;
  }

}
