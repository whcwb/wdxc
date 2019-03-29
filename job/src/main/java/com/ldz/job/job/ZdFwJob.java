package com.ldz.job.job;

import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.job.service.ClZdglService;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// 在成功执行了job类的execute方法后,更新JobDetail中JobDataMap的数据
@PersistJobDataAfterExecution
// 等待上一次任务执行完成，才会继续执行新的任务
@DisallowConcurrentExecution
public class ZdFwJob implements Job {

    @Autowired
    private ClZdglService clZdglService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SimpleCondition condition = new SimpleCondition(ClZdgl.class);
        condition.eq(ClZdgl.InnerColumn.jhzt,"20");
        condition.and().andCondition(" zd_jssj != '#'");
        List<ClZdgl> all = clZdglService.findByCondition(condition);
        // 查询每个终端是否到期
        all.forEach(clZdgl -> {
            String zdJssj = clZdgl.getZdJssj();
            String today = DateUtils.getToday("yyyy-MM-dd");
            if(zdJssj.compareTo(today) < 0){
                // 终端已到期 , 修改到期时间
                clZdgl.setJhzt("30");
                clZdglService.update(clZdgl);
            }
        });
    }
}
