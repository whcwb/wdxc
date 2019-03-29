package com.ldz.util.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TrackPointForReturn {

    /**
     * 用户的ak，授权使用 (必填)
     */
    private String ak;  //2pVOrCuBldNDOgDtwaYSP8gpQ2VQdZY9

    /**
     * servicede ID，作为其唯一标识 (必填)
     */
    private int service_id;  //200383

    /**
     * entity唯一标识 (必填)
     */
    private String entity_name; //CWB201805181744

    /**
     * 纬度 (必填)
     */
    private Double latitude;    //30.5427744502

    /**
     * 经度 (必填)
     */
    private Double longitude;   //114.2618477088

    /**
     * 定位时设备的时间 (必填)  Unix 时间戳
     * 输入的loc_time不能大于当前服务端时间10分钟以上，即不支持存未来的轨迹点。
     */
    private Long loc_time;    //1526637383

    /**
     * 坐标类型 (坐标类型)  (必填)
     * 默认值：bd09ll
     * 该字段用于描述上传的坐标类型。可选值为：
     * wgs84：GPS 坐标
     * gcj02：国测局加密坐标
     * bd09ll：百度经纬度坐标
     */
    private String coord_type_input;    //wgs84


    private String error;

}
