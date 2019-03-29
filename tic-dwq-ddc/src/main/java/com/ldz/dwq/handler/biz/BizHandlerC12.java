package com.ldz.dwq.handler.biz;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.ldz.dwq.bean.DeviceBean;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.handler.ServerChannelInitializer;
import com.ldz.dwq.server.IotServer;
import com.ldz.util.bean.RequestCommonParamsDto;

import io.netty.channel.ChannelHandlerContext;

/**
 * 终端上报异常信息命令解析
 * @author Lee
 *
 */
@Component
public class BizHandlerC12 extends BizBaseHandler {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MessageBean messageBean = (MessageBean)msg;
		
		String[] dataArray = messageBean.getData().split(",");
		//设备功能。需转换为二进制（从低到高）
		/**
			0 GPS 功能 0 为正常，1 为异常
			1 WIFI 功能 0 为正常，1 为异常
			2 G-sensor 0 为正常，1 为异常
		 */
		String sbgn = dataArray[dataArray.length - 1];
		String bSbgn = Integer.toBinaryString(Integer.parseInt(sbgn));
		bSbgn = StringUtils.leftPad(bSbgn, 3, "0");
		bSbgn = StringUtils.reverse(bSbgn);
		if ("000".equals(bSbgn)){
			//表示数据都正常
			return;
		}
		//把异常数据当场事件进行记录
		String[] datas = bSbgn.split("|");
		for (int i=0; i<datas.length; i++){
			if ("1".equals(datas[i])){
				RequestCommonParamsDto requestBean = new RequestCommonParamsDto();
				requestBean.setDeviceId(messageBean.getImei());
				requestBean.setStartTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
				if (i == 0){
					//GPS异常
					requestBean.setEventType("DE00");	
				}else if (i == 1){
					//WIFI异常
					requestBean.setEventType("DE01");
				}else if (i == 2){
					//G-sensor异常
					requestBean.setEventType("DE02");
				}
			}
		}
		
		//读取数据成功后，向终端响应结果
		sendCommonMsg(messageBean);
	}
}
