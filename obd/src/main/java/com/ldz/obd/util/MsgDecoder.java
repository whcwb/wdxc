package com.ldz.obd.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 报文消息处理
 * Created by Administrator on 2018/4/11.
 */
public class MsgDecoder {
    Logger log = LoggerFactory.getLogger("error_info");
    Logger accessLog = LoggerFactory.getLogger("access_info");
    private BitOperator bitOperator;
    private BCDOperater bcdOperater;

    public MsgDecoder() {
        this.bitOperator = new BitOperator();
        this.bcdOperater = new BCDOperater();
    }
    protected String parseStringFromBytes(byte[] data, int startIndex, int lenth) {
        return this.parseStringFromBytes(data, startIndex, lenth, null);
    }

    private String parseStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
        try {
            byte[] tmp = new byte[lenth];
            System.arraycopy(data, startIndex, tmp, 0, lenth);
            return new String(tmp, "UTF-8");
        } catch (Exception e) {
            log.error("解析字符串出错:{}", e.getMessage());
            e.printStackTrace();
            return defaultVal;
        }
    }

    public String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth) {
        String retString=this.parseBcdStringFromBytes(data, startIndex, lenth, null);
        if(StringUtils.isNotEmpty(retString)){
            if(retString.length()<lenth*2){
                retString= StringUtils.leftPad(retString,lenth*2,"0");
            }
        }
        return retString;
    }

    private String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
        try {
            byte[] tmp = new byte[lenth];
            System.arraycopy(data, startIndex, tmp, 0, lenth);
            return this.bcdOperater.bcd2String(tmp);
        } catch (Exception e) {
            log.error("解析BCD(8421码)出错:{}", e.getMessage());
            e.printStackTrace();
            return defaultVal;
        }
    }

    public int parseIntFromBytes(byte[] data, int startIndex, int length) {
        return this.parseIntFromBytes(data, startIndex, length, 0);
    }

    private int parseIntFromBytes(byte[] data, int startIndex, int length, int defaultVal) {
        try {
            // 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
            final int len = length > 4 ? 4 : length;
            byte[] tmp = new byte[len];
            System.arraycopy(data, startIndex, tmp, 0, len);
            return bitOperator.byteToInteger(tmp);
        } catch (Exception e) {
            log.error("解析整数出错:{}", e.getMessage());
            e.printStackTrace();
            return defaultVal;
        }
    }

}
