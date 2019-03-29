package com.ldz.dao.device.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "CL_XC")
public class ClXc implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 车辆行程开始时间
     */
    @Column(name = "XC_KSSJ")
    private String xcKssj;

    /**
     * 行程结束时间
     */
    @Column(name = "XC_JSSJ")
    private String xcJssj;

    /**
     * 终端编号
     */
    @Column(name = "CL_ZDBH")
    private String clZdbh;

    /**
     * 行程开始结束点
     */
    @Column(name = "XC_START_END")
    private String xcStartEnd;

    /**
     *   行程里程
     */
    @Column(name = "XC_LC")
    private String xcLc;

    @Column(name  = "XC_SC" )
    private String xcSc;

    private static final long serialVersionUID = 1L;

    public String getXcLc() {
        return xcLc;
    }

    public void setXcLc(String xcLc) {
        this.xcLc = xcLc;
    }

    public String getXcSc() {
        return xcSc;
    }

    public void setXcSc(String xcSc) {
        this.xcSc = xcSc;
    }

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取车辆行程开始时间
     *
     * @return XC_KSSJ - 车辆行程开始时间
     */
    public String getXcKssj() {
        return xcKssj;
    }

    /**
     * 设置车辆行程开始时间
     *
     * @param xcKssj 车辆行程开始时间
     */
    public void setXcKssj(String xcKssj) {
        this.xcKssj = xcKssj;
    }

    /**
     * 获取行程结束时间
     *
     * @return XC_JSSJ - 行程结束时间
     */
    public String getXcJssj() {
        return xcJssj;
    }

    /**
     * 设置行程结束时间
     *
     * @param xcJssj 行程结束时间
     */
    public void setXcJssj(String xcJssj) {
        this.xcJssj = xcJssj;
    }

    /**
     * 获取终端编号
     *
     * @return CL_ZDBH - 终端编号
     */
    public String getClZdbh() {
        return clZdbh;
    }

    /**
     * 设置终端编号
     *
     * @param clZdbh 终端编号
     */
    public void setClZdbh(String clZdbh) {
        this.clZdbh = clZdbh;
    }

    public String getXcStartEnd() {
        return xcStartEnd;
    }

    public void setXcStartEnd(String xcStartEnd) {
        this.xcStartEnd = xcStartEnd;
    }

    public enum InnerColumn {
        id("ID"),
        xcKssj("XC_KSSJ"),
        xcJssj("XC_JSSJ"),
        clZdbh("CL_ZDBH"),
        xcStartEnd("XC_START_END"),
        xcLc("XC_LC"),
        xcSc("XC_SC");

        private final String column;

        public String value() {
            return this.column;
        }

        public String getValue() {
            return this.column;
        }

        InnerColumn(String column) {
            this.column = column;
        }

        public String desc() {
            return this.column + " DESC";
        }

        public String asc() {
            return this.column + " ASC";
        }
    }
}