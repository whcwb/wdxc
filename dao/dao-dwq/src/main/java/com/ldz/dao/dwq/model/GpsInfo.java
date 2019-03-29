package com.ldz.dao.dwq.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * auther chenwei
 * create at 2019/1/12
 */
@Getter
@Setter
@ToString
public class GpsInfo implements Serializable {
    private static final long serialVersionUID = 7841064927166186454L;
    // 时间
    private String time;
    //经度.精确到小数点后 6 位，负号表示西半球
    private String jd;
    //纬度.精确到小数点后 6 位，负号表示南半球
    private String wd;
    //速度.单位：公里/小时
    private String sd;
    //方向.取值范围：0-359.9
    private String fx;
    //海拔高度
    private String hb;
    // 可见星个数
    private String kjwxgs;
    // 正在使用的卫星个数
    private String sywxgs;

}
