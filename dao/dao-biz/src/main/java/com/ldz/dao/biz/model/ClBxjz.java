package com.ldz.dao.biz.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 报销记账
 *
 */
@Getter
@Setter
@Table(name = "CL_BXJZ")
public class ClBxjz implements Serializable {
    /**
     *
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 报销人
     */
    @Column(name = "BXR")
    private String bxr;

    /**
     * 报销事项
     */
    @Column(name = "BXSX")
    private String bxsx;

    /**
     * 报销时间
     */
    @Column(name = "BXSJ")
    private Date bxsj;

    /**
     * 报销金额
     */
    @Column(name = "BXJE")
    private Float bxje;

    /**
     * 发票数量
     */
    @Column(name = "FPSL")
    private Integer fpsl;

    /**
     * 创建人
     */
    @Column(name = "CJR")
    private String cjr;

    /**
     * 创建时间
     */
    @Column(name = "CJSJ")
    private Date cjsj;
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


    private static final long serialVersionUID = 1L;


    public enum InnerColumn {
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        jgdm("JGDM"),
        jgmc("JGMC");

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