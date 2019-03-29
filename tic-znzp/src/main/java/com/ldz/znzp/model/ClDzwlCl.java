package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_DZWL_CL")
public class ClDzwlCl implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 围栏id
     */
    @Column(name = "WL_ID")
    private String wlId;

    /**
     * 车辆id
     */
    @Column(name = "CL_ID")
    private String clId;

    /**
     * 车牌号
     */
    @Column(name = "CPH")
    private String cph;

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
     * 获取围栏id
     *
     * @return WL_ID - 围栏id
     */
    public String getWlId() {
        return wlId;
    }

    /**
     * 设置围栏id
     *
     * @param wlId 围栏id
     */
    public void setWlId(String wlId) {
        this.wlId = wlId;
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

    public enum InnerColumn {
        id("ID"),
        wlId("WL_ID"),
        clId("CL_ID"),
        cph("CPH"),
        cjsj("CJSJ"),
        cjr("CJR");

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