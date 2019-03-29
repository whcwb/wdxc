package com.ldz.biz.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ldz.sys.model.SysJg;
import com.ldz.sys.service.JgService;
import com.ldz.util.spring.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 访问接口控制
 *
 * @author 李彬彬
 *
 */
@Slf4j
public class UbiAccessInterceptor extends HandlerInterceptorAdapter {

	private JgService jgService;

	private StringRedisTemplate redisDao;

	public UbiAccessInterceptor() {
	}

	public UbiAccessInterceptor(StringRedisTemplate redisTemp) {
		this.jgService = SpringContextUtil.getBean(JgService.class);
		this.redisDao = redisTemp;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 查看请求类型
		String method = request.getMethod();
		if (method.equals("OPTIONS")) {
			// 如果收到的是跨域预请求消息，直接响应，返回true，以便后续跨域请求成功
			return true;
		}

		String apicode = request.getHeader("apicode");
		

		if (apicode == null)
			apicode = request.getParameter("apicode");

		if (StringUtils.isEmpty(apicode)){
			response.sendError(HttpStatus.NOT_FOUND.value());
			return false;
		}
		//验证来访者IP是否已授权，如果未授权则拒绝访问

		return isAccess(apicode, request);
	}
	
	private boolean isAccess(String apicode, HttpServletRequest request){
		try{
			//验证来访者IP是否已授权，如果未授权则拒绝访问
			String ip = getClientIp(request);
			//IP进行MD5加密计算得到一个结果
			String md5Val = ip;
			if (md5Val.equals(apicode)){
				
			}
			//测试用
			SysJg jgxx = new SysJg();
			jgxx.setJgdm("100");
			jgxx.setJgmc("机构名称");
			request.setAttribute("requestJgxx", jgxx);
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
	private static String getClientIp(HttpServletRequest request) {
		String ip=request.getHeader("x-forwarded-for");
		if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("Proxy-Client-IP");
		}
		if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
			ip=request.getHeader("X-Real-IP");
		}
		if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){
			ip=request.getRemoteAddr();
		}


        return ip;
    }
}
