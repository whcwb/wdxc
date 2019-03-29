package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "SYS_HDYX")
public class SysHdyx implements Serializable {
    @Id
    @Column(name = "HD_ID")
    private String hdId;

    @Column(name = "KSSJ")
    private Date kssj;

    @Column(name = "JSSJ")
    private Date jssj;

    @Column(name = "HDLX")
    private String hdlx;

    @Column(name = "JGDM")
    private String jgdm;

    @Column(name = "ZT")
    private String zt;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "HDBT")
    private String hdbt;

    @Column(name = "URL")
    private String url;

    @Column(name = "WJLX")
    private String wjlx;

    @Column(name = "WZ")
    private String wz;

    @Transient
    private String filePaths;

    public String getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(String filePaths) {
        this.filePaths = filePaths;
    }

    private static final long serialVersionUID = 1L;

    public String getWjlx() {
        return wjlx;
    }

    public void setWjlx(String wjlx) {
        this.wjlx = wjlx;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    /**
     * @return HD_ID
     */
    public String getHdId() {
        return hdId;
    }

    /**
     * @param hdId
     */
    public void setHdId(String hdId) {
        this.hdId = hdId;
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

    /**
     * @return HDLX
     */
    public String getHdlx() {
        return hdlx;
    }

    /**
     * @param hdlx
     */
    public void setHdlx(String hdlx) {
        this.hdlx = hdlx;
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

    /**
     * @return HDBT
     */
    public String getHdbt() {
        return hdbt;
    }

    /**
     * @param hdbt
     */
    public void setHdbt(String hdbt) {
        this.hdbt = hdbt;
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

    public enum InnerColumn {
        hdId("HD_ID"),
        kssj("KSSJ"),
        jssj("JSSJ"),
        hdlx("HDLX"),
        jgdm("JGDM"),
        zt("ZT"),
        cjr("CJR"),
        cjsj("CJSJ"),
        xgr("XGR"),
        xgsj("XGSJ"),
        hdbt("HDBT"),
        url("URL");

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
