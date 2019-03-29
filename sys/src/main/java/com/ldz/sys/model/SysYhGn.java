package com.ldz.sys.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Table(name = "sys_yh_gn")
public class SysYhGn implements Serializable {
    /**
     * 用户角色关联表id
     */
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "yhid")
    private String yhid;
    /**
     * gndm
     */
    @Column(name = "gndm")
    private String gndm;
    /**
     * api前缀
     */
    @Column(name = "api_qz")
    private String apiQz;


    private static final long serialVersionUID = 1L;

    public enum InnerColumn {
        id("id"),
        yhid("yhid"),
        gndm("gndm"),
        apiQz("api_qz"),
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
