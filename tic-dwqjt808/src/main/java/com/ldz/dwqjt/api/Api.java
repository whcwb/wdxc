package com.ldz.dwqjt.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dwqjt.server.IotServer;

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
}
