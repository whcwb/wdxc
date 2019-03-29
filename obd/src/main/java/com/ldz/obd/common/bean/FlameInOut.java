package com.ldz.obd.common.bean;

import lombok.Getter;
import lombok.Setter;

/**
 * auther chenwei
 * create at 2018/11/11
 */
@Getter
@Setter
public class FlameInOut {
    private String deviceId;
    private String type;// 1:点火，0：熄火
    private String travelId; // 行程id
    private String time;
    private String lng;
    private String lat;

}
