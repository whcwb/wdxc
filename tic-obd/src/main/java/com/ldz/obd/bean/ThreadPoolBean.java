package com.ldz.obd.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.thread.pool")  
public class ThreadPoolBean {  
	//主线程数
    private int corePoolSize;  
    //最大线程数
    private int maxPoolSize;  
    //在线有效时长。线程池维护线程所允许的空闲时间，默认为60s
    private int keepAliveSeconds;  
    //等待队列数量
    private int queueCapacity;  
    //容器停止时是否等待job执行完，默认为false
    private boolean waitForJobsToCompleteOnShutdown;
    //容器停止时等待job执行的秒数，默认为0
    private int awaitTerminationSeconds;
}  