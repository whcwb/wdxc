package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_WXYH")
public class SysWxyh implements Serializable {
    @Id
    @Column(name = "OPENID")
    private String openid;

    @Column(name = "XM")
    private String xm;

    @Column(name = "DH")
    private String dh;

    @Column(name = "YHLX")
    private String yhlx;

    @Column(name = "XY")
    private String xy;

    @Column(name = "ZJHM")
    private String zjhm;

    @Column(name = "ZW")
    private String zw;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "JGDM")
    private String jgdm;

    @Column(name = "JGMC")
    private String jgmc;

    private static final long serialVersionUID = 1L;

    /**
     * @return OPENID
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * @return XM
     */
    public String getXm() {
        return xm;
    }

    /**
     * @param xm
     */
    public void setXm(String xm) {
        this.xm = xm;
    }

    /**
     * @return DH
     */
    public String getDh() {
        return dh;
    }

    /**
     * @param dh
     */
    public void setDh(String dh) {
        this.dh = dh;
    }

    /**
     * @return YHLX
     */
    public String getYhlx() {
        return yhlx;
    }

    /**
     * @param yhlx
     */
    public void setYhlx(String yhlx) {
        this.yhlx = yhlx;
    }

    /**
     * @return XY
     */
    public String getXy() {
        return xy;
    }

    /**
     * @param xy
     */
    public void setXy(String xy) {
        this.xy = xy;
    }

    /**
     * @return ZJHM
     */
    public String getZjhm() {
        return zjhm;
    }

    /**
     * @param zjhm
     */
    public void setZjhm(String zjhm) {
        this.zjhm = zjhm;
    }

    /**
     * @return ZW
     */
    public String getZw() {
        return zw;
    }

    /**
     * @param zw
     */
    public void setZw(String zw) {
        this.zw = zw;
    }

    /**
     * @return CJSJ
     */
    public Date getCjsj() {
        return cjsj;
    }

    /**
     * @param cjsj
     */
    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    /**
     * @return CJR
     */
    public String getCjr() {
        return cjr;
    }

    /**
     * @param cjr
     */
    public void setCjr(String cjr) {
        this.cjr = cjr;
    }

    /**
     * @return XGSJ
     */
    public Date getXgsj() {
        return xgsj;
    }

    /**
     * @param xgsj
     */
    public void setXgsj(Date xgsj) {
        this.xgsj = xgsj;
    }

    /**
     * @return XGR
     */
    public String getXgr() {
        return xgr;
    }

    /**
     * @param xgr
     */
    public void setXgr(String xgr) {
        this.xgr = xgr;
    }

    /**
     * @return JGDM
     */
    public String getJgdm() {
        return jgdm;
    }

    /**
     * @param jgdm
     */
    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }

    /**
     * @return JGMC
     */
    public String getJgmc() {
        return jgmc;
    }

    /**
     * @param jgmc
     */
    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public enum InnerColumn {
        openid("OPENID"),
        xm("XM"),
        dh("DH"),
        yhlx("YHLX"),
        xy("XY"),
        zjhm("ZJHM"),
        zw("ZW"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        jgdm("JGDM"),
        jgmc("JGMC");

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