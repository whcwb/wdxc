package com.ldz.dao.biz.bean;

import java.util.Date;

/**
 * 当前站点的车辆节点
 * Created by Administrator on 2018/3/30.
 */
public class ClClyxjlModel {
    /**
     * 车牌号码
     */
    private String cphm;

    /**
     * 车辆出站状态  0未出站
     */
    private String clczzt;

    /**
     * 创建时间
     */
    private Date cjsj;


    /**
     * 站点距离
     */
    private Short zdjl;
//
//    /**
//     * 站点ID
//     */
//    private String zdId;
//
//    /**
//     * 线路ID
//     */
//    private String xlId;

    public String getCphm() {
        return cphm;
    }

    public void setCphm(String cphm) {
        this.cphm = cphm;
    }

    public String  getClczzt() {
        return clczzt;
    }

    public void setClczzt(String clczzt) {
        this.clczzt = clczzt;
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    public Short getZdjl() {
        return zdjl;
    }

    public void setZdjl(Short zdjl) {
        this.zdjl = zdjl;
    }


}
