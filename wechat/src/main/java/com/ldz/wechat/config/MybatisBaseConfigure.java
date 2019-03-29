package com.ldz.wechat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ldz.util.commonUtil.SnowflakeIdWorker;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * Mybatis基础配置。
 * 目前用于自动加载指定包下面的Mapper接口文件。
 * 主配置文件和SQL配置文件，根据每个项目结构不同，在具体的项目中进行配置管理，不做统一配置管理
 * @author 李彬彬
 *
 */
@Configuration
@MapperScan(basePackages={"com.ldz.**.mapper"})
public class MybatisBaseConfigure {

	/**
	 * 主键生成对象，所有表的主键都由它来生成
	 * @return
	 */
	@Bean
	public SnowflakeIdWorker idGenerator(){
		return new SnowflakeIdWorker(0, 0);
	}
}
