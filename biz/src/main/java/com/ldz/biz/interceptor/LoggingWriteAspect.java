package com.ldz.biz.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ldz.sys.model.SysRz;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.RzService;
import com.ldz.util.redis.RedisTemplateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Configuration
@Order(1)
public class LoggingWriteAspect {


	Logger log = LoggerFactory.getLogger("access_info");

	@Autowired
	private RedisTemplateUtil redisTemplateUtil;
	@Autowired
	private RzService rzService;
    @Pointcut("execution(public * com.ldz..*.*Controller.*(..))")
    public void logPointCut() {

    }

    @AfterReturning(pointcut="logPointCut()",returning="returnVal")
    public void afterReturn(JoinPoint joinPoint,Object returnVal){
    	ObjectMapper mapper = new ObjectMapper();

    	try{
    		Object[] args = joinPoint.getArgs();
    		Object arg = new Object();
    		if (args != null && args.length > 0){
    			arg = args[0];
    		}
    		Signature signature = joinPoint.getSignature();

            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
    		Object target = joinPoint.getTarget();
    		String methodName = target.getClass().getSimpleName()+"."+method.getName();

			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			SysYh user = (SysYh)request.getAttribute("userInfo");
			SysRz rz = new SysRz();
			if (user != null){
				rz.setCzr(user.getYhid());
			}
			rz.setCs(mapper.writeValueAsString(arg));
			rz.setCzsj(new Date());
			rz.setFf(methodName);
//			rzService.save(rz);
			log.debug("logList");
			redisTemplateUtil.boundListOps("logList").leftPush(rz);
    		log.debug("请求方法[{}]，参数内容[{}]，返回结果[{}]", methodName, mapper.writeValueAsString(arg), mapper.writeValueAsString(returnVal));
    	}catch(Exception e){

    	}
    }
}