package com.ldz.job.config;

import com.ldz.job.bean.JobConfig;
import com.ldz.job.job.*;
import com.ldz.job.job.dwq.DwqGpsSaveJob;
import com.ldz.job.job.dwq.TravelDataSaveJob;
import com.ldz.job.job.obd.GpsObdSaveJob;
import com.ldz.job.job.obd.TravelReportSaveJob;
import org.quartz.Job;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleComponent {

    Logger errorLog = LoggerFactory.getLogger("error_info");

    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    private List<JobConfig> jobConfigs = new ArrayList<>();

    private void addJob(Class<? extends Job> cls, String cron, String group){
        jobConfigs.add(new JobConfig(cls,cron,group));
    }

    public void startScheduler() {
        // 日志存储job
        addJob(LogSaveJob.class,"0 0/1 * * * ? *","LogSaveJob");
        // 定位器行程数据存储job
        addJob(TravelDataSaveJob.class,"0 0/1 * * * ? *","TravelDataSaveJob");
        // 定位器数据存储job
        addJob(DwqGpsSaveJob.class,"0/5 * * * * ? *","DwqGpsSaveJob");
        // 行程报告存储job
        addJob(TravelReportSaveJob.class,"0 0/1 * * * ? *","travelReportSave");
        // gps+obd 存储 job
        addJob(GpsObdSaveJob.class,"0/5 * * * * ? *","GpsObdSaveJob");
        // 上传鹰眼定时任务
        addJob(ZdToYyJob.class,"0 0 0 1/1 * ? ","zdToYy");
        // gps同步job
        addJob(GpsSaveJob.class,"0/5 * * * * ? *","GPSSync");
        // 设备运行事件记录周期，每一分钟运行一次
        addJob(SbYxSjJlJob.class,"0 0/1 * * * ? *","sbyxsjjl");
        // 车辆年审日期获取job
        addJob(ClNianShenJob.class,"0 0 12 * * ?","clnssync");
        // 终端服务到期检测job
        addJob(ZdFwJob.class,"0 0 0 * * ? ","zdFw");
        // 统计job
        addJob(ZdReportJob.class,"1 0 0 * * ?","zdReport");
//        addJob(ZdReportJob.class,"0 0/1 * * * ? *","zdReport");
        // 临时升级设备使用。更新完后将取消获取job
//        addJob(DeviceUpdateSyncJob.class,"0 0/5 * * * ? *","TmpDevUpdatesync");

        try {
            for (JobConfig jobConfig : jobConfigs) {
                schedulerFactory.getScheduler().scheduleJob(jobConfig.getJobDetail(), jobConfig.getCronTrigger());
            }
        } catch (SchedulerException e) {
            errorLog.error("任务创建失败", e);
        }
    }
}
