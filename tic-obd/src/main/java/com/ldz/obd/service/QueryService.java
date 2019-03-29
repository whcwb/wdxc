package com.ldz.obd.service;

import com.ldz.obd.bean.PackageData;

/**
 * Created by Administrator on 2018/4/11.
 */
public interface QueryService {
//请求/上传 GPS+OBD 混合信息
    void getGpsObdMessage(PackageData msg);
//    行程报告上传
    void uploadTravelItineraryMessage(PackageData msg);
//    发动机故障码上传
    void uploadFaultCodeMessage(PackageData msg);
//      设备在线(开机)状态
    void deviceOnLineType(PackageData msg);
}
