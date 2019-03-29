package com.ldz.znzp.bean;

import com.ldz.znzp.model.ClXl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Route {
    private String routeId;
    private String routeName;
    private String startTime;
    private String endTime;
    private String routeType;
    private String runTime;
    private List<Station> stations;
    private List<Bus> buses;

    public Route(ClXl xl){
        if (xl == null)return;
        this.routeId = xl.getId();
        this.routeName = xl.getXlmc();
        this.startTime = xl.getYxkssj();
        this.endTime = xl.getYxjssj();
        this.routeType = xl.getLx();
        this.runTime = String.valueOf(xl.getPjsj());
    }

}
