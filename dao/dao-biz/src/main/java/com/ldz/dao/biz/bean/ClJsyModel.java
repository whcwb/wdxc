package com.ldz.dao.biz.bean;

import java.io.Serializable;
import java.util.List;

import com.ldz.dao.biz.model.ClDd;

/**
 * 驾驶员表自定义类型
 */
public class ClJsyModel implements Serializable {
    /**
     * 身份证号码
     */
    private String sfzhm;

    /**
     * 姓名
     */
    private String xm;
    /**
     * 车队编号
     */
    private String cdbh;

    /**
     * 手机号
     */
    private String sjh;
    /**
     * 车型
     */
    private String cx;
    /**
     * 车牌号
     */
    private String cph;
    /**
     * 载客量
     */
    private String zkl;
    /**
     * 当天已完成的订单数
     */
    private long endOrderCount;
    /**
     * 当天未完成的订单
     */
    private long startOrderCount;

    /**
     * 状态
     */
    private String zt;

    private List<ClDd> clDdList;

    private static final long serialVersionUID = 1L;

    public long getEndOrderCount() {
        return endOrderCount;
    }

    public void setEndOrderCount(long endOrderCount) {
        this.endOrderCount = endOrderCount;
    }

    public long getStartOrderCount() {
        return startOrderCount;
    }

    public void setStartOrderCount(long startOrderCount) {
        this.startOrderCount = startOrderCount;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public List<ClDd> getClDdList() {
        return clDdList;
    }

    public void setClDdList(List<ClDd> clDdList) {
        this.clDdList = clDdList;
    }

    public String getSfzhm() {
        return sfzhm;
    }

    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getCdbh() {
        return cdbh;
    }

    public void setCdbh(String cdbh) {
        this.cdbh = cdbh;
    }

    public String getSjh() {
        return sjh;
    }

    public void setSjh(String sjh) {
        this.sjh = sjh;
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }

    public String getZkl() {
        return zkl;
    }

    public void setZkl(String zkl) {
        this.zkl = zkl;
    }
}