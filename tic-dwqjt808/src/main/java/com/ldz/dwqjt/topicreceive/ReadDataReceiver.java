package com.ldz.dwqjt.topicreceive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.ldz.dwqjt.server.IotServer;
import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.util.spring.SpringContextUtil;

import lombok.val;


@SuppressWarnings("unused")
public class ReadDataReceiver implements MessageListener {
	
	Logger errorLog = LoggerFactory.getLogger("error_info");

	private RedisTemplateUtil redisTemplate;
	
	private IotServer iotServer;
	
	public ReadDataReceiver() {
		this.redisTemplate = SpringContextUtil.getBean(RedisTemplateUtil.class); 
		this.iotServer = SpringContextUtil.getBean(IotServer.class);
	}
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		//接收到的终端数据
		val eventMessage = redisTemplate.getValueSerializer().deserialize(message.getBody());
		
	}
}
