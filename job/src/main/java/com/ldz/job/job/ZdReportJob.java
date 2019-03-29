package com.ldz.job.job;

import com.ldz.dao.biz.model.*;
import com.ldz.job.service.*;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZdReportJob implements Job {
    Logger errorLog = LoggerFactory.getLogger("error_info");
    @Autowired
    private ClZdglService zdglService;
    @Autowired
    private ClZdglReportService reportService;
    @Autowired
    private CarService carService;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private ClReportLogService logService;

    @Autowired
    private ClReportJobService jobService;

    /**
     * 统计每天的终端行程记录
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        List<ClZdglReport> reports = new ArrayList<>();
        // 查询当前处在激活状态的所有的终端设备
        // 统计为每天零点  , 将统计数据全部初始化

        SimpleCondition condition = new SimpleCondition(ClZdgl.class);
        condition.eq(ClZdgl.InnerColumn.jhzt , "20");   // 当天数据初始化 , 只需要当天还在激活状态即可
        // 所有在 激活状态的设备 和 前一天刚过期的设备
        List<ClZdgl> zdgls = zdglService.findByCondition(condition);
        for (ClZdgl zdgl : zdgls) {

            List<Car> cars = carService.findEq(Car.InnerColumn.zdbh, zdgl.getZdbh());
            // 每天 0点查询设备总里程
             if(CollectionUtils.isNotEmpty(cars) ){
                // 首先判断购买时间是否小于今天
                Car car = cars.get(0);
                Date carBuyDate = car.getBuyDate();
                DateTime now = DateTime.now();
                Date date = now.toDate();
                if(carBuyDate.getTime() <= date.getTime()){
                    // 说明有购买里程 , 先查询 job 有没有执行过
                    List<ClReportJob> jobs = jobService.findEq(ClReportJob.InnerColumn.zdbh, zdgl.getZdbh());
                    // 新建一条日志
                    ClReportLog reportLog = new ClReportLog();
                    reportLog.setCjsj(DateUtils.getNowTime());
                    reportLog.setId(idWorker.nextId() + "");
                    // 结束时间为今天的时间 减一
                    reportLog.setJssj(DateTime.now().minusDays(1).toString("yyyy-MM-dd"));
                    // 消耗前剩余里程
                    reportLog.setQlc(car.getRemainingMileage()+"");
                    // 终端编号
                    reportLog.setZdbh(car.getZdbh());
                    ClReportJob job ;
                    List<ClZdglReport> zdglReports;
                    if(CollectionUtils.isNotEmpty(jobs)){
                        // 已经执行过job 只需要在减去前一天的即可
                        job = jobs.get(0);
                        // 最新一天任务的时间
                        String gxrq = job.getGxrq();  // yyyy-Mm-dd

                        SimpleCondition simpleCondition = new SimpleCondition(ClZdglReport.class);
                        simpleCondition.eq(ClZdglReport.InnerColumn.rZdbh , car.getZdbh());
                        simpleCondition.and().andGreaterThan(ClZdglReport.InnerColumn.rTjrq.name(), gxrq);
                        zdglReports = reportService.findByCondition(simpleCondition);

                        // 开始时间为前一天的时间加 1
                        reportLog.setKssj(DateTime.parse(gxrq).plusDays(1).toString("yyyy-MM-dd"));

                        job.setGxrq(DateTime.now().minusDays(1).toString("yyyy-MM-dd"));
                        job.setZxsj(DateUtils.getNowTime());

                    }else {
                        // 第一次存储job
                        job = new ClReportJob();
                        job.setId(idWorker.nextId() + "");
                        job.setZxsj(DateUtils.getNowTime());
                        // 更新日期到今天的前一天为止
                        job.setGxrq(DateTime.parse(DateUtils.getDateStr(new Date(),"yyyy-MM-dd")).minusDays(1).toString("yyyy-MM-dd"));
                        job.setZdbh(car.getZdbh());


                        // 开始计算从购买时间 到 今天0点之前所有的里程总数
                        String buyTime = dateFormat.format(carBuyDate);
                        // 开始时间为购买时间
                        reportLog.setKssj(buyTime);

                        SimpleCondition simpleCondition = new SimpleCondition(ClZdglReport.class);
                        simpleCondition.gte(ClZdglReport.InnerColumn.rTjrq , buyTime);
                        simpleCondition.eq(ClZdglReport.InnerColumn.rZdbh, car.getZdbh());
                        zdglReports = reportService.findByCondition(simpleCondition);
                    }
                    if(reportLog.getJssj().compareTo(job.getGxrq()) <= 0){
                        if(CollectionUtils.isNotEmpty(zdglReports)){
                            double totalMile = 0;
                            for (ClZdglReport zdglReport : zdglReports) {
                                // 计算总里程
                                totalMile += Double.parseDouble(zdglReport.getrLczs())/ 1000d;
                            }
                            // 四舍五入
                            long round = Math.round(totalMile);
                            long l = car.getRemainingMileage() - round;
                            if(l < 0){
                                car.setRemainingMileage(0);
                            }else{
                                car.setRemainingMileage((int) l);
                            }
                            carService.update(car);

                            // 更新job

                            job.setSylc(l+"");
                            if(CollectionUtils.isNotEmpty(jobs)){
                                errorLog.info("reportJob 更新 ->  " + JsonUtil.toJson(job));
                                jobService.update(job);
                            }else{
                                errorLog.info("reportJob 保存 ->" + JsonUtil.toJson(job));
                                jobService.save(job);
                            }

                            // 本次消耗里程
                            reportLog.setLc(round + "");
                            reportLog.setSylc(l + "");

                        }else {
                            reportLog.setSylc(car.getRemainingMileage() + "");
                            reportLog.setLc(0 + "");
                        }
                        errorLog.info("reportJob 操作日志保存 ->" + JsonUtil.toJson(reportLog));
                        logService.save(reportLog);
                    }

                }


            }


            List<ClZdglReport> zdglReports = reportService.findEq(ClZdglReport.InnerColumn.rTjrq, DateUtils.getDateStr(new Date(), "yyyy-MM-dd"));
            if(CollectionUtils.isNotEmpty(zdglReports)){
                continue;
            }

            // 一个终端统计一条数据
            ClZdglReport report = new ClZdglReport();
            // 执行时间
            report.setrGxsj(DateUtils.getNowTime());
            // 终端编号和终端类型
            report.setrZdbh(zdgl.getZdbh());
            report.setrZdlx(zdgl.getZdLx());
            report.setJgdm(zdgl.getJgdm());
            report.setJgmc(zdgl.getJgmc());
            //统计日期
            report.setrTjrq(DateUtils.getDateStr(new Date(),"yyyy-MM-dd"));

            report.setrLczs("0");
            report.setrJszsc("0");
            report.setrXcs("0");
            report.setrJjs("0");
            report.setrJjss("0");
            report.setrJzw("0");
            report.setrZzss("0");
            report.setrZzxslc("0");
            report.setrCscs("0");
            report.setrZgfcxcs("0");
            report.setrWgfcxcs("0");
            report.setrSycxcs("0");
            report.setLc0("0");
            report.setCx0("0");
            report.setLc1("0");
            report.setCx1("0");
            report.setLc2("0");
            report.setCx2("0");
            report.setLc3("0");
            report.setCx3("0");
            report.setLc4("0");
            report.setCx4("0");
            report.setLc5("0");
            report.setCx5("0");
            report.setLc6("0");
            report.setCx6("0");
            report.setLc7("0");
            report.setCx7("0");
            report.setLc8("0");
            report.setCx8("0");
            report.setLc9("0");
            report.setCx9("0");
            report.setLc10("0");
            report.setCx10("0");
            report.setLc11("0");
            report.setCx11("0");
            report.setLc12("0");
            report.setCx12("0");
            report.setLc13("0");
            report.setCx13("0");
            report.setLc14("0");
            report.setCx14("0");
            report.setLc15("0");
            report.setCx15("0");
            report.setLc16("0");
            report.setCx16("0");
            report.setLc17("0");
            report.setCx17("0");
            report.setLc18("0");
            report.setCx18("0");
            report.setLc19("0");
            report.setCx19("0");
            report.setLc20("0");
            report.setCx20("0");
            report.setLc21("0");
            report.setCx21("0");
            report.setLc22("0");
            report.setCx22("0");
            report.setLc23("0");
            report.setCx23("0");
            report.setDsLc("0");
            report.setDsSc("0");
            reports.add(report);
        }
        if(CollectionUtils.isNotEmpty(reports)){
            reportService.saveBatch(reports);
        }

    }

}
