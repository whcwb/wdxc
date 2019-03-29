package com.ldz.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysHsgs;
import com.ldz.sys.service.SysHsgsService;

@RestController
@RequestMapping("/api/hsgs")
public class SysHsgsCtrl extends BaseController<SysHsgs, String> {
    @Autowired
    private SysHsgsService hsgsservice;
	
	@Override
	protected BaseService<SysHsgs, String> getBaseService() {
		return hsgsservice;
	}

}
