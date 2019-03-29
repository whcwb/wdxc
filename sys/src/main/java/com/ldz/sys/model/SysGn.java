package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Table(name = "SYS_FWGN")
public class SysGn implements Serializable {
    @Id
    @Column(name = "GNDM")
    @NotNull(message="功能代码不能为空")
    private String gndm;

    @Column(name = "GNMC")
    @NotNull(message="功能名称不能为空")
    private String gnmc;

    @Column(name = "FWDM")
    @NotNull(message="服务代码不能为空")
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
    @NotNull(message="功能状态不能为空")
    private String zt;

    @Column(name = "BZ")
    private String bz;

    @Column(name = "URL")
    @NotNull(message="URL不能为空")
    private String url;

    @Column(name = "FJD")
    private String fjd;

    @Column(name = "TZDZ")
    @NotNull(message="跳转地址不能为空")
    private String tzdz;

    @Column(name = "TB")
    private String tb;

    @Column(name = "API_QZ")
    @NotNull(message="API前缀不能为空")
    private String apiQz;

    @Column(name = "API_HZ")
    @NotNull(message="api后缀不能为空")
    private String apiHz;

    @Column(name = "PX")
    @NotNull(message="排序不能为空")
    private Float px;

    @Transient
    private String title;

    @Transient
    private String checked;

    @Transient
    private List<SysGn> children;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public List<SysGn> getChildren() {
        return children;
    }

    public void setChildren(List<SysGn> children) {
        this.children = children;
    }

    private static final long serialVersionUID = 1L;

    public Float getPx() {
        return px;
    }

    public void setPx(Float px) {
        this.px = px;
    }

    public String getTitle() {
        return gnmc;
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
     * @return GNMC
     */
    public String getGnmc() {
        return gnmc;
    }

    /**
     * @param gnmc
     */
    public void setGnmc(String gnmc) {
        this.gnmc = gnmc;
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
     * @return BZ
     */
    public String getBz() {
        return bz;
    }

    /**
     * @param bz
     */
    public void setBz(String bz) {
        this.bz = bz;
    }

    /**
     * @return URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return FJD
     */
    public String getFjd() {
        return fjd;
    }

    /**
     * @param fjd
     */
    public void setFjd(String fjd) {
        this.fjd = fjd;
    }

    /**
     * @return TZDZ
     */
    public String getTzdz() {
        return tzdz;
    }

    /**
     * @param tzdz
     */
    public void setTzdz(String tzdz) {
        this.tzdz = tzdz;
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
     * @return API_HZ
     */
    public String getApiHz() {
        return apiHz;
    }

    /**
     * @param apiHz
     */
    public void setApiHz(String apiHz) {
        this.apiHz = apiHz;
    }

    public enum InnerColumn {
        gndm("GNDM"),
        gnmc("GNMC"),
        fwdm("FWDM"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        zt("ZT"),
        bz("BZ"),
        url("URL"),
        fjd("FJD"),
        tzdz("TZDZ"),
        tb("TB"),
        apiQz("API_QZ"),
        apiHz("API_HZ");

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
