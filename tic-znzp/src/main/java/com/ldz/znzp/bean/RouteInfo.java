package com.ldz.znzp.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RouteInfo {
    private String command = "routeInfo";
    private String tid;
    private String showName;
    private List<Route> routes;

}
