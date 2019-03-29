package com.ldz.wechat.module.service.impl;

import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.commonUtil.JwtUtil;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.wechat.module.mapper.SysJzgxxMapper;
import com.ldz.wechat.module.model.SysJzgxx;
import com.ldz.wechat.module.service.SysJzgxxService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class SysJzgxxServiceImpl implements SysJzgxxService {

	@Autowired
	private SysJzgxxMapper jzgmapper;

	@Override
	public ApiResponse<String> findJzg(String zjhm, String name) {
		RuntimeCheck.ifBlank(zjhm, "证件号码不为空");
		RuntimeCheck.ifBlank(name, "姓名不为空");
		SysJzgxx jzg = jzgmapper.findJzg(name,zjhm);
		RuntimeCheck.ifNull(jzg, "证件号码或姓名有误");
		String userInfo = JsonUtil.toJson(jzg);
		String token = JwtUtil.createWechatToken("jzg",userInfo);
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		request.setAttribute("type","jzg");
		request.setAttribute("orgCode",jzg.getJgdm());
		request.setAttribute("userInfo",userInfo);
		ApiResponse<String> res = new ApiResponse<>();
		res.setResult(token);
		return res;
	}

	public  SysJzgxx findById(String id){
		return jzgmapper.findById(id);
	}

	public  ApiResponse<Map<String,Object>> getUserInfo(){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Map<String,Object> map = new HashMap<>();
		String type = (String) request.getAttribute("type");
		String userInfo = (String) request.getAttribute("userInfo");
		RuntimeCheck.ifTrue((StringUtils.isEmpty(type) || StringUtils.isEmpty(userInfo)) && true,"当前登录用户未空！");
		if ("jzg".equals(type)){
			SysJzgxx jzg = JsonUtil.toBean(userInfo,SysJzgxx.class);
			RuntimeCheck.ifNull(jzg,"未找到教职工信息");
			map.put("userInfo",jzg);
		}else{
			RuntimeCheck.ifTrue(true,"您不属于教职工");
		}
		return ApiResponse.success(map);

	}
}
