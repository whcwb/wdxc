package com.ldz.job.job;

import com.ldz.sys.mapper.SysPtrzMapper;
import com.ldz.sys.model.SysRz;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class LogSaveJob implements Job {
    Logger errorLog = LoggerFactory.getLogger("error_info");
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysPtrzMapper ptrzMapper;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        errorLog.info("日志存储job");
        try {
            BoundListOperations operations = redisTemplate.boundListOps("logList");
            long size = operations.size();
            if (size == 0) return;
            List<SysRz> list = new ArrayList<>();
            for (long i = 0; i < size; i++) {
                SysRz bean = (SysRz) operations.rightPop();
                list.add(bean);
            }
            errorLog.info("size:"+list.size());
            ptrzMapper.insertList(list);
        }catch (Exception e){
            errorLog.info("日志存储job error,", e);
        }

    }
}
