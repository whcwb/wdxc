package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_XXTS")
public class SysXxts implements Serializable {
    @Id
    @Column(name = "XX_ID")
    private String xxId;

    @Column(name = "XXNR")
    private String xxnr;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "KJFW")
    private String kjfw;

    @Column(name = "KSSJ")
    private Date kssj;

    @Column(name = "JSSJ")
    private Date jssj;

    private static final long serialVersionUID = 1L;

    /**
     * @return XX_ID
     */
    public String getXxId() {
        return xxId;
    }

    /**
     * @param xxId
     */
    public void setXxId(String xxId) {
        this.xxId = xxId;
    }

    /**
     * @return XXNR
     */
    public String getXxnr() {
        return xxnr;
    }

    /**
     * @param xxnr
     */
    public void setXxnr(String xxnr) {
        this.xxnr = xxnr;
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
     * @return KJFW
     */
    public String getKjfw() {
        return kjfw;
    }

    /**
     * @param kjfw
     */
    public void setKjfw(String kjfw) {
        this.kjfw = kjfw;
    }

    /**
     * @return KSSJ
     */
    public Date getKssj() {
        return kssj;
    }

    /**
     * @param kssj
     */
    public void setKssj(Date kssj) {
        this.kssj = kssj;
    }

    /**
     * @return JSSJ
     */
    public Date getJssj() {
        return jssj;
    }

    /**
     * @param jssj
     */
    public void setJssj(Date jssj) {
        this.jssj = jssj;
    }

    public enum InnerColumn {
        xxId("XX_ID"),
        xxnr("XXNR"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        kjfw("KJFW"),
        kssj("KSSJ"),
        jssj("JSSJ");

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