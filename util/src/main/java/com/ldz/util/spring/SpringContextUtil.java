package com.ldz.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext ;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}
	
	public static <T> T getBean(Class<T> t){
		if (applicationContext == null) return null;
		return applicationContext.getBean(t);
	}

	public static boolean containBean(String name){
		return applicationContext.containsBean(name);
	}
	
	public static Object getBean(String name){
		return applicationContext.getBean(name);
	}
}
