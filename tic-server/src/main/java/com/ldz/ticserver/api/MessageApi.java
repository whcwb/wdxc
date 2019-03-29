package com.ldz.ticserver.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.JsonUtil;

/**
 * 其他服务平台，向车载终端发送指令请求时，通过该接口对外提供设备操作功能
 * @author Lee
 *
 */
@RestController
@RequestMapping("/api/msg")
public class MessageApi {
	
	//@Autowired
	private RestTemplate restTemplate;
	@RequestMapping("/inx")
	public ApiResponse<String> index(){
		ApiResponse<String> s = new ApiResponse<>();
		//post json string data
		//return string
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		RequestCommonParamsDto dto = new RequestCommonParamsDto();
		dto.setDeviceId("865923030039405");
		dto.setChannelId("865923030039405");
		dto.setCmdType("01");
		dto.setCmd("120");
		
		Map<String,String> map = new HashMap<>();
		map.put("deviceId", "865923030039405");
		map.put("cmdType", "01");
		map.put("cmd", "120");
		/*
		GpsInfo j = new GpsInfo();
		j.setDeviceId("865923030039405");
		j.setChannelId("865923030039405");
		j.setCmdType("01");
		j.setCmd("120");
		*/
		
		//HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(dto), headers);
		HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(map), headers);
		//HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJson(j), headers);
		String result = restTemplate.postForObject("http://weicy.natapp1.cc/api/push/carcmd", formEntity, String.class);
		System.err.println("xxxxx"+result);
		return s;
	}

	
}
