package com.ldz.util.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LatestLocation {

    private Double latitude;

    private Double longitude;

    private Double radius;

    private String loc_time;

    private int direction;

    private Double speed;

    private Double height;

    private String floor;

    private String object_name;


}
