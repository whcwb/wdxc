package com.ldz.biz.controller;

import com.ldz.sys.model.SysYh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClJsy;
import com.ldz.service.biz.interfaces.JsyService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.util.List;

import javax.validation.Valid;

/**
 * 驾驶员设定
 */
@RestController
@RequestMapping("api/jsy")
public class CljsyCtrl extends BaseController<ClJsy, String> {
	 @Autowired
	 private  JsyService jsyservice;

	@Override
	protected BaseService<ClJsy, String> getBaseService() {
		
		return jsyservice;
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public ApiResponse<String> update(@Valid ClJsy entity) {
		return jsyservice.updateEntity(entity);
	}

	@PostMapping("/save")
	public ApiResponse<String> save(@Valid ClJsy entity) {
		return jsyservice.saveEntity(entity);
	}

	@RequestMapping("notBindList")
	public ApiResponse<List<ClJsy>> notBindList(){
		SysYh user = getCurrentUser();
		return jsyservice.notBindList(user);
	}

	/**
	 * 修改司机在班、休息的状态
	 * 当司机从在班修改成休息时，需要完结或者取消未完成的订单
	 * @return
	 */
	@PostMapping("jsytype")
	public ApiResponse<String> updateJsyType(ClJsy entity){
		return jsyservice.updateJsyType(entity);
	}
}
