package com.ldz.biz.controller;

import com.ldz.dao.biz.model.ClClyxjl;
import com.ldz.service.biz.interfaces.ClyxjlService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/clyxjl")
public class YxjlCtrl extends BaseController<ClClyxjl, String> {
	@Autowired
	private ClyxjlService service;

	@Override
	protected BaseService<ClClyxjl, String> getBaseService() {
		return service;
	}



}
