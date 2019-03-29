package com.ldz.znzp.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_ZD")
public class ClZd implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 地址
     */
    @Column(name = "DZ")
    private String dz;

    /**
     * 名称
     */
    @Column(name = "MC")
    private String mc;

    /**
     * 经度
     */
    @Column(name = "JD")
    private Double jd;

    /**
     * 纬度
     */
    @Column(name = "WD")
    private Double wd;

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

    /**
     * 状态
     */
    @Column(name = "ZT")
    private String zt;

    /**
     * 备注
     */
    @Column(name = "BZ")
    private String bz;

    /**
     * 运行方式
     */
    @Column(name = "YXFS")
    private String yxfs;

    /**
     * 范围
     */
    @Column(name = "FW")
    private Short fw;

    /**
     * 类型
     */
    @Column(name = "LX")
    private String lx;

    @Transient
    private String xlId;

    /**
     * 站点序号
     */
    @Transient
    private Short routeOrder;

    public Short getRouteOrder() {
        return routeOrder;
    }

    public void setRouteOrder(Short routeOrder) {
        this.routeOrder = routeOrder;
    }

    public String getXlId() {
        return xlId;
    }

    public void setXlId(String xlId) {
        this.xlId = xlId;
    }

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
     * 获取地址
     *
     * @return DZ - 地址
     */
    public String getDz() {
        return dz;
    }

    /**
     * 设置地址
     *
     * @param dz 地址
     */
    public void setDz(String dz) {
        this.dz = dz;
    }

    /**
     * 获取名称
     *
     * @return MC - 名称
     */
    public String getMc() {
        return mc;
    }

    /**
     * 设置名称
     *
     * @param mc 名称
     */
    public void setMc(String mc) {
        this.mc = mc;
    }

    /**
     * 获取经度
     *
     * @return JD - 经度
     */
    public Double getJd() {
        return jd;
    }

    /**
     * 设置经度
     *
     * @param jd 经度
     */
    public void setJd(Double jd) {
        this.jd = jd;
    }

    /**
     * 获取纬度
     *
     * @return WD - 纬度
     */
    public Double getWd() {
        return wd;
    }

    /**
     * 设置纬度
     *
     * @param wd 纬度
     */
    public void setWd(Double wd) {
        this.wd = wd;
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
     * 获取备注
     *
     * @return BZ - 备注
     */
    public String getBz() {
        return bz;
    }

    /**
     * 设置备注
     *
     * @param bz 备注
     */
    public void setBz(String bz) {
        this.bz = bz;
    }

    /**
     * 获取运行方式
     *
     * @return YXFS - 运行方式
     */
    public String getYxfs() {
        return yxfs;
    }

    /**
     * 设置运行方式
     *
     * @param yxfs 运行方式
     */
    public void setYxfs(String yxfs) {
        this.yxfs = yxfs;
    }

    /**
     * 获取范围
     *
     * @return FW - 范围
     */
    public Short getFw() {
        return fw;
    }

    /**
     * 设置范围
     *
     * @param fw 范围
     */
    public void setFw(Short fw) {
        this.fw = fw;
    }

    /**
     * 获取类型
     *
     * @return LX - 类型
     */
    public String getLx() {
        return lx;
    }

    /**
     * 设置类型
     *
     * @param lx 类型
     */
    public void setLx(String lx) {
        this.lx = lx;
    }

    public enum InnerColumn {
        id("ID"),
        dz("DZ"),
        mc("MC"),
        jd("JD"),
        wd("WD"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        zt("ZT"),
        bz("BZ"),
        yxfs("YXFS"),
        fw("FW"),
        lx("LX");

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