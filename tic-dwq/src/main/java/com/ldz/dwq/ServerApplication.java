package com.ldz.dwq;

import com.ldz.dwq.common.bean.ThreadPoolBean;
import com.ldz.dwq.common.tools.SpringContextUtil;
import com.ldz.dwq.handler.biz.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ThreadPoolBean.class} ) // 开启配置属性支持
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
