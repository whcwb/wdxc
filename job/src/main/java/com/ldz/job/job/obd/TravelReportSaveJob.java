package com.ldz.job.job.obd;

import com.ldz.dao.obd.mapper.TravelReportMapper;
import com.ldz.dao.obd.model.ObdTravelItineraryBean;
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

public class TravelReportSaveJob implements Job {
    Logger errorLog = LoggerFactory.getLogger("error_info");
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TravelReportMapper travelReportMapper;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        errorLog.info("行程报告存储job");
        try {
            BoundListOperations operations = redisTemplate.boundListOps("travelReport");
            long size = operations.size();
            errorLog.info("点位大小,", size);
            if (size == 0) return;
            List<ObdTravelItineraryBean> list = new ArrayList<>();
            for (long i = 0; i < size; i++) {
                ObdTravelItineraryBean bean = (ObdTravelItineraryBean) operations.rightPop();
                errorLog.info("循环获取点位," , JsonUtil.toJson(bean));
                list.add(bean);
            }
            travelReportMapper.insertList(list);
        }catch (Exception e){
            errorLog.info("行程存储报告异常," , e);
        }

    }
}
