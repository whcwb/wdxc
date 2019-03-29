package com.ldz.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClXl;
import com.ldz.service.biz.interfaces.XlService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

@RestController
@RequestMapping("api/xl")
public class ClXlCtrl  extends BaseController<ClXl, String>{
    @Autowired
    private XlService xlservice;
	@Override
	protected BaseService<ClXl, String> getBaseService() {
		return xlservice;
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public ApiResponse<String> update(ClXl entity) {
		return xlservice.updateEntity(entity);
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ApiResponse<String> save(ClXl entity) {
		return xlservice.saveEntity(entity);
	}
	
	
}
