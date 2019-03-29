package com.ldz.sys.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "SYS_JGSQLB")
public class SysJgsq implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "JGDM")
    private String jgdm;

    @Column(name = "FWDM")
    private String fwdm;

    @Column(name = "GNDM")
    private String gndm;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "YXQ")
    private Date yxq;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
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
     * @return FWDM
     */
    public String getFwdm() {
        return fwdm;
    }

    /**
     * @param fwdm
     */
    public void setFwdm(String fwdm) {
        this.fwdm = fwdm;
    }

    /**
     * @return GNDM
     */
    public String getGndm() {
        return gndm;
    }

    /**
     * @param gndm
     */
    public void setGndm(String gndm) {
        this.gndm = gndm;
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
     * @return YXQ
     */
    public Date getYxq() {
        return yxq;
    }

    /**
     * @param yxq
     */
    public void setYxq(Date yxq) {
        this.yxq = yxq;
    }

    public enum InnerColumn {
        id("ID"),
        jgdm("JGDM"),
        fwdm("FWDM"),
        gndm("GNDM"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        yxq("YXQ");

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