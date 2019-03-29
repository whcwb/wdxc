package com.ldz.sys.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_HSGS")
public class SysHsgs implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "CX")
    private String cx;

    @Column(name = "LX")
    private String lx;

    @Column(name = "NR")
    private String nr;

    @Column(name = "JE")
    private BigDecimal je;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "XGSJ")
    private Date xgsj;

    private static final long serialVersionUID = 1L;

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
     * @return CX
     */
    public String getCx() {
        return cx;
    }

    /**
     * @param cx
     */
    public void setCx(String cx) {
        this.cx = cx;
    }

    /**
     * @return LX
     */
    public String getLx() {
        return lx;
    }

    /**
     * @param lx
     */
    public void setLx(String lx) {
        this.lx = lx;
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
     * @return JE
     */
    public BigDecimal getJe() {
        return je;
    }

    /**
     * @param je
     */
    public void setJe(BigDecimal je) {
        this.je = je;
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
        cx("CX"),
        lx("LX"),
        nr("NR"),
        je("JE"),
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