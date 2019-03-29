package com.ldz.dao.biz.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "cl_report_log")
@Getter
@Setter
public class ClReportLog {

    @Id
    private String id;

    private String cjsj;

    private String sylc;

    private String lc;

    private String zdbh;

    private String qlc;

    private String kssj;

    private String jssj;

    public enum InnerColumn {
        id("ID"),
        cjsj("cjsj"),sylc("sylc"),lc("lc"),zdbh("zdbh"),
        qlc("qlc"),kssj("kssj"),jssj("jssj")
        ;

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
