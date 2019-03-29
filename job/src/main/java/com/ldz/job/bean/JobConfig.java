package com.ldz.job.bean;

import org.quartz.*;

/**
 * auther chenwei
 * create at 2018/11/4
 */
public class JobConfig {
    private String identity;
    private String group;
    private String cron;
    private Class<? extends Job> cls;
    private JobDetail jobDetail;
    private CronTrigger cronTrigger;

    public JobConfig(Class<? extends Job> cls, String cron, String group) {
        this.identity = cls.getName();
        this.group = group;
        this.cron = cron;
        this.cls = cls;
    }
    public JobConfig(Class<? extends Job> cls, String cron, String group, String identity) {
        this.identity = identity;
        this.group = group;
        this.cron = cron;
        this.cls = cls;
    }

    public JobDetail getJobDetail() {
        return JobBuilder.newJob(this.cls).withIdentity(this.identity, this.group).build();
    }

    public CronTrigger getCronTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity(this.identity, this.group)
                .withSchedule(CronScheduleBuilder.cronSchedule(this.cron))
                .build();
    }
}
