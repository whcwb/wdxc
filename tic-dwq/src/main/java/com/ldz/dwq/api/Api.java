package com.ldz.dwq.api;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dwq.common.bean.ApiResponse;
import com.ldz.dwq.common.bean.CommandType;
import com.ldz.dwq.common.bean.MessageBean;
import com.ldz.dwq.server.IotServer;

/**
 * 协议对外接口提供
 * 用于其他平台进行设备的指令操作
 * @author Lee
 *
 */
@RestController
@RequestMapping("/api")
public class Api {

	@Autowired
	private IotServer iotServer;

	Logger errorLog = LoggerFactory.getLogger("error_info");


	/**
	 * 平台下发关机命令(S9,C5)
	 * @return
	 */
	@RequestMapping("s8")
	public ApiResponse<String> s8(String deviceId){
		MessageBean bean = new MessageBean();
		bean.setImei(deviceId);
		bean.setCommand("S8");
		bean.setMid("0");
		iotServer.sendMsg(bean);
		return ApiResponse.success();
	}

	/**
	 * 平台下发定位请求(S8,C5)
	 * @return
	 */
	@RequestMapping("s9")
	public ApiResponse<String> s9(String deviceId){
		MessageBean bean = new MessageBean();
		bean.setImei(deviceId);
		bean.setCommand("S9");
		bean.setMid("0");
		iotServer.sendMsg(bean);
		return ApiResponse.success();
	}

	/**
	 * 平台下发复位命令(S10,C5)
	 * @return
	 */
	@RequestMapping("s10")
	public ApiResponse<String> s10(String deviceId){
		MessageBean bean = new MessageBean();
		bean.setImei(deviceId);
		bean.setCommand("S10");
		bean.setMid("0");
		iotServer.sendMsg(bean);
		return ApiResponse.success();
	}
	/**
	 * 写继电器打开持续时间命令
	 * 该接口暂时是单独向一个继电器写配置信息
	 * @param facId		通道ID
	 * @param jdqIndex	继电器通道编号。一共1-16个通道
	 * @param time		持续开启时间
	 * @return
	 */
	@GetMapping(value="/setJdqTimeConfig/{facId}/{jdqIndex}/{time}")
	public ApiResponse<String> jdqTimeConfig(@PathVariable("facId") String facId, @PathVariable("jdqIndex") Integer jdqIndex, @PathVariable("time") Integer time) {
		MessageBean message = new MessageBean();


		return ApiResponse.success("写入操作完成！");
	}

	/**
	 * 写继电器控制命令
	 * 该接口暂时是单独向一个继电器写配置信息
	 * @param facId		通道ID
	 * @param jdqIndex	继电器通道编号。一共1-16个通道
	 * @param type		控制类型。01：打开继电器；00：关闭继电器
	 * @return
	 */
	@GetMapping(value="/setJdqConfig/{facId}/{jdqIndex}/{type}")
	public ApiResponse<String> jdqConfig(@PathVariable("facId") String facId, @PathVariable("jdqIndex") Integer jdqIndex, @PathVariable("type") String type) {
		MessageBean message = new MessageBean();


		return ApiResponse.success("写入操作完成！");
	}
}
