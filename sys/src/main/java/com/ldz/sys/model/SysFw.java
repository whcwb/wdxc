package com.ldz.sys.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "SYS_PTFW")
public class SysFw implements Serializable {
    @Id
    @Column(name = "FW_ID")
    private String fwId;

    @Column(name = "FWMC")
    private String fwmc;

    @Column(name = "FWDM")
    private String fwdm;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "ZT")
    private String zt;

    @Column(name = "API_QZ")
    private String apiQz;

    @Column(name = "TB")
    private String tb;

    @Transient
    private List<SysGn> functions;

    public List<SysGn> getFunctions() {
        return functions;
    }

    public void setFunctions(List<SysGn> functions) {
        this.functions = functions;
    }

    private static final long serialVersionUID = 1L;

    /**
     * @return FW_id
     */
    public String getFwId() {
        return fwId;
    }

    /**
     * @param fwId
     */
    public void setFwId(String fwId) {
        this.fwId = fwId;
    }

    /**
     * @return FWMC
     */
    public String getFwmc() {
        return fwmc;
    }

    /**
     * @param fwmc
     */
    public void setFwmc(String fwmc) {
        this.fwmc = fwmc;
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
     * @return API_QZ
     */
    public String getApiQz() {
        return apiQz;
    }

    /**
     * @param apiQz
     */
    public void setApiQz(String apiQz) {
        this.apiQz = apiQz;
    }

    /**
     * @return TB
     */
    public String getTb() {
        return tb;
    }

    /**
     * @param tb
     */
    public void setTb(String tb) {
        this.tb = tb;
    }

    public enum InnerColumn {
        fwId("FW_ID"),
        fwmc("FWMC"),
        fwdm("FWDM"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        zt("ZT"),
        apiQz("API_QZ"),
        tb("TB");

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