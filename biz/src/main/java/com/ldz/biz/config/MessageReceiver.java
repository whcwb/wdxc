package com.ldz.biz.config;


import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.dao.dwq.mapper.DwqGpsMapper;
import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.service.biz.interfaces.GpsService;
import com.ldz.service.biz.interfaces.SpkService;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.redis.RedisTemplateUtil;
import lombok.val;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.ArrayList;
import java.util.List;


public class MessageReceiver  implements MessageListener {

	Logger errorLog = LoggerFactory.getLogger("error_info");
	private SpkService spkService;

    private GpsService gpsservice;

	private RedisTemplateUtil redisTemplate;

	private  DwqGpsMapper dwqGpsMapper;
	public MessageReceiver(SpkService spkService, GpsService gpsservice, RedisTemplateUtil redisTemplate, DwqGpsMapper dwqGpsMapper) {
		this.spkService = spkService;
		this.gpsservice = gpsservice;
		this.redisTemplate = redisTemplate;
		this.dwqGpsMapper = dwqGpsMapper;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		val redisChannel = redisTemplate.getStringSerializer().deserialize(message.getChannel());
		val eventMessage = redisTemplate.getValueSerializer().deserialize(message.getBody());
		String topic = redisChannel;
		// String topic = Arrays.toString(pattern);

		switch (topic){
			case "spk":
				GpsInfo gpsInfo1 = new GpsInfo();
				RequestCommonParamsDto dto1 = (RequestCommonParamsDto) eventMessage;
				errorLog.error("Redis接收数据:"+dto1.toString());
				BeanUtils.copyProperties(dto1,gpsInfo1);
				gpsInfo1.setSpeed(Integer.parseInt(dto1.getSpeed()));
				spkService.saveSpk(gpsInfo1);
				break;
			case "gps":
				GpsInfo gpsInfo = new GpsInfo();
				RequestCommonParamsDto dto = (RequestCommonParamsDto) eventMessage;
				errorLog.error("Redis接收数据:"+dto.toString());
				BeanUtils.copyProperties(dto,gpsInfo);
				gpsInfo.setSpeed(Integer.parseInt(dto.getSpeed()));
                gpsservice.onReceiveGps(gpsInfo);
				break;
			case "gpsList":
				List<GpsInfo> gpsInfos = new ArrayList<>();
				List<RequestCommonParamsDto> dto2 = (List) eventMessage;
				for (RequestCommonParamsDto paramsDto : dto2) {
					GpsInfo gpsInfo2 = new GpsInfo();
					BeanUtils.copyProperties(paramsDto,gpsInfo2);
					gpsInfo2.setSpeed(Integer.parseInt(paramsDto.getSpeed()));
					gpsInfos.add(gpsInfo2);
				}
				gpsservice.onReceiveGpsList(gpsInfos);
				break;
			case "dwq_temperature":
				GpsBean dtot = (GpsBean) eventMessage;
				SimpleCondition condition = new SimpleCondition(GpsBean.class);
				condition.eq("device_id",dtot.getDeviceId());
				condition.setOrderByClause("time desc");
				List<GpsBean> gpsLs2 = dwqGpsMapper.selectByExampleAndRowBounds(condition,new RowBounds(0,1));
				if (gpsLs2.size() != 0){
					gpsLs2.get(0).setTempurature(dtot.getTempurature());
					dwqGpsMapper.updateByPrimaryKeySelective(gpsLs2.get(0));
				}

				break;
		}
		System.out.println("收到一条消息："+redisChannel);
	}

}
