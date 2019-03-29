package com.ldz.job.job.dwq;

import com.ldz.dao.dwq.mapper.DwqGpsMapper;
import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.dao.dwq.model.GpsBeanNew;
import com.ldz.dao.obd.mapper.GpsObdMapper;
import com.ldz.dao.obd.model.GpsObdMessageBean;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class DwqGpsSaveJob implements Job {
    @Autowired
    private RedisTemplate redisTemplate;
    Logger errorLog = LoggerFactory.getLogger("error_info");
    @Autowired
    private DwqGpsMapper mapper;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        errorLog.info("dwq_gps存储job");
        BoundListOperations operations = redisTemplate.boundListOps("dwq_gps");
        long size = operations.size();
        if (size == 0) return;
        List<GpsBean> list = new ArrayList<>();
        for (long i = 0; i < size; i++) {
            GpsBean bean = (GpsBean) operations.rightPop();
            list.add(bean);
        }
        mapper.insertList(list);
    }
}
