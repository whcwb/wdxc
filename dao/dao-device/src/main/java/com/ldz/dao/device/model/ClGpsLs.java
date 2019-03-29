package com.ldz.dao.device.model;

import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "CL_GPS_LS")
@AllArgsConstructor
@ToString
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

    /**
     * 定位精度
     */
    @Column(name = "DWJD")
    private Short dwjd;

    /**
     * 方向角
     */
    @Column(name = "FXJ")
    private BigDecimal fxj;

    /**
     * 运行速度
     */
    @Column(name = "YXSD")
    private String yxsd;

    private static final long serialVersionUID = 1L;

    public ClGpsLs(){};

    public ClGpsLs(ClGps gps){
        this.cjsj = gps.getCjsj();
        this.jd = gps.getJd();
        this.wd = gps.getWd();
        this.ggjd = gps.getGgjd();
        this.ggwd = gps.getGgwd();
        this.bdjd = gps.getBdjd();
        this.bdwd = gps.getBdwd();
        this.gdjd = gps.getGdjd();
        this.gdwd = gps.getGdwd();
        this.lx = gps.getLx();
        this.dwjd = gps.getDwjd();
        this.fxj = gps.getFxj();
        this.yxsd = gps.getYxsd();
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

    /**
     * 获取定位精度
     *
     * @return DWJD - 定位精度
     */
    public Short getDwjd() {
        return dwjd;
    }

    /**
     * 设置定位精度
     *
     * @param dwjd 定位精度
     */
    public void setDwjd(Short dwjd) {
        this.dwjd = dwjd;
    }

    /**
     * 获取方向角
     *
     * @return FXJ - 方向角
     */
    public BigDecimal getFxj() {
        return fxj;
    }

    /**
     * 设置方向角
     *
     * @param fxj 方向角
     */
    public void setFxj(BigDecimal fxj) {
        this.fxj = fxj;
    }

    /**
     * 获取运行速度
     *
     * @return YXSD - 运行速度
     */
    public String getYxsd() {
        return yxsd;
    }

    /**
     * 设置运行速度
     *
     * @param yxsd 运行速度
     */
    public void setYxsd(String yxsd) {
        this.yxsd = yxsd;
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
        lx("LX"),
        dwjd("DWJD"),
        fxj("FXJ"),
        yxsd("YXSD");

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