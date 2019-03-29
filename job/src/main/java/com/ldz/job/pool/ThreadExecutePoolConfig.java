package com.ldz.job.pool;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ldz.job.bean.ThreadPoolBean;


@Configuration  
@EnableAsync  
public class ThreadExecutePoolConfig {  
  
    @Autowired  
    private ThreadPoolBean config;  
  
    @Primary
    @Bean  
    public Executor ticThreadAsyncPool() { 
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        executor.setCorePoolSize(config.getCorePoolSize());  
        executor.setMaxPoolSize(config.getMaxPoolSize());  
        executor.setQueueCapacity(config.getQueueCapacity());  
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());  
        executor.setWaitForTasksToCompleteOnShutdown(config.isWaitForJobsToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(config.getAwaitTerminationSeconds());
        executor.setThreadNamePrefix("TicExecutor-");  
        
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务  
        // ThreadPoolExecutor.AbortPolicy():直接抛出java.util.concurrent.RejectedExecutionException异常
        // ThreadPoolExecutor.CallerRunsPolicy:用于被拒绝任务的处理程序，它直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        // ThreadPoolExecutor.DiscardOldestPolicy:用于被拒绝任务的处理程序，它将丢弃队列中最旧任务
        // ThreadPoolExecutor.DiscardPolicy:用于被拒绝任务的处理程序，它将丢弃当前的任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行  
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());  
        executor.initialize();  
        return executor;  
    }  
}  