package com.ldz.znzp.redis;


import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.redis.RedisTemplateUtil;
import com.ldz.znzp.bean.GpsInfo;
import com.ldz.znzp.model.ClCl;
import com.ldz.znzp.model.ClClyxjl;
import com.ldz.znzp.model.ClPb;
import com.ldz.znzp.model.ClXl;
import com.ldz.znzp.service.*;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class MessageReceiver implements MessageListener {

	@Autowired
	private ClService clService;

	@Autowired
	private XlService xlService;
	@Autowired
	private ClyxjlService clyxjlService;

	private RedisTemplateUtil redisTemplate;

	public MessageReceiver(RedisTemplateUtil redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		val topic = redisTemplate.getStringSerializer().deserialize(message.getChannel());
		val eventMessage = redisTemplate.getValueSerializer().deserialize(message.getBody());
		System.out.println(eventMessage);
		//String topic = Arrays.toString(pattern);
        GpsInfo gpsInfo = new GpsInfo();
        RequestCommonParamsDto dto = (RequestCommonParamsDto) eventMessage;
        BeanUtils.copyProperties(dto,gpsInfo);
		switch (topic){
			case "gps":
				if (StringUtils.isEmpty(gpsInfo.getDeviceId())){
					return ;
				}
				if (StringUtils.isEmpty(gpsInfo.getLongitude())){
					return ;
				}
				if (StringUtils.isEmpty(gpsInfo.getLatitude())){
					return ;
				}

				ClCl car = clService.getByDeviceId(gpsInfo.getDeviceId());
				if (car == null)return  ;

				List<ClClyxjl> clClyxjls = clyxjlService.findEq(ClClyxjl.InnerColumn.clId,car.getClId());

				ClPb pb = clService.getCarPb(car.getClId());
				if (pb == null)return ;

				ClXl route = xlService.findById(pb.getXlId());
				if (route == null)return ;

				ClClyxjl clClyxjl = clClyxjls.size() == 0 ? null : clClyxjls.get(0);

				ApiResponse<String> res = clService.updateGps(gpsInfo,pb,car,route,clClyxjl);
				if (!res.isSuccess())return ;
				clService.report(gpsInfo.getDeviceId(),pb,car,route,clClyxjl);
				break;
		}
		System.out.println("收到一条消息："+topic);
	}
}
