package com.ldz.util.commonUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 手机短信下发
 * Created by Administrator on 2018/5/9.
 */
public class SMSUtils {
    /**
     * 手机短信下发
     * @param appid  申请的appid
     * @param appkey  应用的密码key
     * @param tmpl  模板id
     * @return
     */
    public static Boolean sendSMS(String appid,String appkey,String tmpl){
        JSONArray tel = new JSONArray();
        JSONObject telElement = new JSONObject();
        telElement.put("mobile", "13825250100"); //填写需要的手机号码
        tel.add(telElement);
        JSONObject data = new JSONObject();
        data.put("tel", tel);
        data.put("Appid", appid); //申请的appid
        data.put("Appkey", appkey); //应用的密码key
        data.put("Tmpl", tmpl); //模板id（由信息中心申请，为0则自定义内容发送，不需要模板，param只用put一个即可）
        JSONObject param1 = new JSONObject();
        param1.put("param", "验证码");  //变量一
        JSONObject param2 = new JSONObject();
        param2.put("param", "5698");  //变量二
        JSONObject param3 = new JSONObject();
        param3.put("param", "15分钟"); //变量三
        JSONArray content = new JSONArray();
        content.add(param1);
        content.add(param2);
        content.add(param3);
        System.out.println(data);

        //post发送到服务器端
        String wholeurl = "http://10.131.4.40:8080/SMS/AppuserAction.action"; //服务器短信发送接口
        URL object = null;
        HttpURLConnection conn;
        try {
            object = new URL(wholeurl);
            conn = (HttpURLConnection) object.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",  "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            wr.write(data.toString());
            wr.flush();
            //接收服务器发来的回复
            StringBuilder sb = new StringBuilder();
            int httpRspCode = conn.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                System.out.println(sb+"123456");    //将服务器回复输出
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public static void main(String[] args) {
//        sendSMS("123");
        System.out.println("123");
    }
}
