package com.ldz.wechat.module.controller;

import com.alibaba.druid.util.StringUtils;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.wechat.exception.RuntimeCheck;
import com.ldz.wechat.module.model.ClJsy;
import com.ldz.wechat.module.model.SysJzgxx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.base.BaseController;
import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.SysYjfk;
import com.ldz.wechat.module.service.YjService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 意见
 */
@RestController
@RequestMapping("put/yj")
public class YjController extends BaseController<SysYjfk, String> {
	@Autowired
	private YjService service;

	@Override
	protected BaseService<SysYjfk, String> getBaseService() {
		return service;
	}

	@Override
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public ApiResponse<String> save(SysYjfk entity) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String type = (String) request.getAttribute("type");
		String userInfo = (String) request.getAttribute("userInfo");
		if ("jzg".equals(type)){
			SysJzgxx jzg = JsonUtil.toBean(userInfo,SysJzgxx.class);
			if(jzg!=null){
				entity.setUserId(jzg.getId());
			}
		}else if ("jsy".equals(type)){
			ClJsy jsy = JsonUtil.toBean(userInfo,ClJsy.class);
			if(jsy!=null){
				entity.setSjId(jsy.getSfzhm());
			}
		}
		return service.saveEntity(entity);
	}

	@Override
	@RequestMapping(value="/update", method={RequestMethod.POST})
	public ApiResponse<String> update(SysYjfk entity){
		return service.updateEntity(entity);
	}
}
