package com.ldz.ticserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ldz.ticserver.bean.ThreadPoolBean;

@EnableScheduling
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableConfigurationProperties({ThreadPoolBean.class} ) // 开启配置属性支持  
public class TicServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicServerApplication.class, args);
	}
}
