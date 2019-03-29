package com.ldz.znzp.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_GPS_LS")
public class ClGpsLs implements Serializable {
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
     * 谷歌经度
     */
    @Column(name = "GGJD")
    private BigDecimal ggjd;

    /**
     * 谷歌纬度
     */
    @Column(name = "GGWD")
    private BigDecimal ggwd;

    /**
     * 百度经度
     */
    @Column(name = "BDJD")
    private BigDecimal bdjd;

    /**
     * 百度纬度
     */
    @Column(name = "BDWD")
    private BigDecimal bdwd;

    /**
     * 高德经度
     */
    @Column(name = "GDJD")
    private BigDecimal gdjd;

    /**
     * 高德纬度
     */
    @Column(name = "GDWD")
    private BigDecimal gdwd;

    /**
     * 类型
     */
    @Column(name = "LX")
    private String lx;

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
     * 获取谷歌经度
     *
     * @return GGJD - 谷歌经度
     */
    public BigDecimal getGgjd() {
        return ggjd;
    }

    /**
     * 设置谷歌经度
     *
     * @param ggjd 谷歌经度
     */
    public void setGgjd(BigDecimal ggjd) {
        this.ggjd = ggjd;
    }

    /**
     * 获取谷歌纬度
     *
     * @return GGWD - 谷歌纬度
     */
    public BigDecimal getGgwd() {
        return ggwd;
    }

    /**
     * 设置谷歌纬度
     *
     * @param ggwd 谷歌纬度
     */
    public void setGgwd(BigDecimal ggwd) {
        this.ggwd = ggwd;
    }

    /**
     * 获取百度经度
     *
     * @return BDJD - 百度经度
     */
    public BigDecimal getBdjd() {
        return bdjd;
    }

    /**
     * 设置百度经度
     *
     * @param bdjd 百度经度
     */
    public void setBdjd(BigDecimal bdjd) {
        this.bdjd = bdjd;
    }

    /**
     * 获取百度纬度
     *
     * @return BDWD - 百度纬度
     */
    public BigDecimal getBdwd() {
        return bdwd;
    }

    /**
     * 设置百度纬度
     *
     * @param bdwd 百度纬度
     */
    public void setBdwd(BigDecimal bdwd) {
        this.bdwd = bdwd;
    }

    /**
     * 获取高德经度
     *
     * @return GDJD - 高德经度
     */
    public BigDecimal getGdjd() {
        return gdjd;
    }

    /**
     * 设置高德经度
     *
     * @param gdjd 高德经度
     */
    public void setGdjd(BigDecimal gdjd) {
        this.gdjd = gdjd;
    }

    /**
     * 获取高德纬度
     *
     * @return GDWD - 高德纬度
     */
    public BigDecimal getGdwd() {
        return gdwd;
    }

    /**
     * 设置高德纬度
     *
     * @param gdwd 高德纬度
     */
    public void setGdwd(BigDecimal gdwd) {
        this.gdwd = gdwd;
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
        zdbh("ZDBH"),
        cjsj("CJSJ"),
        jd("JD"),
        wd("WD"),
        ggjd("GGJD"),
        ggwd("GGWD"),
        bdjd("BDJD"),
        bdwd("BDWD"),
        gdjd("GDJD"),
        gdwd("GDWD"),
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