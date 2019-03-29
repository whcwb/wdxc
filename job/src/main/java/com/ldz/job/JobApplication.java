package com.ldz.job;

import java.math.BigDecimal;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ldz.job.convert.BeanUtilsDateConverter;

@SpringBootApplication
@ComponentScan("com.ldz")
public class JobApplication {
	//注册BeanUtils拷贝日期对象转换器
	static {
	      ConvertUtils.register(new BeanUtilsDateConverter(null), java.sql.Date.class);
	      ConvertUtils.register(new BeanUtilsDateConverter(null), java.sql.Timestamp.class);
	      ConvertUtils.register(new BeanUtilsDateConverter(null), java.util.Date.class);
	      ConvertUtils.register(new BeanUtilsDateConverter(null), BigDecimal.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}
}
