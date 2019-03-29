package com.ldz.biz.controller;

import java.util.List;

import com.ldz.util.exception.RuntimeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.dao.biz.model.ClCssd;
import com.ldz.service.biz.interfaces.CssdService;
import com.ldz.sys.base.BaseController;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

/**
 * 超速设定
 */
@RestController
@RequestMapping("api/cssd")
public class CssdCtrl extends BaseController<ClCssd, String> {
	@Autowired
	private CssdService service;

	@Override
	protected BaseService<ClCssd, String> getBaseService() {
		return service;
	}

	@Override
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ApiResponse<String> save(ClCssd entity) {
		return service.saveEntity(entity);
	}

	@RequestMapping(value = "/update", method = { RequestMethod.POST })
	public ApiResponse<String> update(ClCssd entity) {
		return service.updateEntity(entity);
	}

	/**
	 * 根据车牌号获取超速设定值，带有机构筛选条件
	 *
	 * @param cx
	 * @return
	 */
	@RequestMapping("getByCx")
	public ApiResponse<List<ClCssd>> getByCx(String cx) {
		return service.getByCx(cx);
	}

	@PostMapping("/setCssds")
	public ApiResponse<String> setCssds(String cphs, String csz) {
		RuntimeCheck.ifBlank(cphs,"请先绑定车辆");
		return service.setCssds(cphs, csz);
	}

}
