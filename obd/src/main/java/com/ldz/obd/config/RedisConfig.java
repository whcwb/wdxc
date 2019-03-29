package com.ldz.obd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.ldz.util.redis.RedisTemplateUtil;

/**
 * redis配置
 * @author 李彬彬
 *
 */
@Configuration
@Order(1)
public class RedisConfig {
	
	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	/**
	 * 本项目缓存Redis对象
	 * @return
	 */
	@Bean
	@Primary
	public RedisTemplateUtil redisTemplateDefault(){
		RedisTemplateUtil bean = new RedisTemplateUtil(redisConnectionFactory);
		return bean;
	}
}
