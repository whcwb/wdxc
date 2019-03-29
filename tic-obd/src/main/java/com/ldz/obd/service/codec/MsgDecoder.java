package com.ldz.obd.service.codec;

import com.ldz.obd.bean.PackageData;
import com.ldz.obd.util.BCDOperater;
import com.ldz.obd.util.BitOperator;
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

    //字节数组到消息体实体类
    public PackageData queueElement2PackageData(byte[] data) {
        PackageData ret = new PackageData();

//        1、封装报文
//        1-1、equipmentId 设备ID  6byte  12 个 ASCII 字符，BCD 码表示
        ret.setEquipmentId(this.parseBcdStringFromBytes(data, 1, 6));
//        1-2、命令字 2byte orderCode
        ret.setOrderCode(this.parseIntFromBytes(data, 7, 2));
//        1-3、指令长度 2byte bodyLength
        ret.setBodyLength(this.parseIntFromBytes(data, 9, 2));
//        1-4、消息体字节数组 byte[] bodyBytes;
        if(ret.getBodyLength()>0){
            byte[] tmp = new byte[ret.getBodyLength()];
            System.arraycopy(data, 11, tmp, 0, tmp.length);
            ret.setBodyBytes(tmp);
        }
//        1-5、校验码 1byte
        int checkSumInPkg = data[data.length - 2];
        ret.setCheckSum(checkSumInPkg);

        int calculatedCheckSum = this.bitOperator.getCheckSum4JT808(data, 1, data.length - 2);
        if (checkSumInPkg != calculatedCheckSum) {
            ret.setCheckType(false);
            log.warn("检验码不一致,设备ID:{},报文中的校验码(转为十进制后):{},做了异或后的值：{}>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", ret.getEquipmentId(), checkSumInPkg, calculatedCheckSum);
            accessLog.debug("检验码不一致,设备ID:{},报文中的校验码(转为十进制后):{},做了异或后的值：{}>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", ret.getEquipmentId(), checkSumInPkg, calculatedCheckSum);
        }else {
            log.warn("检验码通过--------------");
            ret.setCheckType(true);
        }
        return ret;
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
