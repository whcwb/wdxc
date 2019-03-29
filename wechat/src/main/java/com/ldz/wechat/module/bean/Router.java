package com.ldz.wechat.module.bean;

import com.ldz.wechat.module.model.ClXl;
import com.ldz.wechat.module.model.ClZd;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * auther chenwei
 * create at 2018/8/4
 */
@Getter
@Setter
public class Router {
    private String id;
    private String name;
    private String startTime;
    private String endTime;
    private ClZd endStation;
    private String direct;
    private Short order;
    private List<Integer> nextBus;

    public Router(){

    }

    public Router(ClXl xl,ClZd endStation) {
        this.id = xl.getId();
        this.name = xl.getXlmc();
        this.startTime = xl.getYxkssj();
        this.endTime = xl.getYxjssj();
        this.endStation = endStation;
        this.direct = xl.getYxfs();

    }
}
