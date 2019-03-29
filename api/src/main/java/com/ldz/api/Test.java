package com.ldz.api;

import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.HttpUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * auther chenwei
 * create at 2018/12/16
 */
public class Test {
    static String logPath = "C:\\Users\\李彬彬\\Desktop\\http.log";
    static int count = 0;
    static int total = 3600;
    static Timer timer;
    static int threadNum = 10;
    public static void main(String[] args) {
        start();
    }
    public static void method2( String conent) {
        BufferedWriter out = null;
        String d = DateUtils.getNowTime();
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(logPath, true)));
            out.write(d +"--"+conent+"\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static void start(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startOne();
            }
        },100,1000);
    }


    private static void startOne(){
        if (count ++ > total){
            timer.cancel();
            System.exit(0);
        }
        for (int i = 0; i < threadNum; i++) {
            Thread t = new Thread(new PostReq());
            t.start();
        }
    }

    private static class PostReq implements Runnable{
        @Override
        public void run() {
            Map<String,String> headers = new HashMap<>();
            headers.put("Content-Type","application/json");
            String entity = "{\"id\":\"0\",\"deviceId\":\"865923030018219\",\"channelId\":\"b35511d72fe74682293577c417db5713\",\"deviceTag\":\"89860617070009839712\",\"startTime\":\"2018-12-08 08:49:35\",\"endTime\":\"2018-12-08 08:49:35\",\"longitude\":\"114.35193416667\",\"latitude\":\"30.53621266667\",\"speed\":\"0\",\"sczt\":\"20\",\"cmdParams\":\"{\\\"versionCode\\\":16,\\\"versionName\\\":\\\"0.2.4\\\"}\",\"gpsjd\":\"1\",\"fxj\":\"0.0\"}";
            try {
                String res = HttpUtil.postJson("http://cheliang.court.gov.cn/ticserver/api/device/gps",headers,entity);
                method2(res);
            } catch (Exception e) {
                e.printStackTrace();
                method2(e.getMessage());
            }
        }
    }
}
