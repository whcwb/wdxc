package com.ldz.job.job;

import com.ldz.job.service.ClSbyxsjjlService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 设备运行事件记录
 */
public class SbYxSjJlJob implements Job {


    @Autowired
    private ClSbyxsjjlService clSbyxsjjlService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        clSbyxsjjlService.saveBatch();

    }
}
