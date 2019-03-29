package com.ldz.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClXlzd;
import com.ldz.service.biz.interfaces.XlzdService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

@RestController
@RequestMapping("api/xlzd")
public class XLZDCtrl extends BaseController<ClXlzd, String> {
    @Autowired
    private XlzdService xlzdservice;
	@Override
	protected BaseService<ClXlzd, String> getBaseService() {
		return xlzdservice;
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public ApiResponse<String> update(ClXlzd entity) {
		return xlzdservice.updateEntity(entity);
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ApiResponse<String> save(ClXlzd entity) {
		return xlzdservice.saveEntity(entity);
	}
}
