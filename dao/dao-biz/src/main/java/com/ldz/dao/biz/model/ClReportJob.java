package com.ldz.dao.biz.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "cl_report_job")
@Getter
@Setter
public class ClReportJob {
    @Id
    private String id;

    private String gxrq;

    private String zdbh;

    private String zxsj;

    private String sylc;


    public enum InnerColumn {
        id("ID"),
        gxrq("gxrq"),zdbh("zdbh"),zxsj("zxsj"),sylc("sylc")
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
