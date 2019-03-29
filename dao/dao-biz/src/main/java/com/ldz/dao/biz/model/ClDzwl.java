package com.ldz.dao.biz.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Table(name = "CL_DZWL")
public class ClDzwl implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 围栏名称
     */
    @Column(name = "WLMC")
    private String wlmc;

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
     * 面积
     */
    @Column(name = "MJ")
    private Double mj;

    /**
     * 地理信息坐标
     */
    @Column(name = "DLXXZB")
    private String dlxxzb;

    /**
     * 开始经度
     */
    @Column(name = "KSJD")
    private BigDecimal ksjd;

    /**
     * 开始纬度
     */
    @Column(name = "KSWD")
    private BigDecimal kswd;

    /**
     * 状态
     */
    @Column(name = "ZT")
    private String zt;

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
     * 围栏范围
     */
    @Column(name = "WLFW")
    private String wlfw;

    /**
     * 围栏来源  app 或  pc
     */
    @Column(name = "WLLY")
    private String wlly;

    /**
     * 用户id
     */
    @Column(name = "yh_id")
    private String yhId;

    @Transient
    private List<ClCl> cls;

    private static final long serialVersionUID = 1L;



    public enum InnerColumn {
        id("ID"),
        wlmc("WLMC"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        mj("MJ"),
        dlxxzb("DLXXZB"),
        ksjd("KSJD"),
        kswd("KSWD"),
        zt("ZT"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        wlfw("WLFW"),
        wlly("WLLY"),
        yhId("yh_id");

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