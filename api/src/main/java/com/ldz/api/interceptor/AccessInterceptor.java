package com.ldz.api.interceptor;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ldz.api.util.TokenUtil;
import com.ldz.dao.biz.mapper.ClClientMapper;
import com.ldz.dao.biz.model.ClClient;
import com.ldz.util.commonUtil.Des;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import lombok.extern.slf4j.Slf4j;

/**
 * 访问接口控制
 * @author 李彬彬
 *
 */
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {

    private ClClientMapper clientMapper;

    private RedisTemplateUtil redisTemplateUtil;

	public AccessInterceptor(ClClientMapper clientMapper,RedisTemplateUtil redisTemplateUtil) {
        this.redisTemplateUtil = redisTemplateUtil;
        this.clientMapper = clientMapper;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//查看请求类型
		String method = request.getMethod();
		if (method.equals("OPTIONS")){
			//如果收到的是跨域预请求消息，直接响应，返回true，以便后续跨域请求成功
			return true;
		}

		//访问权限值
//		String userid = "348561383694008320";
//		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3Y3BtcyIsImF1ZCI6IndjcG1zIiwibG9naW5OYW1lIjoiYWRtaW4iLCJpc3MiOiJ3Y3BtcyIsInVzZXJJZCI6IjM0ODU2MTM4MzY5NDAwODMyMCJ9.TjjF-kfmSnnJYIV4y5XR1C00rqOv3YxkeAB1sAvi80g";
		String clientId = request.getHeader("clientId");
        String token = request.getHeader("token");
		String userId = request.getHeader("user_id");
		String url = request.getHeader("url");

		if (token == null) token = request.getParameter("token");
		if (userId == null) userId = request.getParameter("user_id");
		String uri = request.getRequestURI();
//		if (!(uri.equals("/app/user/login") || uri.equals("/app/user/regist")) && StringUtils.isEmpty(userId)){
//			request.getRequestDispatcher("/403").forward(request,response);
//			return false;
//		}
//		if (StringUtils.isEmpty(token)){
//            request.getRequestDispatcher("/403").forward(request,response);
//            return false;
//		}
//		ClClient client = clientMapper.selectByPrimaryKey(clientId);
//		if (client == null){
//            request.getRequestDispatcher("/403").forward(request,response);
//            return false;
//        }
//        if (!TokenUtil.validToken(clientId, token,client.getSecret())){
//            request.getRequestDispatcher("/403").forward(request,response);
//            return false;
//        }


//        String keyPrefix = "LoginUser-"+userId+"-";
//        Set<Object> keys = redisTemplateUtil.keys(keyPrefix+"*");
//        if (keys.size() == 0){
//            request.getRequestDispatcher("/authFiled").forward(request,response);
//            return false;
//        }

		request.setAttribute("appUserId",userId);
		request.getSession().setAttribute("jgdm","100");
        request.getSession().setAttribute("userId",userId);
//        singleClientCheck(userId,token);
        log.debug("访问地址[{}], 请求openid[{}],请求token[{},header请求地址[{}]]", request.getRequestURI(), userId, token, url);
		return super.preHandle(request, response, handler);
	}

    /**
     * 单客户端登录限制
     * @param userId
     * @param token
     */
	private void singleClientCheck(String userId,String token){
	    String keyPrefix = "LoginUser-"+userId+"-";
	    Set<Object> keys = redisTemplateUtil.keys(keyPrefix+"*");
	    if (keys.size() != 0){
            redisTemplateUtil.delete(keys);
        }
        redisTemplateUtil.boundValueOps(keyPrefix + token).set("");

    }
}
