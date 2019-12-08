package com.ldz.dwq.handler.biz;

import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.handler.ServerChannelHandler;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通用回复
 [C5,11,866401020000010,S11,1]
 *
 */
@Component
@SuppressWarnings("static-access")
public class BizHandlerC5 extends BizBaseHandler {
    Logger accessLog = LoggerFactory.getLogger("access_info");

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		String[] dataArray = messageBean.getData().split(",");
        accessLog.info("dataArray -- > {}"  , JsonUtil.toJson(dataArray));


	}
}
