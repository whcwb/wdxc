//package com.ldz.dwq.config;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.ThreadPoolExecutor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import com.ldz.dwq.common.bean.ThreadPoolBean;
//
//@Configuration
//@EnableAsync
//public class ThreadExecutePoolConfig {
//
//    @Autowired
//    private ThreadPoolBean config;
//
//    @Bean
//    public Executor jxbaThreadAsyncPool() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(config.getCorePoolSize());
//        executor.setMaxPoolSize(config.getMaxPoolSize());
//        executor.setQueueCapacity(config.getQueueCapacity());
//        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
//        executor.setThreadNamePrefix("IotServerExecutor-");
//
//        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
//        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
//        executor.initialize();
//        return executor;
//    }
//}
