package com.ldz.job.job.obd;

import com.ldz.dao.obd.mapper.GpsObdMapper;
import com.ldz.dao.obd.model.GpsObdMessageBean;
import com.ldz.util.commonUtil.JsonUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class GpsObdSaveJob implements Job {
    @Autowired
    private RedisTemplate redisTemplate;
    Logger errorLog = LoggerFactory.getLogger("error_info");
    @Autowired
    private GpsObdMapper gpsObdMapper;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        errorLog.info("gps obd存储job");
        BoundListOperations operations = redisTemplate.boundListOps("gps_obd");
        long size = operations.size();
        if (size == 0) return;
        List<GpsObdMessageBean> list = new ArrayList<>();
        errorLog.info("obd gps list size , " + list.size());
        errorLog.info("obd gps list content , " + JsonUtil.toJson(list));
        for (long i = 0; i < size; i++) {
            GpsObdMessageBean bean = (GpsObdMessageBean) operations.rightPop();
            errorLog.info("obd_gps 存储 , " + JsonUtil.toJson(bean));
            list.add(bean);
        }
        gpsObdMapper.insertList(list);
    }
}
