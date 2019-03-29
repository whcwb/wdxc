package com.ldz.znzp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ldz.znzp.bean.ThreadPoolBean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.ldz")
@EnableConfigurationProperties({ThreadPoolBean.class} ) // 开启配置属性支持  
public class ZnzpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZnzpServerApplication.class, args);
	}
}
