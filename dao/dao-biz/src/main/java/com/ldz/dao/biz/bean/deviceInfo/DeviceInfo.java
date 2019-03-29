package com.ldz.dao.biz.bean.deviceInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceInfo {
    private String deviceId;
    private double lng;
    private double lat;
    private String status;
    private String createTime;
    private String updateTime;

}
