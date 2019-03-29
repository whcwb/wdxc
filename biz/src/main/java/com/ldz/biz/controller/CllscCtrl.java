package com.ldz.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClLsc;
import com.ldz.service.biz.interfaces.LscService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

/**
 * 临时车设定
 */
@RestController
@RequestMapping("api/lsc")
public class CllscCtrl extends BaseController<ClLsc, String> {
	 @Autowired
	 private  LscService lscService;

	@Override
	protected BaseService<ClLsc, String> getBaseService() {
		
		return lscService;
	}

	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ApiResponse<String> save(ClLsc entity) {
		return lscService.saveEntity(entity);
	}
}
