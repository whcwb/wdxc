package com.ldz.wechat.config;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

/**
 * Spring声明式事物拦截器配置
 * 不使用Springboot提供的注解事物方式@EnableTransactionManagement和@Transactional
 * @author 李彬彬
 *
 */
@Configuration
public class TransactionManagerConfigure {
	
	@Bean(name="mybatisTransactionInterceptor")
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager platformTransactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        //事物管理器
        transactionInterceptor.setTransactionManager(platformTransactionManager);
        Properties transactionAttributes = new Properties();
        //查询方法事物配置
        transactionAttributes.setProperty("find*", "PROPAGATION_NOT_SUPPORTED,readOnly");
        transactionAttributes.setProperty("query*", "PROPAGATION_NOT_SUPPORTED,readOnly");
        transactionAttributes.setProperty("get*", "PROPAGATION_NOT_SUPPORTED,readOnly");
        transactionAttributes.setProperty("select*", "PROPAGATION_NOT_SUPPORTED,readOnly");
        //更新数据事物配置
        transactionAttributes.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("remove*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
        transactionAttributes.setProperty("batch*", "PROPAGATION_REQUIRED,-Exception");

        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        return transactionInterceptor;
    }
	
    //代理到ServiceImpl的Bean
    @Bean
    public BeanNameAutoProxyCreator transactionAutoProxy() {
        BeanNameAutoProxyCreator transactionAutoProxy = new BeanNameAutoProxyCreator();
        transactionAutoProxy.setProxyTargetClass(true);
        transactionAutoProxy.setBeanNames("*ServiceImpl");
        transactionAutoProxy.setInterceptorNames("mybatisTransactionInterceptor");
        return transactionAutoProxy;
    }
}
