package com.ldz.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.service.biz.interfaces.GpsLsService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;

@RestController
@RequestMapping("api/clgpsls")
public class ClGpsLsCtrl extends BaseController<ClGpsLs, String> {

	@Autowired
	private GpsLsService gpslsservice;

	@Override
	protected BaseService<ClGpsLs, String> getBaseService() {
		return gpslsservice;
	}

}
