package com.ldz.znzp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_DZWL")
public class ClDzwl implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private String id;

    /**
     * 围栏名称
     */
    @Column(name = "WLMC")
    private String wlmc;

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
     * 面积
     */
    @Column(name = "MJ")
    private Double mj;

    /**
     * 地理信息坐标
     */
    @Column(name = "DLXXZB")
    private String dlxxzb;

    /**
     * 开始经度
     */
    @Column(name = "KSJD")
    private BigDecimal ksjd;

    /**
     * 开始纬度
     */
    @Column(name = "KSWD")
    private BigDecimal kswd;

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
     * 获取围栏名称
     *
     * @return WLMC - 围栏名称
     */
    public String getWlmc() {
        return wlmc;
    }

    /**
     * 设置围栏名称
     *
     * @param wlmc 围栏名称
     */
    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
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
     * 获取面积
     *
     * @return MJ - 面积
     */
    public Double getMj() {
        return mj;
    }

    /**
     * 设置面积
     *
     * @param mj 面积
     */
    public void setMj(Double mj) {
        this.mj = mj;
    }

    /**
     * 获取地理信息坐标
     *
     * @return DLXXZB - 地理信息坐标
     */
    public String getDlxxzb() {
        return dlxxzb;
    }

    /**
     * 设置地理信息坐标
     *
     * @param dlxxzb 地理信息坐标
     */
    public void setDlxxzb(String dlxxzb) {
        this.dlxxzb = dlxxzb;
    }

    /**
     * 获取开始经度
     *
     * @return KSJD - 开始经度
     */
    public BigDecimal getKsjd() {
        return ksjd;
    }

    /**
     * 设置开始经度
     *
     * @param ksjd 开始经度
     */
    public void setKsjd(BigDecimal ksjd) {
        this.ksjd = ksjd;
    }

    /**
     * 获取开始纬度
     *
     * @return KSWD - 开始纬度
     */
    public BigDecimal getKswd() {
        return kswd;
    }

    /**
     * 设置开始纬度
     *
     * @param kswd 开始纬度
     */
    public void setKswd(BigDecimal kswd) {
        this.kswd = kswd;
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
        id("ID"),
        wlmc("WLMC"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        mj("MJ"),
        dlxxzb("DLXXZB"),
        ksjd("KSJD"),
        kswd("KSWD"),
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