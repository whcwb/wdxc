package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_YXHDWJ")
public class SysYxhdwj implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "HD_ID")
    private String hdId;

    @Column(name = "WJLX")
    private String wjlx;

    @Column(name = "WJLJ")
    private String wjlj;

    @Column(name = "WLLJ")
    private String wllj;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "XGR")
    private String xgr;

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
     * @return WJLX
     */
    public String getWjlx() {
        return wjlx;
    }

    /**
     * @param wjlx
     */
    public void setWjlx(String wjlx) {
        this.wjlx = wjlx;
    }

    /**
     * @return WJLJ
     */
    public String getWjlj() {
        return wjlj;
    }

    /**
     * @param wjlj
     */
    public void setWjlj(String wjlj) {
        this.wjlj = wjlj;
    }

    /**
     * @return WLLJ
     */
    public String getWllj() {
        return wllj;
    }

    /**
     * @param wllj
     */
    public void setWllj(String wllj) {
        this.wllj = wllj;
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

    public enum InnerColumn {
        id("ID"),
        hdId("HD_ID"),
        wjlx("WJLX"),
        wjlj("WJLJ"),
        wllj("WLLJ"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR");

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