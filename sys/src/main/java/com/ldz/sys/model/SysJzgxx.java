package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_JZGXX")
public class SysJzgxx implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private String id;

    @Column(name = "XM")
    private String xm;

    @Column(name = "XB")
    private String xb;

    @Column(name = "ZW")
    private String zw;

    @Column(name = "ZJHM")
    private String zjhm;

    @Column(name = "JGDM")
    private String jgdm;

    @Column(name = "JDMC")
    private String jdmc;

    @Column(name = "ZGLX")
    private String zglx;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "SJHM")
    private String sjhm;

    private static final long serialVersionUID = 1L;


    public String getSjhm() {
        return sjhm;
    }

    public void setSjhm(String sjhm) {
        this.sjhm = sjhm;
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
     * @return XB
     */
    public String getXb() {
        return xb;
    }

    /**
     * @param xb
     */
    public void setXb(String xb) {
        this.xb = xb;
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
     * @return JDMC
     */
    public String getJdmc() {
        return jdmc;
    }

    /**
     * @param jdmc
     */
    public void setJdmc(String jdmc) {
        this.jdmc = jdmc;
    }

    /**
     * @return ZGLX
     */
    public String getZglx() {
        return zglx;
    }

    /**
     * @param zglx
     */
    public void setZglx(String zglx) {
        this.zglx = zglx;
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
        id("ID"),
        xm("XM"),
        xb("XB"),
        zw("ZW"),
        zjhm("ZJHM"),
        jgdm("JGDM"),
        jdmc("JDMC"),
        zglx("ZGLX"),
        cjr("CJR"),
        cjsj("CJSJ"),
        xgr("XGR"),
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