package com.ldz.obd.common.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceAlarm {
    private String deviceId;
    private String createTime;
    private String lng;
    private String lat;
}
