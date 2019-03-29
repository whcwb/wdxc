package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_LSC")
public class ClLsc implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 车牌号
     */
    @Column(name = "CPH")
    private String cph;

    /**
     * 创建人
     */
    @Column(name = "CJR")
    private String cjr;

    /**
     * 登记次数
     */
    @Column(name = "DJCS")
    private Short djcs;

    /**
     * 临时单位id
     */
    @Column(name = "LSDW_ID")
    private String lsdwId;

    /**
     * 临时单位名称
     */
    @Column(name = "LSDWMC")
    private String lsdwmc;

    /**
     * 车辆类型
     */
    @Column(name = "CLLX")
    private String cllx;

    /**
     * 座位数
     */
    @Column(name = "ZWS")
    private Short zws;

    /**
     * 状态
     */
    @Column(name = "ZT")
    private String zt;

    /**
     * 车辆id
     */
    @Column(name = "CL_ID")
    private String clId;
    /**
     * 创建时间
     */
    @Column(name = "CJSJ")
    private Date cjsj;

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
     * 获取登记次数
     *
     * @return DJCS - 登记次数
     */
    public Short getDjcs() {
        return djcs;
    }

    /**
     * 设置登记次数
     *
     * @param djcs 登记次数
     */
    public void setDjcs(Short djcs) {
        this.djcs = djcs;
    }

    /**
     * 获取临时单位id
     *
     * @return LSDW_ID - 临时单位id
     */
    public String getLsdwId() {
        return lsdwId;
    }

    /**
     * 设置临时单位id
     *
     * @param lsdwId 临时单位id
     */
    public void setLsdwId(String lsdwId) {
        this.lsdwId = lsdwId;
    }

    /**
     * 获取临时单位名称
     *
     * @return LSDWMC - 临时单位名称
     */
    public String getLsdwmc() {
        return lsdwmc;
    }

    public Date getCjsj() {
        return cjsj;
    }

    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    /**
     * 设置临时单位名称
     *
     * @param lsdwmc 临时单位名称
     */
    public void setLsdwmc(String lsdwmc) {
        this.lsdwmc = lsdwmc;
    }

    /**
     * 获取车辆类型
     *
     * @return CLLX - 车辆类型
     */
    public String getCllx() {
        return cllx;
    }

    /**
     * 设置车辆类型
     *
     * @param cllx 车辆类型
     */
    public void setCllx(String cllx) {
        this.cllx = cllx;
    }

    /**
     * 获取座位数
     *
     * @return ZWS - 座位数
     */
    public Short getZws() {
        return zws;
    }

    /**
     * 设置座位数
     *
     * @param zws 座位数
     */
    public void setZws(Short zws) {
        this.zws = zws;
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
        cph("CPH"),
        cjr("CJR"),
        djcs("DJCS"),
        lsdwId("LSDW_ID"),
        lsdwmc("LSDWMC"),
        cllx("CLLX"),
        zws("ZWS"),
        zt("ZT"),
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