package com.ldz.biz.controller;

import com.ldz.dao.biz.bean.ObdMessageBean;
import com.ldz.service.biz.interfaces.ObdService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * OBD方法
 * todo 这里是测试的，真实路径是要加api/这个目录的。
 */
@RestController
@RequestMapping("obd")
public class ObdCtrl extends BaseController<ObdMessageBean, String> {
	 @Autowired
	 private ObdService obdservice;

	@Override
	protected BaseService<ObdMessageBean, String> getBaseService() {
		return obdservice;
	}

	@RequestMapping(value = "/getObdTimely", method = { RequestMethod.POST })
	public ApiResponse<Object> getObdTimely(String obdId) {
		return obdservice.getObdTimely(obdId);
	}
}
