package com.ldz.sys.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Getter
@Setter
@Table(name = "SYS_PTRZ")
public class SysRz implements Serializable {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "CZLX")
    private String czlx;

    @Column(name = "CZSJ")
    private Date czsj;

    @Column(name = "CZR")
    private String czr;

    @Column(name = "DX_ID")
    private String dxId;

    @Column(name = "DXLX")
    private String dxlx;

    @Column(name = "CS")
    private String cs;

    @Column(name = "JG")
    private String jg;

    @Column(name = "ZXSJ")
    private Integer zxsj;

    @Column(name = "SM")
    private String sm;

    @Column(name = "FF")
    private String ff;

    private static final long serialVersionUID = 1L;

    public enum InnerColumn {
        id("id"),
        czlx("CZLX"),
        czsj("CZSJ"),
        czr("CZR"),
        dxId("DX_ID"),
        dxlx("DXLX"),
        cs("CS"),
        jg("JG"),
        zxsj("ZXSJ"),
        sm("SM"),
        ff("FF");

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