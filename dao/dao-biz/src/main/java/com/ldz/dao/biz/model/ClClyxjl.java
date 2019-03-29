package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "CL_CLYXJL")
public class ClClyxjl implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 车辆ID
     */
    @Column(name = "CL_ID")
    private String clId;

    /**
     * 车牌号码
     */
    @Column(name = "CPHM")
    private String cphm;

    /**
     * 设备终端编号
     */
    @Column(name = "SZDBH")
    private String szdbh;

    /**
     * 站点编号
     */
    @Column(name = "ZDBH")
    private String zdbh;

    /**
     * 创建时间
     */
    @Column(name = "CJSJ")
    private Date cjsj;

    /**
     * 经度
     */
    @Column(name = "JD")
    private BigDecimal jd;

    /**
     * 纬度
     */
    @Column(name = "WD")
    private BigDecimal wd;

    /**
     * 站点距离
     */
    @Column(name = "ZDJL")
    private Short zdjl;

    /**
     * 站点ID
     */
    @Column(name = "ZD_ID")
    private String zdId;

    /**
     * 站点名称
     */
    @Column(name = "ZDMC")
    private String zdmc;

    /**
     * 线路ID
     */
    @Column(name = "XL_ID")
    private String xlId;

    /**
     * 线路名称
     */
    @Column(name = "XLMC")
    private String xlmc;

    /**
     * 运行方向
     */
    @Column(name = "YXFX")
    private String yxfx;

    /**
     * 精度
     */
    @Column(name = "JID")
    private Short jid;

    /**
     * 状态(inStation进站，runing正常  off离线)
     */
    @Column(name = "ZT")
    private String zt;

    private static final long serialVersionUID = 1L;

    public String getSzdbh() {
		return szdbh;
	}

	public void setSzdbh(String szdbh) {
		this.szdbh = szdbh;
	}

	public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

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
     * 获取车辆ID
     *
     * @return CL_ID - 车辆ID
     */
    public String getClId() {
        return clId;
    }

    /**
     * 设置车辆ID
     *
     * @param clId 车辆ID
     */
    public void setClId(String clId) {
        this.clId = clId;
    }

    /**
     * 获取车牌号码
     *
     * @return CPHM - 车牌号码
     */
    public String getCphm() {
        return cphm;
    }

    /**
     * 设置车牌号码
     *
     * @param cphm 车牌号码
     */
    public void setCphm(String cphm) {
        this.cphm = cphm;
    }

    /**
     * 获取站点编号
     *
     * @return ZDBH - 站点编号
     */
    public String getZdbh() {
        return zdbh;
    }

    /**
     * 设置站点编号
     *
     * @param zdbh 站点编号
     */
    public void setZdbh(String zdbh) {
        this.zdbh = zdbh;
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
     * 获取经度
     *
     * @return JD - 经度
     */
    public BigDecimal getJd() {
        return jd;
    }

    /**
     * 设置经度
     *
     * @param jd 经度
     */
    public void setJd(BigDecimal jd) {
        this.jd = jd;
    }

    /**
     * 获取纬度
     *
     * @return WD - 纬度
     */
    public BigDecimal getWd() {
        return wd;
    }

    /**
     * 设置纬度
     *
     * @param wd 纬度
     */
    public void setWd(BigDecimal wd) {
        this.wd = wd;
    }

    /**
     * 获取站点距离
     *
     * @return ZDJL - 站点距离
     */
    public Short getZdjl() {
        return zdjl;
    }

    /**
     * 设置站点距离
     *
     * @param zdjl 站点距离
     */
    public void setZdjl(Short zdjl) {
        this.zdjl = zdjl;
    }

    /**
     * 获取站点ID
     *
     * @return ZD_ID - 站点ID
     */
    public String getZdId() {
        return zdId;
    }

    /**
     * 设置站点ID
     *
     * @param zdId 站点ID
     */
    public void setZdId(String zdId) {
        this.zdId = zdId;
    }

    /**
     * 获取站点名称
     *
     * @return ZDMC - 站点名称
     */
    public String getZdmc() {
        return zdmc;
    }

    /**
     * 设置站点名称
     *
     * @param zdmc 站点名称
     */
    public void setZdmc(String zdmc) {
        this.zdmc = zdmc;
    }

    /**
     * 获取线路ID
     *
     * @return XL_ID - 线路ID
     */
    public String getXlId() {
        return xlId;
    }

    /**
     * 设置线路ID
     *
     * @param xlId 线路ID
     */
    public void setXlId(String xlId) {
        this.xlId = xlId;
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
     * 获取运行方向
     *
     * @return YXFX - 运行方向
     */
    public String getYxfx() {
        return yxfx;
    }

    /**
     * 设置运行方向
     *
     * @param yxfx 运行方向
     */
    public void setYxfx(String yxfx) {
        this.yxfx = yxfx;
    }

    /**
     * 获取精度
     *
     * @return JID - 精度
     */
    public Short getJid() {
        return jid;
    }

    /**
     * 设置精度
     *
     * @param jid 精度
     */
    public void setJid(Short jid) {
        this.jid = jid;
    }

    public enum InnerColumn {
        id("ID"),
        clId("CL_ID"),
        cphm("CPHM"),
        zdbh("ZDBH"),
        szdbh("SZDBH"),
        cjsj("CJSJ"),
        jd("JD"),
        wd("WD"),
        zdjl("ZDJL"),
        zdId("ZD_ID"),
        zdmc("ZDMC"),
        xlId("XL_ID"),
        xlmc("XLMC"),
        yxfx("YXFX"),
        zt("ZT"),
        jid("JID");

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
