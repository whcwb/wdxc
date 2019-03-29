package com.ldz.ticserver.service;

import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service("bizApiService")
public class BizApiServiceImpl implements BizApiService{

	Logger accessLog = LoggerFactory.getLogger("access_info");

	Logger errorLog = LoggerFactory.getLogger("error_info");

	//设备状态更新接口
	@Value("${interface.status}")
	private String statusApi;
	//设备事件更新接口
	@Value("${interface.event}")
	private String eventApi;
	//设备GPS上报处理接口
	@Value("${interface.gps}")
	private String gpsApi;
	//文件上传的数据交互接口
	@Value("${interface.fileapi}")
	private String fileApi;

	@Autowired
	private RedisTemplateUtil redisTemplate;



	@Autowired
	private Executor executor;

	//测试上传设备状态接口
	@Override
	public void pushData(RequestCommonParamsDto dto){
		pushDataApi(eventApi,dto);
	}

	@Override
	public void pushFileData(RequestCommonParamsDto dto) {
		// TODO Auto-generated method stub
		pushDataApi(fileApi,dto);
	}

	private void pushDataApi(String serverUrl,RequestCommonParamsDto dto){
		if (StringUtils.isNotEmpty(serverUrl)){
			errorLog.error("dto:"+dto.toString());
			String topic = serverUrl.equals(fileApi) ? "spk" : "gps";
			redisTemplate.convertAndSend(topic, dto);
			//2018年11月2日。新增终端的原始数据存储，包含推送的channelid值，用于发送指令时匹配CID值
			redisTemplate.boundValueOps(RequestCommonParamsDto.class.getSimpleName()+"-"+dto.getDeviceId()).set(dto);
		}
	}

}
