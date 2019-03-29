package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_XLCL")
public class ClXlcl implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    private String 线路id;

    private String 车牌号;

    private String 状态;

    private Date 创建时间;

    private String 创建人;

    private Date 修改时间;

    private String 修改人;

    private String 车辆id;

    private static final long serialVersionUID = 1L;

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return 线路id
     */
    public String get线路id() {
        return 线路id;
    }

    /**
     * @param 线路id
     */
    public void set线路id(String 线路id) {
        this.线路id = 线路id;
    }

    /**
     * @return 车牌号
     */
    public String get车牌号() {
        return 车牌号;
    }

    /**
     * @param 车牌号
     */
    public void set车牌号(String 车牌号) {
        this.车牌号 = 车牌号;
    }

    /**
     * @return 状态
     */
    public String get状态() {
        return 状态;
    }

    /**
     * @param 状态
     */
    public void set状态(String 状态) {
        this.状态 = 状态;
    }

    /**
     * @return 创建时间
     */
    public Date get创建时间() {
        return 创建时间;
    }

    /**
     * @param 创建时间
     */
    public void set创建时间(Date 创建时间) {
        this.创建时间 = 创建时间;
    }

    /**
     * @return 创建人
     */
    public String get创建人() {
        return 创建人;
    }

    /**
     * @param 创建人
     */
    public void set创建人(String 创建人) {
        this.创建人 = 创建人;
    }

    /**
     * @return 修改时间
     */
    public Date get修改时间() {
        return 修改时间;
    }

    /**
     * @param 修改时间
     */
    public void set修改时间(Date 修改时间) {
        this.修改时间 = 修改时间;
    }

    /**
     * @return 修改人
     */
    public String get修改人() {
        return 修改人;
    }

    /**
     * @param 修改人
     */
    public void set修改人(String 修改人) {
        this.修改人 = 修改人;
    }

    /**
     * @return 车辆id
     */
    public String get车辆id() {
        return 车辆id;
    }

    /**
     * @param 车辆id
     */
    public void set车辆id(String 车辆id) {
        this.车辆id = 车辆id;
    }

    public enum InnerColumn {
        id("ID"),
        线路id("线路id"),
        车牌号("车牌号"),
        状态("状态"),
        创建时间("创建时间"),
        创建人("创建人"),
        修改时间("修改时间"),
        修改人("修改人"),
        车辆id("车辆id");

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