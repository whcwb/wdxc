package com.ldz.api.util;

import com.ldz.util.bean.RequestCommonParamsDto;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * auther chenwei
 * create at 2019/2/2
 */
public class TestJson {
    public static void main(String[] args)throws Exception {
        String zdbh = "159483726";
        String[] gpsArray = new String[]{
        };
        // TODO Auto-generated method stub
        Map<String, String> postHeaders = new HashMap<>();
        postHeaders.put("Content-type", "application/json");
        for (int i=0; i<gpsArray.length; i++){
            RequestCommonParamsDto dto = new RequestCommonParamsDto();
            dto.setDeviceId(zdbh);
            dto.setStartTime(DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            dto.setEndTime(dto.getStartTime());
            dto.setSczt("10");
            dto.setFxj(gpsArray[i].split(",")[3]);
            dto.setSpeed(gpsArray[i].split(",")[2]);
            dto.setLatitude(gpsArray[i].split(",")[1]);
            dto.setLongitude(gpsArray[i].split(",")[0]);
            //logger.debug("请求了GPS上传的方法");

            String result = HttpUtil.postJson("http://119.23.242.234:9095/api/device/gps", postHeaders, JsonUtil.toJson(dto));

            System.out.println("响应结果："+result);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
