package com.ldz.api.interceptor;

import com.ldz.dao.biz.mapper.ClClientMapper;
import com.ldz.util.redis.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.ldz.util.config.BaseWebConfigure;

@Configuration
public class ExtendInterceptor extends BaseWebConfigure {

	@Autowired
	private ClClientMapper clientMapper;
	@Autowired
	private RedisTemplateUtil redisTemplateUtil;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AccessInterceptor(clientMapper,redisTemplateUtil))
				.addPathPatterns("/app/user/**","/app/car/**")
				.excludePathPatterns(
						"/app/device/**",
						"/pub/**"
						,"/upload", "/app/dzwl/**", "/app/report/**");
		registry.addInterceptor(new AppAccessInterceptor())
				.addPathPatterns("/app/device/**" , "/app/dzwl/**");
		super.addInterceptors(registry);
	}

	/**
	 * 全局跨域处理方法
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		        .allowedOrigins("*")
		        .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS",  "TRACE")
		        .allowCredentials(true)
		        .maxAge(3600);
	}
}
