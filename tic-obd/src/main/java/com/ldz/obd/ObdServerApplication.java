package com.ldz.obd;

import com.ldz.obd.bean.ThreadPoolBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})//阻止SPRING BOOT加载数据库。
@EnableConfigurationProperties({ThreadPoolBean.class} ) // 开启配置属性支持  
public class ObdServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObdServerApplication.class, args);
	}
}
