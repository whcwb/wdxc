package com.ldz.wechat.module.model;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@ToString
@Table(name = "CL_XL")
public class ClXl implements Serializable {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 线路名称
     */
    @Column(name = "XLMC")
    private String xlmc;

    /**
     * 线路编号
     */
    @Column(name = "XLBH")
    private String xlbh;

    /**
     * 长度
     */
    @Column(name = "CD")
    private Double cd;

    /**
     * 平均时间
     */
    @Column(name = "PJSJ")
    private Double pjsj;

    /**
     * 运行开始时间
     */
    @Column(name = "YXKSSJ")
    private String yxkssj;

    /**
     * 运行结束时间
     */
    @Column(name = "YXJSSJ")
    private String yxjssj;

    /**
     * 创建人
     */
    @Column(name = "CJR")
    private String cjr;

    /**
     * 创建时间
     */
    @Column(name = "CJSJ")
    private Date cjsj;

    /**
     * 修改人
     */
    @Column(name = "XGR")
    private String xgr;

    /**
     * 修改时间
     */
    @Column(name = "XGSJ")
    private Date xgsj;

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
     * 类型
     */
    @Column(name = "LX")
    private String lx;

    @Transient
    private String zdIds;


    public String getZdIds() {
        return zdIds;
    }

    public void setZdIds(String zdIds) {
        this.zdIds = zdIds;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 获取id
     *
     * @return id - id
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
     * 获取线路名称
     *
     * @return XLMC - 线路名称
     */
    public String getXlmc() {
        return xlmc;
    }

    /**
     * 设置线路名称
     *
     * @param xlmc 线路名称
     */
    public void setXlmc(String xlmc) {
        this.xlmc = xlmc;
    }

    /**
     * 获取线路编号
     *
     * @return XLBH - 线路编号
     */
    public String getXlbh() {
        return xlbh;
    }

    /**
     * 设置线路编号
     *
     * @param xlbh 线路编号
     */
    public void setXlbh(String xlbh) {
        this.xlbh = xlbh;
    }

    /**
     * 获取长度
     *
     * @return CD - 长度
     */
    public Double getCd() {
        return cd;
    }

    /**
     * 设置长度
     *
     * @param cd 长度
     */
    public void setCd(Double cd) {
        this.cd = cd;
    }

    /**
     * 获取平均时间
     *
     * @return PJSJ - 平均时间
     */
    public Double getPjsj() {
        return pjsj;
    }

    /**
     * 设置平均时间
     *
     * @param pjsj 平均时间
     */
    public void setPjsj(Double pjsj) {
        this.pjsj = pjsj;
    }

    /**
     * 获取运行开始时间
     *
     * @return YXKSSJ - 运行开始时间
     */
    public String getYxkssj() {
        return yxkssj;
    }

    /**
     * 设置运行开始时间
     *
     * @param yxkssj 运行开始时间
     */
    public void setYxkssj(String yxkssj) {
        this.yxkssj = yxkssj;
    }

    /**
     * 获取运行结束时间
     *
     * @return YXJSSJ - 运行结束时间
     */
    public String getYxjssj() {
        return yxjssj;
    }

    /**
     * 设置运行结束时间
     *
     * @param yxjssj 运行结束时间
     */
    public void setYxjssj(String yxjssj) {
        this.yxjssj = yxjssj;
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
        id("id"),
        xlmc("XLMC"),
        xlbh("XLBH"),
        cd("CD"),
        pjsj("PJSJ"),
        yxkssj("YXKSSJ"),
        yxjssj("YXJSSJ"),
        cjr("CJR"),
        cjsj("CJSJ"),
        xgr("XGR"),
        xgsj("XGSJ"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        zt("ZT"),
        bz("BZ"),
        yxfs("YXFS"),
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