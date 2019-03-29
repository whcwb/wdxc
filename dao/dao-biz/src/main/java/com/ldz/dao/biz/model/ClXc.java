package com.ldz.dao.biz.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Table(name = "CL_XC")
public class ClXc implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 车辆行程开始时间
     */
    @Column(name = "XC_KSSJ")
    private String xcKssj;

    /**
     * 行程结束时间
     */
    @Column(name = "XC_JSSJ")
    private String xcJssj;

    /**
     * 终端编号
     */
    @Column(name = "CL_ZDBH")
    private String clZdbh;

    /**
     * 行程开始结束点
     */
    @Column(name = "XC_START_END")
    private String xcStartEnd;

    /**
     * 开始地址
     */
    @Column(name = "start_address")
    private String startAddress;

    /**
     * 结束地址
     */
    @Column(name = "end_address")
    private String endAddress;

    /**
     *   行程里程
     */
    @Column(name = "XC_LC")
    private String xcLc;

    @Column(name  = "XC_SC" )
    private String xcSc;

    /**
     * 平均时速
     */
    @Column(name = "XC_PJSS")
    private String xcPjss;

    /**
     * 最高时速
     */
    @Column(name = "XC_ZGSS")
    private String xcZgss;

    /**
     * 超速次数
     */
    @Column(name = "XC_CSCS")
    private String xcCscs;

    /**
     * 急加速次数
     */
    @Column(name = "XC_JJSCS")
    private String xcJjscs;

    /**
     * 急刹车次数
     */
    @Column(name = "XC_JSCCS")
    private String xcJsccs;

    /**
     * 急转弯次数
     */
    @Column(name = "XC_JZWCS")
    private String xcJzwcs;

    /**
     * 疲劳驾驶
     */
    @Column(name = "pljs")
    private String pljs;

    private static final long serialVersionUID = 1L;

    public enum InnerColumn {
        id("ID"),
        xcKssj("XC_KSSJ"),
        xcJssj("XC_JSSJ"),
        clZdbh("CL_ZDBH"),
        xcStartEnd("XC_START_END"),
        xcLc("XC_LC"),
        startAddress("start_address"),
        endAddress("end_address"),
        xcSc("XC_SC"),
        xcPjss("XC_PJSS"),
        xcZgss("XC_ZGSS"),
        xcJjscs("XC_JJSCS"),
        xcCscs("XC_CSCS"),
        pljs("pljs"),
        xcJsccs("XC_JSCCS"),
        xcJzwcs("XC_JZWCS");

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
