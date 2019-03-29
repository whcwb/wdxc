package com.ldz.biz.config;

import com.ldz.dao.biz.model.*;
import com.ldz.service.biz.interfaces.*;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.SnowflakeIdWorker;
import com.ldz.util.gps.DistanceUtil;
import com.ldz.util.redis.RedisTemplateUtil;
import lombok.val;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ZdReportListener implements MessageListener {

    private RedisTemplateUtil redisTemplate;

    private ClZdglReportService reportService;

    private XcService xcService;

    private SbyxsjjlService sbyxsjjlService;

    private GpsLsService gpsLsService;

    private ZdglService zdglService;

    private SnowflakeIdWorker idWorker;

    private ClYyService yyService;

    public ZdReportListener(RedisTemplateUtil redisTemplate, ClZdglReportService reportService, XcService xcService, SbyxsjjlService sbyxsjjlService, GpsLsService gpsLsService, ZdglService zdglService, SnowflakeIdWorker idWorker, ClYyService yyService) {
        this.redisTemplate = redisTemplate;
        this.reportService = reportService;
        this.xcService = xcService;
        this.sbyxsjjlService = sbyxsjjlService;
        this.gpsLsService = gpsLsService;
        this.zdglService = zdglService;
        this.idWorker = idWorker;
        this.yyService = yyService;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        val redisChannel = redisTemplate.getStringSerializer().deserialize(message.getChannel());
        val eventMessage = redisTemplate.getValueSerializer().deserialize(message.getBody());

        String topic = redisChannel;
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(1);
        if (StringUtils.equals(topic, "zdReport")) {

            // 某条行程结束, 进行统计操作
            ClXc xc = (ClXc) eventMessage;
            String kssj = xc.getXcKssj().substring(0, 10);
            // 首先查找开始时间有没有统计记录
            SimpleCondition condition = new SimpleCondition(ClZdglReport.class);
            condition.eq(ClZdglReport.InnerColumn.rTjrq, kssj);
            condition.eq(ClZdglReport.InnerColumn.rZdbh, xc.getClZdbh());
            List<ClZdglReport> reports = reportService.findByCondition(condition);
            if (CollectionUtils.isNotEmpty(reports)) {
                // 已有记录 , 只需在原有基础上更新即可
                ClZdglReport report = reports.get(0);
                ClZdglReport zdglReport = updateReport(xc, kssj, report, format);
                reportService.update(zdglReport);
            } else {
                // 如果没有行程统计 , 则新建一条
                // 一个终端统计一条数据
                ClZdglReport report = new ClZdglReport();

                // 查询终端
                ClZdgl zdgl = zdglService.findById(xc.getClZdbh());
                // 终端编号和终端类型
                report.setrZdbh(zdgl.getZdbh());
                report.setrZdlx(zdgl.getZdLx());
                report.setJgdm(zdgl.getJgdm());
                report.setJgmc(zdgl.getJgmc());
                //统计日期
                report.setrTjrq(DateUtils.getDateStr(new Date(), "yyyy-MM-dd"));
                report.setId(idWorker.nextId() + "");
                ClZdglReport zdglReport = updateReport(xc, kssj, report, format);
                reportService.save(zdglReport);
            }

        }


    }



    /**
     * 根据怠速列表，计算怠速总时长和怠速里程
     *
     * @param slowSpeedList
     */
    private Map<String, Long> slowSpeed(List<Clyy> slowSpeedList) {
        Map<String, Long> result = new ConcurrentHashMap<>();
        if (CollectionUtils.isNotEmpty(slowSpeedList) && slowSpeedList.size() > 1) {
            long tSecond = 0, tMiles = 0;
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            //计算本次怠速总时长，单位：秒
            DateTime start = DateTime.parse(slowSpeedList.get(0).getLoc_time(), formatter);
            DateTime end = DateTime.parse(slowSpeedList.get(slowSpeedList.size() - 1).getLoc_time(), formatter);
            Duration s = new Duration(start, end);
            tSecond = s.getStandardSeconds();
            //两两GPS点进行距离计算，算出最终怠速里程数，单位：米
            for (int i = 0; i < slowSpeedList.size(); i++) {
                Clyy first = slowSpeedList.get(i);
                int sIndex = i + 1;
                if (sIndex >= slowSpeedList.size()) {
                    break;
                }
                Clyy second = slowSpeedList.get(sIndex);
                double miles = DistanceUtil.getLongDistance(first.getLongitude().doubleValue(), first.getLatitude().doubleValue(), second.getLongitude().doubleValue(), second.getLatitude().doubleValue());
                tMiles += miles;
            }
            result.put("second", tSecond);
            result.put("mile", tMiles);
        } else {
            result.put("second", 0l);
            result.put("mile", 0l);
        }

        return result;
    }

    private ClZdglReport updateReport(ClXc xc, String kssj, ClZdglReport report, NumberFormat format) {
        report.setrGxsj(DateUtils.getNowTime());
        // 当天里程总数 (取行程表中的总数相加)
        SimpleCondition xcCondition = new SimpleCondition(ClXc.class);
        xcCondition.like(ClXc.InnerColumn.xcKssj, kssj);
        xcCondition.eq(ClXc.InnerColumn.clZdbh, xc.getClZdbh());
        List<ClXc> xcs = xcService.findByCondition(xcCondition);
        if (CollectionUtils.isEmpty(xcs)) {
            return null;
        }
        // 怠速统计 对每条行程进行计算然后相加
        Long dsSec = 0L;
        Long dsLc = 0L;
        for (ClXc clXc : xcs) {
            SimpleCondition yyCondition = new SimpleCondition(Clyy.class);
            yyCondition.lte(Clyy.InnerColumn.speed, 5);
            yyCondition.lte(Clyy.InnerColumn.loc_time, clXc.getXcJssj());
            yyCondition.gte(Clyy.InnerColumn.loc_time, clXc.getXcKssj());
            yyCondition.eq(Clyy.InnerColumn.zdbh, clXc.getClZdbh());
            yyCondition.setOrderByClause(" id asc");
            List<Clyy> clyys = yyService.findByCondition(yyCondition);
            Map<String, Long> stringLongMap = slowSpeed(clyys);
            dsSec += stringLongMap.get("second");
            dsLc += stringLongMap.get("mile");
        }

        report.setDsSc(dsSec + "");
        report.setDsLc(dsLc + "");
        String lc;
        if (CollectionUtils.size(xcs) > 1) {
            lc = xcs.stream().map(ClXc::getXcStartEnd).filter(s -> s.split(",").length == 3).map(s -> s.split(",")[2]).reduce((s, s2) -> Double.parseDouble(s) + Double.parseDouble(s2) + "").get();
        } else {
            lc = xcs.get(0).getXcStartEnd().split(",")[2];
        }
        // 当天累计里程
        report.setrLczs(format.format(Double.parseDouble(lc)));
        // 驾驶总时长(分钟)
        Map<String, String> collect = xcs.stream().collect(Collectors.toMap(ClXc::getXcKssj, ClXc::getXcJssj));
        int sc = 0;
        for (String s : collect.keySet()) {
            try {
                Date start = DateUtils.getDate(s, "yyyy-MM-dd HH:mm:ss");
                Date end = DateUtils.getDate(collect.get(s), "yyyy-MM-dd HH:mm:ss");
                long sta = start.getTime();
                long en = end.getTime();
                sc += (en - sta) / (1000 * 60);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        report.setrJszsc(sc + "");
        // 行程总次数
        report.setrXcs(xcs.size() + "");
        // 最大行驶里程
        String maxLc;
        if (CollectionUtils.size(xcs) == 1) {
            maxLc = xcs.get(0).getXcStartEnd().split(",")[2];
        } else {
            maxLc = xcs.stream().map(ClXc::getXcStartEnd).map(s -> Double.parseDouble(s.split(",")[2])).max(Double::compareTo).get() + "";
        }

        report.setrZzxslc(format.format(Double.parseDouble(maxLc)));
        // 早高峰出行次数(比较开始时间)
        long zgf = xcs.stream().map(ClXc::getXcKssj).filter(s -> s.substring(11, s.length()).startsWith("07:") || s.substring(11, s.length()).startsWith("08:")).count();
        report.setrZgfcxcs(zgf + "");
        // 晚高峰出行次数
        long wgf = xcs.stream().map(ClXc::getXcKssj).filter(s -> s.substring(11, s.length()).startsWith("17:") || s.substring(11, s.length()).startsWith("18:")).count();
        report.setrWgfcxcs(wgf + "");
        // 深夜出行次数
        long sy = xcs.stream().map(ClXc::getXcKssj).filter(s -> s.substring(11, s.length()).startsWith("22:") || s.substring(11, s.length()).startsWith("23:")).count();
        report.setrSycxcs(sy + "");
        // 查询异常事件  10急加速，20急刹车，30急转弯 ，40超速
        SimpleCondition sjCondition = new SimpleCondition(ClSbyxsjjl.class);
        List<String> list = Arrays.asList("10", "20", "30", "40");
        sjCondition.in(ClSbyxsjjl.InnerColumn.sjlx, list);
        sjCondition.like(ClSbyxsjjl.InnerColumn.cjsj, kssj);
        sjCondition.eq(ClSbyxsjjl.InnerColumn.zdbh, xc.getClZdbh());
        List<ClSbyxsjjl> sbyxsjjls = sbyxsjjlService.findByCondition(sjCondition);
        Map<String, Long> sjLx = sbyxsjjls.stream().collect(Collectors.groupingBy(ClSbyxsjjl::getSjlx, Collectors.counting()));
        // 急加速次数
        report.setrJjs(sjLx.containsKey("10") ? sjLx.get("10") + "" : 0 + "");
        // 急减速次数
        report.setrJjss(sjLx.containsKey("20") ? sjLx.get("20") + "" : 0 + "");
        // 急转弯
        report.setrJzw(sjLx.containsKey("30") ? sjLx.get("30") + "" : 0 + "");
        // 超速
        report.setrCscs(sjLx.containsKey("40") ? sjLx.get("40") + "" : 0 + "");
        // 最高时速
        long maxSpeed = gpsLsService.getMaxSpeed(xc.getClZdbh(), kssj);
        report.setrZzss(maxSpeed + "");

        // 每个小时的里程 和 出行次数
        // 0 - 23 出行次数
        Map<String, Long> longMap = xcs.stream().collect(Collectors.groupingBy(clXc1 -> clXc1.getXcKssj().substring(11, 13), Collectors.counting()));

        report.setCx0("" + (longMap.get("00") == null ? 0 : longMap.get("00")));
        report.setCx1("" + (longMap.get("01") == null ? 0 : longMap.get("01")));
        report.setCx2("" + (longMap.get("02") == null ? 0 : longMap.get("02")));
        report.setCx3("" + (longMap.get("03") == null ? 0 : longMap.get("03")));
        report.setCx4("" + (longMap.get("04") == null ? 0 : longMap.get("04")));
        report.setCx5("" + (longMap.get("05") == null ? 0 : longMap.get("05")));
        report.setCx6("" + (longMap.get("06") == null ? 0 : longMap.get("06")));
        report.setCx7("" + (longMap.get("07") == null ? 0 : longMap.get("07")));
        report.setCx8("" + (longMap.get("08") == null ? 0 : longMap.get("08")));
        report.setCx9("" + (longMap.get("09") == null ? 0 : longMap.get("09")));
        report.setCx10("" + (longMap.get("10") == null ? 0 : longMap.get("10")));
        report.setCx11("" + (longMap.get("11") == null ? 0 : longMap.get("11")));
        report.setCx12("" + (longMap.get("12") == null ? 0 : longMap.get("12")));
        report.setCx13("" + (longMap.get("13") == null ? 0 : longMap.get("13")));
        report.setCx14("" + (longMap.get("14") == null ? 0 : longMap.get("14")));
        report.setCx15("" + (longMap.get("15") == null ? 0 : longMap.get("15")));
        report.setCx16("" + (longMap.get("16") == null ? 0 : longMap.get("16")));
        report.setCx17("" + (longMap.get("17") == null ? 0 : longMap.get("17")));
        report.setCx18("" + (longMap.get("18") == null ? 0 : longMap.get("18")));
        report.setCx19("" + (longMap.get("19") == null ? 0 : longMap.get("19")));
        report.setCx20("" + (longMap.get("20") == null ? 0 : longMap.get("20")));
        report.setCx21("" + (longMap.get("21") == null ? 0 : longMap.get("21")));
        report.setCx22("" + (longMap.get("22") == null ? 0 : longMap.get("22")));
        report.setCx23("" + (longMap.get("23") == null ? 0 : longMap.get("23")));

        // 0 - 23 里程总数
        Map<String, List<ClXc>> listMap = xcs.stream().collect(Collectors.groupingBy(clXc1 -> clXc1.getXcKssj().substring(11, 13)));
        report.setLc0(format.format(Double.parseDouble("" + (listMap.get("00") == null ? 0 : listMap.get("00").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc1(format.format(Double.parseDouble("" + (listMap.get("01") == null ? 0 : listMap.get("01").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc2(format.format(Double.parseDouble("" + (listMap.get("02") == null ? 0 : listMap.get("02").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc3(format.format(Double.parseDouble("" + (listMap.get("03") == null ? 0 : listMap.get("03").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc4(format.format(Double.parseDouble("" + (listMap.get("04") == null ? 0 : listMap.get("04").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc5(format.format(Double.parseDouble("" + (listMap.get("05") == null ? 0 : listMap.get("05").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc6(format.format(Double.parseDouble("" + (listMap.get("06") == null ? 0 : listMap.get("06").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc7(format.format(Double.parseDouble("" + (listMap.get("07") == null ? 0 : listMap.get("07").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc8(format.format(Double.parseDouble("" + (listMap.get("08") == null ? 0 : listMap.get("08").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc9(format.format(Double.parseDouble("" + (listMap.get("09") == null ? 0 : listMap.get("09").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc10(format.format(Double.parseDouble("" + (listMap.get("10") == null ? 0 : listMap.get("10").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc11(format.format(Double.parseDouble("" + (listMap.get("11") == null ? 0 : listMap.get("11").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc12(format.format(Double.parseDouble("" + (listMap.get("12") == null ? 0 : listMap.get("12").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc13(format.format(Double.parseDouble("" + (listMap.get("13") == null ? 0 : listMap.get("13").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc14(format.format(Double.parseDouble("" + (listMap.get("14") == null ? 0 : listMap.get("14").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc15(format.format(Double.parseDouble("" + (listMap.get("15") == null ? 0 : listMap.get("15").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc16(format.format(Double.parseDouble("" + (listMap.get("16") == null ? 0 : listMap.get("16").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc17(format.format(Double.parseDouble("" + (listMap.get("17") == null ? 0 : listMap.get("17").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc18(format.format(Double.parseDouble("" + (listMap.get("18") == null ? 0 : listMap.get("18").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc19(format.format(Double.parseDouble("" + (listMap.get("19") == null ? 0 : listMap.get("19").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc20(format.format(Double.parseDouble("" + (listMap.get("20") == null ? 0 : listMap.get("20").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc21(format.format(Double.parseDouble("" + (listMap.get("21") == null ? 0 : listMap.get("21").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc22(format.format(Double.parseDouble("" + (listMap.get("22") == null ? 0 : listMap.get("22").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        report.setLc23(format.format(Double.parseDouble("" + (listMap.get("23") == null ? 0 : listMap.get("23").stream().map(clXc1 -> Double.parseDouble(clXc1.getXcStartEnd().split(",")[2])).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get()))));
        return report;
    }





}
