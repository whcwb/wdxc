package com.ldz.znzp.bean;

import com.ldz.znzp.model.ClZd;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Station {
    private String stationNo;
    private String direct;
    private String stationName;
    private String longitude;
    private String latitude;
    private String runTime;

    public Station(ClZd zd){
        short order = zd.getRouteOrder() == null ? 1 :zd.getRouteOrder();
        this.stationNo = order < 0 ? "0"+order : ""+order;
        this.direct = zd.getYxfs();
        this.stationName = zd.getMc();
        this.longitude = String.valueOf(zd.getJd());
        this.latitude = String.valueOf(zd.getWd());
    }



}
