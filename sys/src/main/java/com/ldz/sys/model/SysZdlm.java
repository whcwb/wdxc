package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "SYS_ZDLM")
public class SysZdlm implements Serializable {
    @Id
    @Column(name = "LMDM")
    private String lmdm;

    @Column(name = "LMMC")
    private String lmmc;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "QZ")
    private Double qz;

    @Transient
    private List<SysZdxm> zdxmList;

    public List<SysZdxm> getZdxmList() {
        return zdxmList;
    }

    public void setZdxmList(List<SysZdxm> zdxmList) {
        this.zdxmList = zdxmList;
    }

    private static final long serialVersionUID = 1L;

    /**
     * @return LMDM
     */
    public String getLmdm() {
        return lmdm;
    }

    /**
     * @param lmdm
     */
    public void setLmdm(String lmdm) {
        this.lmdm = lmdm;
    }

    /**
     * @return LMMC
     */
    public String getLmmc() {
        return lmmc;
    }

    /**
     * @param lmmc
     */
    public void setLmmc(String lmmc) {
        this.lmmc = lmmc;
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
     * @return QZ
     */
    public Double getQz() {
        return qz;
    }

    /**
     * @param qz
     */
    public void setQz(Double qz) {
        this.qz = qz;
    }

    public enum InnerColumn {
        lmdm("LMDM"),
        lmmc("LMMC"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        qz("QZ");

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