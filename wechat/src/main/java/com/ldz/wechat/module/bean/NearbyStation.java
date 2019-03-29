package com.ldz.wechat.module.bean;

import com.ldz.wechat.module.model.ClZd;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * auther chenwei
 * create at 2018/8/4
 */
@Getter
@Setter
public class NearbyStation {
    private String id;
    private String xlId;
    private String name;
    private int distance;
    private double lng;
    private double lat;
    private List<Router> routerList;

    public NearbyStation(ClZd zd,double distance) {
        this.id = zd.getId();
        this.xlId = zd.getId();
        this.name = zd.getMc();
        this.lng = zd.getJd();
        this.lat = zd.getWd();
        this.distance = (int)distance;
        this.routerList = new ArrayList();
    }
}
