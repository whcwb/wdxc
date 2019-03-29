package com.ldz.dwq.common.bean;

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
    //在线有效时长
    private int keepAliveSeconds;  
    //等待队列数量
    private int queueCapacity;  
}  