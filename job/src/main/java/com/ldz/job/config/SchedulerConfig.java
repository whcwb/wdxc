package com.ldz.job.config;

import java.io.IOException;
import java.util.Properties;

import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * Quartz配置
 * @author Lee
 *
 */
@Configuration
public class SchedulerConfig {
	
	//创建新的JobFactory，目的在job中可以使用Spring管理的bean对象
	@Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
	
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        //延时启动。单位秒
        factory.setStartupDelay(20);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        return propertiesFactoryBean.getObject();
    }
    
    @Bean
    public QuartzInitializerListener executorListener() {
       return new QuartzInitializerListener();
    }

    public static class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
	
		private transient AutowireCapableBeanFactory beanFactory;
		
		@Override
		public void setApplicationContext(final ApplicationContext context) {
		    beanFactory = context.getAutowireCapableBeanFactory();
		}
		
		@Override
		protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
			final Object job = super.createJobInstance(bundle);
		    beanFactory.autowireBean(job);
		    return job;
		}
	}
}