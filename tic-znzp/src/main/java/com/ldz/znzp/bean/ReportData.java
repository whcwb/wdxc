package com.ldz.znzp.bean;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReportData {
    private String tid;
    private String routeName;
    private String routeId;
    private String direct;
    private String type;
    private String bus_plate;
    private String command = "reporting";
    private Short stationNo;
}
