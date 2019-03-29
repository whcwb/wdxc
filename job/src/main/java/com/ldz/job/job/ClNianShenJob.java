package com.ldz.job.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ldz.job.service.ClService;

/**
 * 定时器说明:每天更新一次,更新车辆年审时间
 * 
 * @author liuzhihao
 *
 */
// 在成功执行了job类的execute方法后,更新JobDetail中JobDataMap的数据
@PersistJobDataAfterExecution
// 等待上一次任务执行完成，才会继续执行新的任务
@DisallowConcurrentExecution
public class ClNianShenJob implements Job {
	
	Logger errorLog = LoggerFactory.getLogger("error_info");

    @Autowired
    private ClService clservice;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		try {
			clservice.updateNianshen();
		} catch (Exception e) {
			errorLog.error("同步车辆在线状态异常", e);
			JobExecutionException e2 = new JobExecutionException(e);
			// 当任务执行失败时，立即停止所有相关这个任务的触发器
			e2.setRefireImmediately(true);
		}

		
	}

}
