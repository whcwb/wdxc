package com.ldz.dwq.handler.biz;

import com.ldz.dao.dwq.model.GpsBean;
import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * auther chenwei
 * create at 2018/11/19
 */
@Component
public class Test {

    @Autowired
    protected RedisTemplateUtil redisDao;

    public void test(String path,long p){
        try {
//            path = "/Users/chenwei/Downloads/access_info1.log";
            List<String> lines = FileUtils.readLines(new File(path));
            int i = 0;
            int t = lines.size();
            for (String line : lines) {
                i++;
                System.out.println("第 "+i+" 个,"+(i*100/t)+"%");
                if (line.length() < 100)continue;
                int index = line.indexOf("[C4,");
                if (index < 0)continue;
                String content = line.substring(index + 1);
                String[] dataArray = content.split(",");
                GpsBean gps = new GpsBean();
                gps.setXxlx(dataArray[4]);
                gps.setLcs(dataArray[9]);
                //接收终端时间是0时区的，需要先转换再存储
                String time = DateTime.now().parse(dataArray[10], DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()).withZone(DateTimeZone.forID("Asia/Shanghai")).toLocalDateTime().toString("yyyy-MM-dd HH:mm:ss");
                gps.setTime(time);
                if (StringUtils.isNotBlank(dataArray[13])){
                    //GPS定位
                    String[] gpsArray = dataArray[13].split("\\|");
                    gps.setJd(gpsArray[0]);
                    gps.setWd(gpsArray[1]);
                    gps.setSd(gpsArray[2]);
                    if (gpsArray.length > 3){
                        gps.setFx(gpsArray[3]);
                        gps.setHb(gpsArray[4]);
                    }
                }else if (StringUtils.isNotBlank(dataArray[14])){
                    //WIFI定位
                    gps.setWifi(dataArray[14]);
                }else if (StringUtils.isNotBlank(dataArray[15])){
                    //LBS定位
                    gps.setLbs(dataArray[15]);
                }
                //将GPS存储到List集合中
                //redisDao.boundListOps(GpsBean.class.getName() + "-" + messageBean.getImei()).leftPush(gps);

                //推送GPS数据到biz中处理
                RequestCommonParamsDto dto = new RequestCommonParamsDto();
                String deviceId = "866401020000050";
                dto.setDeviceId(deviceId);
                dto.setLatitude(gps.getWd());
                dto.setLongitude(gps.getJd());
                dto.setStartTime(gps.getTime());
                dto.setFxj(gps.getFx());
                dto.setSpeed(StringUtils.isEmpty(gps.getSd()) ? "0" : gps.getSd());
                if (dto.getSpeed().contains(".")){
                    dto.setSpeed(""+Math.round(new Float(dto.getSpeed())));
                }
                dto.setEndTime(gps.getTime());
                dto.setEventType(getEventType(dataArray[6]));
                dto.setSczt("10");

                redisDao.convertAndSend("gps", dto);
                if (p == 0) p = 500;
                Thread.sleep(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private String getEventType(String s){
        switch (s){
            case "0":
                return null;
            case "1":
                return "DE01";
            case "2":
                return "DE02";
            case "3":
                return "DE03";
            case "4":
                return "DE04";
            case "5":
                return "DE05";
            case "6":
                return "DE06";
            case "7":
                return "10";
            case "8":
                return "20";
            case "9":
                return "30";
            case "10":
                return "DE10";
            case "11":
                return "60";
            case "12":
                return "50";
        }
        return null;
    }
//    public static void main(String[] args) {
//        String s = "2018-11-19 15:31:08.065 INFO  access_info   -- ͨ��[6f4b9d48]��������:[C4,481,866401020000050,1,0,93,4.01,0,0,5547,2018-11-19 07:31:06,460,0,114.040075|22.694148|17.724|212.14|31.6,,9375#4151#-75|9375#3693#-86|9375#4152#-86|9375#4071#-91|9375#4072#-96\n";
//        System.out.println(s.indexOf("[C4,"));
//    }
}
