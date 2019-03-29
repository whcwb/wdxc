package com.ldz.geo.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * auther chenwei
 * create at 2018/8/4
 */
@Getter
@Setter
public class GeoModel {
    private String key;
    private String name;
    private Double lat;
    private Double lng;
    private Long expire;
    private TimeUnit timeUnit;

}
