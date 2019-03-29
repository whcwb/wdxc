package com.ldz.wechat.module.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * 用户意见反馈表
 */
@Table(name = "SYS_YJFK")
public class SysYjfk implements Serializable {
    @Id
    @Column(name = "YJ_ID")
    private String yjId;

    @Column(name = "YH_ID")
    private String yhId;

    @Column(name = "NR")
    private String nr;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "ZT")
    private String zt;
    /**
     * yjLx 意见类型 ZDCLK0009
        00意见 10反馈 20投诉
     */
    @Column(name = "YJLX")
    private String yjlx;

    @Column(name = "CLJG")
    private String cljg;

    @Column(name = "LXRXM")
    private String lxrxm;

    @Column(name = "LXFS")
    private String lxfs;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "XGSJ")
    private Date xgsj;
    /**
     * 司机ID
     */
    @Column(name = "SJ_ID")
    private String sjId;
    /**
     * 教职工ID
     */
    @Column(name = "USER_ID")
    private String userId;


    private static final long serialVersionUID = 1L;

    public String getSjId() {
        return sjId;
    }

    public void setSjId(String sjId) {
        this.sjId = sjId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return YJ_ID
     */
    public String getYjId() {
        return yjId;
    }

    /**
     * @param yjId
     */
    public void setYjId(String yjId) {
        this.yjId = yjId;
    }

    /**
     * @return YH_ID
     */
    public String getYhId() {
        return yhId;
    }

    /**
     * @param yhId
     */
    public void setYhId(String yhId) {
        this.yhId = yhId;
    }

    /**
     * @return NR
     */
    public String getNr() {
        return nr;
    }

    /**
     * @param nr
     */
    public void setNr(String nr) {
        this.nr = nr;
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
     * @return ZT
     */
    public String getZt() {
        return zt;
    }

    /**
     * @param zt
     */
    public void setZt(String zt) {
        this.zt = zt;
    }

    /**
     * @return YJLX
     */
    public String getYjlx() {
        return yjlx;
    }

    /**
     * @param yjlx
     */
    public void setYjlx(String yjlx) {
        this.yjlx = yjlx;
    }

    /**
     * @return CLJG
     */
    public String getCljg() {
        return cljg;
    }

    /**
     * @param cljg
     */
    public void setCljg(String cljg) {
        this.cljg = cljg;
    }

    /**
     * @return LXRXM
     */
    public String getLxrxm() {
        return lxrxm;
    }

    /**
     * @param lxrxm
     */
    public void setLxrxm(String lxrxm) {
        this.lxrxm = lxrxm;
    }

    /**
     * @return LXFS
     */
    public String getLxfs() {
        return lxfs;
    }

    /**
     * @param lxfs
     */
    public void setLxfs(String lxfs) {
        this.lxfs = lxfs;
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

    public enum InnerColumn {
        yjId("YJ_ID"),
        yhId("YH_ID"),
        nr("NR"),
        cjsj("CJSJ"),
        cjr("CJR"),
        zt("ZT"),
        yjlx("YJLX"),
        cljg("CLJG"),
        lxrxm("LXRXM"),
        lxfs("LXFS"),
        xgr("XGR"),

        userId("USER_ID"),
        sjId("SJ_ID"),

        xgsj("XGSJ");

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