package com.ldz.job.job.dwq;

import com.ldz.dao.dwq.mapper.TravelDataMapper;
import com.ldz.dao.dwq.model.TravelData;
import com.ldz.util.commonUtil.JsonUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class TravelDataSaveJob implements Job {
    Logger errorLog = LoggerFactory.getLogger("error_info");
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TravelDataMapper travelDataMapper;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        errorLog.info("定位器行程报告存储job");
        try {
            BoundListOperations operations = redisTemplate.boundListOps("travelData");
            long size = operations.size();
            if (size == 0) return;
            List<TravelData> list = new ArrayList<>();
            errorLog.info("list 定位器 size " + list.size());
            for (long i = 0; i < size; i++) {
                TravelData bean = (TravelData) operations.rightPop();
                errorLog.info("定位器存储" + JsonUtil.toJson(bean));
                list.add(bean);
            }
            travelDataMapper.insertList(list);
        }catch (Exception e){
            errorLog.info("定位器存储报错,", e);
        }

    }
}
