package com.ldz.obd.api;

import com.ldz.obd.common.bean.ApiResponse;
import com.ldz.obd.common.bean.MessageBean;
import com.ldz.obd.service.ApiService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.obd.server.IotServer;

/**
 * 协议对外接口提供
 * 用于其他平台进行设备的指令操作
 * @author Lee
 *
 */
@RestController
@RequestMapping("/api")
public class ApiCtrl {
	@Autowired
	private ApiService apiService;

	Logger errorLog = LoggerFactory.getLogger("error_info");

	/**
	 * 4.3.1 设置/查询 GPS 位置信息上传通道和间隔
	 * 设置： 0x01 0x00 0x00 0x1E 0x00 0x00 第一个 0x01 表示设置。第二个字节表示上传通
	 * 道，0x00 表示 GPRS 通道上传，0x01 表示短信通道上传，第三和第四个字节表示点火上传
	 * 的间隔，单位 S，0x00 0x1E 上传间隔为 30S，取值 5 – 1000S。设置为 0 表示不上传，默
	 * 认 15S。第五和第六个字节表示熄火上传的间隔，单位 S，0x00 0x00 上传间隔为 0S，取值
	 * 5 – 1000S。设置为 0 表示不上传，默认 0S。
	 * 查询：0x00 表示查询
	 * @param flamein
	 * @param flameout
	 * @return
	 */
	@RequestMapping("2001")
	public ApiResponse<String> c2001(String deviceId,Integer flamein, Integer flameout){
		String content = "0100";
		content += StringUtils.leftPad(Integer.toHexString(flamein),4,"0")
				+ StringUtils.leftPad(Integer.toHexString(flameout),4,"0");
		MessageBean bean = new MessageBean(deviceId,"2001",content);
		System.out.println(bean.getMessage());
		return apiService.send(bean);
	}

	/**
	 * 4.3.3 设置/查询 GPS+OBD 混合信息上传间隔
	 * @return
	 */
	@RequestMapping("2003")
	public ApiResponse<String> c2003(String deviceId,Integer interval){
		String content = "01";
		content += StringUtils.leftPad(Integer.toHexString(interval),4,"0");
		MessageBean bean = new MessageBean(deviceId,"2003",content);
		System.out.println(bean.getMessage());
		return apiService.send(bean);
	}

	/**
	 * 4.4.2 清除发动机系统的故障码
	 * @return
	 */
	@RequestMapping("3006")
	public ApiResponse<String> c3006(String deviceId){
		String content = "01";
		MessageBean bean = new MessageBean(deviceId,"3006",content);
		System.out.println(bean.getMessage());
		return apiService.send(bean);
	}

	/**
	 * 4.4.3 查询发动机系统的故障码
	 * @return
	 */
	@RequestMapping("3007")
	public ApiResponse<String> c3007(String deviceId){
		String content = "00";
		MessageBean bean = new MessageBean(deviceId,"3007",content);
		return apiService.send(bean);
	}

	/**
	 * 查询 油量
	 * @return
	 */
	@RequestMapping("001D00")
	public ApiResponse<String> c001D00(String deviceId){
		String content = "00";
		MessageBean bean = new MessageBean(deviceId,"001D",content);
		System.out.println(bean.getMessage());
		return apiService.send(bean);
	}
	/**
	 * 标定 油量
	 * @return
	 */
	@RequestMapping("001D01")
	public ApiResponse<String> c001D01(String deviceId,Integer number){
		String content = "01";
		content += StringUtils.leftPad(Integer.toHexString(number),8,"0");
		MessageBean bean = new MessageBean(deviceId,"001D",content);
		System.out.println(bean.getMessage());
		return apiService.send(bean);
	}
	/**
	 * 4.1.3 恢复出厂设置
	 * @return
	 */
	@RequestMapping("0004")
	public ApiResponse<String> c0004(String deviceId){
		String content = "01";
		MessageBean bean = new MessageBean(deviceId,"0004",content);
		System.out.println(bean.getMessage());
		return apiService.send(bean);
	}
	/**
	 * 4.1.4 请求设备重启
	 * @return
	 */
	@RequestMapping("0005")
	public ApiResponse<String> c0005(String deviceId){
		String content = "01";
		MessageBean bean = new MessageBean(deviceId,"0005",content);
		System.out.println(bean.getMessage());
		return apiService.send(bean);
	}
	/**
	 * 4.1.5 设备报警/收到报警确认
	 * @return
	 */
	@RequestMapping("0008")
	public ApiResponse<String> c0008(String deviceId,String alarmCode){
		String content = "01";
		content += StringUtils.leftPad(alarmCode,2,"0");
		MessageBean bean = new MessageBean(deviceId,"0008",content);
		return apiService.send(bean);
	}
}
