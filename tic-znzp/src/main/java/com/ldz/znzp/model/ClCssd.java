package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_CSSD")
public class ClCssd implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 车型
     */
    @Column(name = "CX")
    private String cx;

    /**
     * 速度上限
     */
    @Column(name = "SDSX")
    private Short sdsx;

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

    /**
     * 修改时间
     */
    @Column(name = "XGSJ")
    private Date xgsj;

    /**
     * 修改人
     */
    @Column(name = "XGR")
    private String xgr;

    /**
     * 机构代码
     */
    @Column(name = "JGDM")
    private String jgdm;

    /**
     * 机构名称
     */
    @Column(name = "JGMC")
    private String jgmc;

    private static final long serialVersionUID = 1L;

    /**
     * 获取id
     *
     * @return ID - id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取车型
     *
     * @return CX - 车型
     */
    public String getCx() {
        return cx;
    }

    /**
     * 设置车型
     *
     * @param cx 车型
     */
    public void setCx(String cx) {
        this.cx = cx;
    }

    /**
     * 获取速度上限
     *
     * @return SDSX - 速度上限
     */
    public Short getSdsx() {
        return sdsx;
    }

    /**
     * 设置速度上限
     *
     * @param sdsx 速度上限
     */
    public void setSdsx(Short sdsx) {
        this.sdsx = sdsx;
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

    /**
     * 获取修改时间
     *
     * @return XGSJ - 修改时间
     */
    public Date getXgsj() {
        return xgsj;
    }

    /**
     * 设置修改时间
     *
     * @param xgsj 修改时间
     */
    public void setXgsj(Date xgsj) {
        this.xgsj = xgsj;
    }

    /**
     * 获取修改人
     *
     * @return XGR - 修改人
     */
    public String getXgr() {
        return xgr;
    }

    /**
     * 设置修改人
     *
     * @param xgr 修改人
     */
    public void setXgr(String xgr) {
        this.xgr = xgr;
    }

    /**
     * 获取机构代码
     *
     * @return JGDM - 机构代码
     */
    public String getJgdm() {
        return jgdm;
    }

    /**
     * 设置机构代码
     *
     * @param jgdm 机构代码
     */
    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }

    /**
     * 获取机构名称
     *
     * @return JGMC - 机构名称
     */
    public String getJgmc() {
        return jgmc;
    }

    /**
     * 设置机构名称
     *
     * @param jgmc 机构名称
     */
    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public enum InnerColumn {
        id("ID"),
        cx("CX"),
        sdsx("SDSX"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        jgdm("JGDM"),
        jgmc("JGMC");

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