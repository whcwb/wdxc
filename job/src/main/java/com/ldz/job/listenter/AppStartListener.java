package com.ldz.job.listenter;

import com.ldz.job.config.ScheduleComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * 系统启动时需要手动加载运行的方法
 * @author Lee
 *
 */
@Service
public class AppStartListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private ScheduleComponent schedule;
	
	@Override  
    public void onApplicationEvent(ContextRefreshedEvent event)  {
		//只在初始化“根上下文”的时候执行
        final ApplicationContext app = event.getApplicationContext();
        if (null == app.getParent()) { // 当存在父子容器时，此判断很有用
        	schedule.startScheduler();
        }
		
	}
}
