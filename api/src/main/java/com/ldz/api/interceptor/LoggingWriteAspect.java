package com.ldz.api.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

@Aspect
@Configuration
@Order(1)
public class LoggingWriteAspect {
	
	Logger log = LoggerFactory.getLogger("access_info");

    @Pointcut("execution(public * com.ldz.api..*.*Controller.*(..))")
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
    		
    		log.debug("请求方法[{}]，参数内容[{}]，返回结果[{}]", methodName, mapper.writeValueAsString(arg), mapper.writeValueAsString(returnVal));
    	}catch(Exception e){
    		
    	}
    }
}