package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "CL_SPK")
public class ClSpk implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 文件名
     */
    @Column(name = "WJM")
    private String wjm;

    /**
     * 地址
     */
    @Column(name = "DZ")
    private String dz;

    /**
     * 终端编号
     */
    @Column(name = "ZDBH")
    private String zdbh;

    /**
     * 开始时间
     */
    @Column(name = "KSSJ")
    private Date kssj;

    /**
     * 结束时间
     */
    @Column(name = "JSSJ")
    private Date jssj;

    /**
     * 时长
     */
    @Column(name = "SC")
    private Short sc;

    /**
     * url
     */
    @Column(name = "URL")
    private String url;

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
     * 标记
     */
    @Column(name = "BJ")
    private String bj;

    /**
     * 视频来源
     */
    @Column(name = "SPLY")
    private String sply;

    /**
     * 视频类型
     */
    @Column(name = "SPLX")
    private String splx;

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
     * 车牌号
     */
    @Column(name = "CPH")
    private String cph;

    /**
     * 车辆id
     */
    @Column(name = "CL_ID")
    private String clId;

    private BigDecimal jd;

    private BigDecimal wd;

    private String wz;

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    public BigDecimal getJd() {
        return jd;
    }

    public void setJd(BigDecimal jd) {
        this.jd = jd;
    }

    public BigDecimal getWd() {
        return wd;
    }

    public void setWd(BigDecimal wd) {
        this.wd = wd;
    }

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
     * 获取文件名
     *
     * @return WJM - 文件名
     */
    public String getWjm() {
        return wjm;
    }

    /**
     * 设置文件名
     *
     * @param wjm 文件名
     */
    public void setWjm(String wjm) {
        this.wjm = wjm;
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
     * 获取终端编号
     *
     * @return ZDBH - 终端编号
     */
    public String getZdbh() {
        return zdbh;
    }

    /**
     * 设置终端编号
     *
     * @param zdbh 终端编号
     */
    public void setZdbh(String zdbh) {
        this.zdbh = zdbh;
    }

    /**
     * 获取开始时间
     *
     * @return KSSJ - 开始时间
     */
    public Date getKssj() {
        return kssj;
    }

    /**
     * 设置开始时间
     *
     * @param kssj 开始时间
     */
    public void setKssj(Date kssj) {
        this.kssj = kssj;
    }

    /**
     * 获取结束时间
     *
     * @return JSSJ - 结束时间
     */
    public Date getJssj() {
        return jssj;
    }

    /**
     * 设置结束时间
     *
     * @param jssj 结束时间
     */
    public void setJssj(Date jssj) {
        this.jssj = jssj;
    }

    /**
     * 获取时长
     *
     * @return SC - 时长
     */
    public Short getSc() {
        return sc;
    }

    /**
     * 设置时长
     *
     * @param sc 时长
     */
    public void setSc(Short sc) {
        this.sc = sc;
    }

    /**
     * 获取url
     *
     * @return URL - url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置url
     *
     * @param url url
     */
    public void setUrl(String url) {
        this.url = url;
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
     * 获取标记
     *
     * @return BJ - 标记
     */
    public String getBj() {
        return bj;
    }

    /**
     * 设置标记
     *
     * @param bj 标记
     */
    public void setBj(String bj) {
        this.bj = bj;
    }

    /**
     * 获取视频来源
     *
     * @return SPLY - 视频来源
     */
    public String getSply() {
        return sply;
    }

    /**
     * 设置视频来源
     *
     * @param sply 视频来源
     */
    public void setSply(String sply) {
        this.sply = sply;
    }

    /**
     * 获取视频类型
     *
     * @return SPLX - 视频类型
     */
    public String getSplx() {
        return splx;
    }

    /**
     * 设置视频类型
     *
     * @param splx 视频类型
     */
    public void setSplx(String splx) {
        this.splx = splx;
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
     * 获取车牌号
     *
     * @return CPH - 车牌号
     */
    public String getCph() {
        return cph;
    }

    /**
     * 设置车牌号
     *
     * @param cph 车牌号
     */
    public void setCph(String cph) {
        this.cph = cph;
    }

    /**
     * 获取车辆id
     *
     * @return CL_ID - 车辆id
     */
    public String getClId() {
        return clId;
    }

    /**
     * 设置车辆id
     *
     * @param clId 车辆id
     */
    public void setClId(String clId) {
        this.clId = clId;
    }

    public enum InnerColumn {
        id("ID"),
        wjm("WJM"),
        dz("DZ"),
        zdbh("ZDBH"),
        kssj("KSSJ"),
        jssj("JSSJ"),
        sc("SC"),
        url("URL"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        bj("BJ"),
        sply("SPLY"),
        splx("SPLX"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        cph("CPH"),
        clId("CL_ID");

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