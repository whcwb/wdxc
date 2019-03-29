package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_SGWJ")
public class ClSgwj implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 事故id
     */
    @Column(name = "SG_ID")
    private String sgId;

    /**
     * 文件类型
     */
    @Column(name = "WJLX")
    private String wjlx;

    /**
     * 服务器地址
     */
    @Column(name = "FWQDZ")
    private String fwqdz;

    /**
     * 网络地址
     */
    @Column(name = "WLDZ")
    private String wldz;

    /**
     * 文件大小
     */
    @Column(name = "WJDX")
    private Double wjdx;

    /**
     * 创建时间
     */
    @Column(name = "CJSJ")
    private Date cjsj;

    /**
     * 创建人
     */
    @Column(name = "CJR")
    private String cjr;

    private static final long serialVersionUID = 1L;

    /**
     * 获取ID
     *
     * @return ID - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取事故id
     *
     * @return SG_ID - 事故id
     */
    public String getSgId() {
        return sgId;
    }

    /**
     * 设置事故id
     *
     * @param sgId 事故id
     */
    public void setSgId(String sgId) {
        this.sgId = sgId;
    }

    /**
     * 获取文件类型
     *
     * @return WJLX - 文件类型
     */
    public String getWjlx() {
        return wjlx;
    }

    /**
     * 设置文件类型
     *
     * @param wjlx 文件类型
     */
    public void setWjlx(String wjlx) {
        this.wjlx = wjlx;
    }

    /**
     * 获取服务器地址
     *
     * @return FWQDZ - 服务器地址
     */
    public String getFwqdz() {
        return fwqdz;
    }

    /**
     * 设置服务器地址
     *
     * @param fwqdz 服务器地址
     */
    public void setFwqdz(String fwqdz) {
        this.fwqdz = fwqdz;
    }

    /**
     * 获取网络地址
     *
     * @return WLDZ - 网络地址
     */
    public String getWldz() {
        return wldz;
    }

    /**
     * 设置网络地址
     *
     * @param wldz 网络地址
     */
    public void setWldz(String wldz) {
        this.wldz = wldz;
    }

    /**
     * 获取文件大小
     *
     * @return WJDX - 文件大小
     */
    public Double getWjdx() {
        return wjdx;
    }

    /**
     * 设置文件大小
     *
     * @param wjdx 文件大小
     */
    public void setWjdx(Double wjdx) {
        this.wjdx = wjdx;
    }

    /**
     * 获取创建时间
     *
     * @return CJSJ - 创建时间
     */
    public Date getCjsj() {
        return cjsj;
    }

    /**
     * 设置创建时间
     *
     * @param cjsj 创建时间
     */
    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    /**
     * 获取创建人
     *
     * @return CJR - 创建人
     */
    public String getCjr() {
        return cjr;
    }

    /**
     * 设置创建人
     *
     * @param cjr 创建人
     */
    public void setCjr(String cjr) {
        this.cjr = cjr;
    }

    public enum InnerColumn {
        id("ID"),
        sgId("SG_ID"),
        wjlx("WJLX"),
        fwqdz("FWQDZ"),
        wldz("WLDZ"),
        wjdx("WJDX"),
        cjsj("CJSJ"),
        cjr("CJR");

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