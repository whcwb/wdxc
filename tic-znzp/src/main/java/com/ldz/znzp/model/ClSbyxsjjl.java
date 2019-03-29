package com.ldz.znzp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "CL_SBYXSJJL")
public class ClSbyxsjjl implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 终端编号
     */
    @Column(name = "ZDBH")
    private String zdbh;

    /**
     * 事件级别
     */
    @Column(name = "SJJB")
    private String sjjb;

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
     * 精度
     */
    @Column(name = "JID")
    private BigDecimal jid;

    /**
     * 事件类型
     */
    @Column(name = "SJLX")
    private String sjlx;

    /**
     * 运行方向
     */
    @Column(name = "YXFX")
    private Double yxfx;

    /**
     * 备注
     */
    @Column(name = "BZ")
    private String bz;

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
     * 获取事件级别
     *
     * @return SJJB - 事件级别
     */
    public String getSjjb() {
        return sjjb;
    }

    /**
     * 设置事件级别
     *
     * @param sjjb 事件级别
     */
    public void setSjjb(String sjjb) {
        this.sjjb = sjjb;
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
     * 获取精度
     *
     * @return JID - 精度
     */
    public BigDecimal getJid() {
        return jid;
    }

    /**
     * 设置精度
     *
     * @param jid 精度
     */
    public void setJid(BigDecimal jid) {
        this.jid = jid;
    }

    /**
     * 获取事件类型
     *
     * @return SJLX - 事件类型
     */
    public String getSjlx() {
        return sjlx;
    }

    /**
     * 设置事件类型
     *
     * @param sjlx 事件类型
     */
    public void setSjlx(String sjlx) {
        this.sjlx = sjlx;
    }

    /**
     * 获取运行方向
     *
     * @return YXFX - 运行方向
     */
    public Double getYxfx() {
        return yxfx;
    }

    /**
     * 设置运行方向
     *
     * @param yxfx 运行方向
     */
    public void setYxfx(Double yxfx) {
        this.yxfx = yxfx;
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

    public enum InnerColumn {
        id("ID"),
        zdbh("ZDBH"),
        sjjb("SJJB"),
        cjsj("CJSJ"),
        jd("JD"),
        wd("WD"),
        jid("JID"),
        sjlx("SJLX"),
        yxfx("YXFX"),
        bz("BZ");

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