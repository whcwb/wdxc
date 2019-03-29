package com.ldz.sys.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "SYS_CLK_PTJS")
public class SysJs implements Serializable {
    @Id
    @Column(name = "JS_ID")
    private String jsId;

    @Column(name = "JSMC")
    private String jsmc;

    @Column(name = "JSLX")
    private String jslx;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "ZT")
    private String zt;

    @Column(name = "JGDM")
    private String jgdm;

    @Column(name = "SM")
    private String sm;

    @Transient
    private boolean _checked;


    public boolean is_checked() {
        return _checked;
    }

    public void set_checked(boolean _checked) {
        this._checked = _checked;
    }

    private static final long serialVersionUID = 1L;

    /**
     * @return JS_ID
     */
    public String getJsId() {
        return jsId;
    }

    /**
     * @param jsId
     */
    public void setJsId(String jsId) {
        this.jsId = jsId;
    }

    /**
     * @return JSMC
     */
    public String getJsmc() {
        return jsmc;
    }

    /**
     * @param jsmc
     */
    public void setJsmc(String jsmc) {
        this.jsmc = jsmc;
    }

    /**
     * @return JSLX
     */
    public String getJslx() {
        return jslx;
    }

    /**
     * @param jslx
     */
    public void setJslx(String jslx) {
        this.jslx = jslx;
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
     * @return SM
     */
    public String getSm() {
        return sm;
    }

    /**
     * @param sm
     */
    public void setSm(String sm) {
        this.sm = sm;
    }


    public enum InnerColumn {
        jsId("JS_ID"),
        jsmc("JSMC"),
        jslx("JSLX"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgr("XGR"),
        xgsj("XGSJ"),
        zt("ZT"),
        jgdm("JGDM"),
        sm("SM");

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