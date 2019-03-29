package com.ldz.api;

import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * auther chenwei
 * create at 2018/12/22
 */
public class Test1 {

    public static void main(String[] args) {
        String url = "http://iu.91bihu.com/api/CarInsurance/PostNewPrecisePrice";
        String json = "{\"LicenseNo\":\"鄂A88888\",\"CarOwnersName\":\"张三\",\"IdCard\":\"420381199111031218\",\"OwnerIdCardType\":\"1\",\"QuoteGroup\":\"1\",\"SubmitGroup\":\"1\",\"HolderIdCard\":\"420381199111031218\",\"HolderName\":\"张三\",\"HolderIdType\":\"1\",\"CityCode\":\"19\",\"EngineNo\":\"qawerw\",\"CarVin\":\"123\",\"RegisterDate\":\"2018-01-01\",\"MoldName\":\"奥迪\",\"ForceTax\":\"2\",\"BoLi\":\"0\",\"BuJiMianCheSun\":\"0\",\"BuJiMianDaoQiang\":\"0\",\"BuJiMianSanZhe\":\"0\",\"BuJiMianChengKe\":\"0\",\"BuJiMianSiJi\":\"0\",\"BuJiMianHuaHen\":\"0\",\"BuJiMianSheShui\":\"0\",\"BuJiMianZiRan\":\"0\",\"BuJiMianJingShenSunShi\":\"0\",\"SheShui\":\"0\",\"HuaHen\":\"0\",\"SiJi\":\"0\",\"ChengKe\":\"0\",\"CheSun\":\"0\",\"DaoQiang\":\"0\",\"SanZhe\":\"0\",\"ZiRan\":\"0\",\"QuoteParalelConflict\":\"0\",\"CustKey\":\"666666666666\",\"Agent\":\"76691\",\"SecCode\":\"009aa6b9c9d09c7f931996455a059a88\"}";

        Map<String,Object> param = JsonUtil.toMap(json);
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/x-www-form-urlencoded");
        try{
            String s = sendPost(url,param,"UTF-8");
            System.out.println(s);
//            String s = HttpUtil.postJson(url,headers,JsonUtil.toJson(param));
//            System.out.println(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String sendPost(String urlParam, Map<String, Object> params, String charset) {
        StringBuffer resultBuffer = null;
        // 构建请求参数
        StringBuffer sbParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sbParams.append(e.getKey());
                sbParams.append("=");
                sbParams.append(e.getValue());
                sbParams.append("&");
            }
        }
        HttpURLConnection con = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        // 发送请求
        try {
            URL url = new URL(urlParam);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (sbParams != null && sbParams.length() > 0) {
                osw = new OutputStreamWriter(con.getOutputStream(), charset);
                osw.write(sbParams.substring(0, sbParams.length() - 1));
                osw.flush();
            }
            // 读取返回内容
            resultBuffer = new StringBuffer();
            int contentLength = Integer.parseInt(con.getHeaderField("Content-Length"));
            if (contentLength > 0) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
                String temp;
                while ((temp = br.readLine()) != null) {
                    resultBuffer.append(temp);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    osw = null;
                    throw new RuntimeException(e);
                } finally {
                    if (con != null) {
                        con.disconnect();
                        con = null;
                    }
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                    throw new RuntimeException(e);
                } finally {
                    if (con != null) {
                        con.disconnect();
                        con = null;
                    }
                }
            }
        }

        return resultBuffer.toString();
    }

}
