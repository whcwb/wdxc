package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * 车队表
 * @author Lee
 *
 */
@Table(name = "CL_CD")
public class ClCd implements Serializable {
    /**
     * 车队编号
     */
    @Id
    @Column(name = "CDBH")
    private String cdbh;

    /**
     * 车队名称
     */
    @Column(name = "CDMC")
    private String cdmc;

    /**
     * 队长编号
     */
    @Column(name = "DZBH")
    private String dzbh;

    /**
     * 队长姓名
     */
    @Column(name = "DZXM")
    private String dzxm;

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

    /**
     * 状态
     */
    @Column(name = "ZT")
    private String zt;

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

    private static final long serialVersionUID = 1L;

    /**
     * 获取车队编号
     *
     * @return CDBH - 车队编号
     */
    public String getCdbh() {
        return cdbh;
    }

    /**
     * 设置车队编号
     *
     * @param cdbh 车队编号
     */
    public void setCdbh(String cdbh) {
        this.cdbh = cdbh;
    }

    /**
     * 获取车队名称
     *
     * @return CDMC - 车队名称
     */
    public String getCdmc() {
        return cdmc;
    }

    /**
     * 设置车队名称
     *
     * @param cdmc 车队名称
     */
    public void setCdmc(String cdmc) {
        this.cdmc = cdmc;
    }

    /**
     * 获取队长编号
     *
     * @return DZBH - 队长编号
     */
    public String getDzbh() {
        return dzbh;
    }

    /**
     * 设置队长编号
     *
     * @param dzbh 队长编号
     */
    public void setDzbh(String dzbh) {
        this.dzbh = dzbh;
    }

    /**
     * 获取队长姓名
     *
     * @return DZXM - 队长姓名
     */
    public String getDzxm() {
        return dzxm;
    }

    /**
     * 设置队长姓名
     *
     * @param dzxm 队长姓名
     */
    public void setDzxm(String dzxm) {
        this.dzxm = dzxm;
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

    /**
     * 获取状态
     *
     * @return ZT - 状态
     */
    public String getZt() {
        return zt;
    }

    /**
     * 设置状态
     *
     * @param zt 状态
     */
    public void setZt(String zt) {
        this.zt = zt;
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

    public enum InnerColumn {
        cdbh("CDBH"),
        cdmc("CDMC"),
        dzbh("DZBH"),
        dzxm("DZXM"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        zt("ZT"),
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