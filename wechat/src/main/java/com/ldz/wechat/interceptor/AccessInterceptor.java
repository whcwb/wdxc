package com.ldz.wechat.interceptor;

import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.commonUtil.JwtUtil;
import com.ldz.wechat.module.model.SysJzgxx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 访问接口控制
 *
 * @author 李彬彬
 *
 */
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {

	private StringRedisTemplate redisDao;

	private List<String> whiteList = Arrays.asList("/api/gn/getMenuTree","/api/jg/pager","/put/jsy/getjsy","/put/jzg/getJzg");
	private List<String> anonymousList = Arrays.asList("/api/zd/pager");

	public AccessInterceptor() {
	}

	public AccessInterceptor(StringRedisTemplate redisTemp) {
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
		if (anonymousList.contains(request.getRequestURI())){
			return true;
		}
		String token = request.getHeader("token");
		if (token == null)
			token = request.getParameter("token");
		if (StringUtils.isEmpty(token)){
			return true;
		}

		log.debug("访问地址[{}],请求token[{}]]", request.getRequestURI(), token);
		String type = JwtUtil.getClaimAsString(token, "type");
		String userInfo = JwtUtil.getClaimAsString(token, "userInfo");
		if (StringUtils.isEmpty(userInfo)){
			return true;
		}
		request.setAttribute("type",type);
		request.setAttribute("userInfo",userInfo);
		return super.preHandle(request, response, handler);
	}

}
