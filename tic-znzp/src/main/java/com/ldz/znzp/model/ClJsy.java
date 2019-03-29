package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_JSY")
public class ClJsy implements Serializable {
    /**
     * 身份证号码
     */
    @Id
    @Column(name = "SFZHM")
    private String sfzhm;

    /**
     * 姓名
     */
    @Column(name = "XM")
    private String xm;

    /**
     * 机构代码
     */
    @Column(name = "JGDM")
    private String jgdm;

    /**
     * 性别
     */
    @Column(name = "XB")
    private String xb;

    /**
     * 年龄
     */
    @Column(name = "NL")
    private Short nl;

    /**
     * 驾龄
     */
    @Column(name = "JL")
    private Short jl;

    /**
     * 状态
     */
    @Column(name = "ZT")
    private String zt;

    /**
     * 头像
     */
    @Column(name = "TX")
    private String tx;

    /**
     * 准驾车型
     */
    @Column(name = "ZJCX")
    private String zjcx;

    /**
     * 等级
     */
    @Column(name = "DJ")
    private String dj;

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
     * 车队编号
     */
    @Column(name = "CDBH")
    private String cdbh;

    /**
     * 档案编号
     */
    @Column(name = "DABH")
    private String dabh;

    /**
     * 初领日期
     */
    @Column(name = "CLRQ")
    private Date clrq;

    private static final long serialVersionUID = 1L;

    /**
     * 获取身份证号码
     *
     * @return SFZHM - 身份证号码
     */
    public String getSfzhm() {
        return sfzhm;
    }

    /**
     * 设置身份证号码
     *
     * @param sfzhm 身份证号码
     */
    public void setSfzhm(String sfzhm) {
        this.sfzhm = sfzhm;
    }

    /**
     * 获取姓名
     *
     * @return XM - 姓名
     */
    public String getXm() {
        return xm;
    }

    /**
     * 设置姓名
     *
     * @param xm 姓名
     */
    public void setXm(String xm) {
        this.xm = xm;
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
     * 获取性别
     *
     * @return XB - 性别
     */
    public String getXb() {
        return xb;
    }

    /**
     * 设置性别
     *
     * @param xb 性别
     */
    public void setXb(String xb) {
        this.xb = xb;
    }

    /**
     * 获取年龄
     *
     * @return NL - 年龄
     */
    public Short getNl() {
        return nl;
    }

    /**
     * 设置年龄
     *
     * @param nl 年龄
     */
    public void setNl(Short nl) {
        this.nl = nl;
    }

    /**
     * 获取驾龄
     *
     * @return JL - 驾龄
     */
    public Short getJl() {
        return jl;
    }

    /**
     * 设置驾龄
     *
     * @param jl 驾龄
     */
    public void setJl(Short jl) {
        this.jl = jl;
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
     * 获取头像
     *
     * @return TX - 头像
     */
    public String getTx() {
        return tx;
    }

    /**
     * 设置头像
     *
     * @param tx 头像
     */
    public void setTx(String tx) {
        this.tx = tx;
    }

    /**
     * 获取准驾车型
     *
     * @return ZJCX - 准驾车型
     */
    public String getZjcx() {
        return zjcx;
    }

    /**
     * 设置准驾车型
     *
     * @param zjcx 准驾车型
     */
    public void setZjcx(String zjcx) {
        this.zjcx = zjcx;
    }

    /**
     * 获取等级
     *
     * @return DJ - 等级
     */
    public String getDj() {
        return dj;
    }

    /**
     * 设置等级
     *
     * @param dj 等级
     */
    public void setDj(String dj) {
        this.dj = dj;
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
     * 获取档案编号
     *
     * @return DABH - 档案编号
     */
    public String getDabh() {
        return dabh;
    }

    /**
     * 设置档案编号
     *
     * @param dabh 档案编号
     */
    public void setDabh(String dabh) {
        this.dabh = dabh;
    }

    /**
     * 获取初领日期
     *
     * @return CLRQ - 初领日期
     */
    public Date getClrq() {
        return clrq;
    }

    /**
     * 设置初领日期
     *
     * @param clrq 初领日期
     */
    public void setClrq(Date clrq) {
        this.clrq = clrq;
    }

    public enum InnerColumn {
        sfzhm("SFZHM"),
        xm("XM"),
        jgdm("JGDM"),
        xb("XB"),
        nl("NL"),
        jl("JL"),
        zt("ZT"),
        tx("TX"),
        zjcx("ZJCX"),
        dj("DJ"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        cdbh("CDBH"),
        dabh("DABH"),
        clrq("CLRQ");

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