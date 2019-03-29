package com.ldz.dao.biz.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 客户端表
 */
@Getter
@Setter
@Table(name = "CL_CLIENT")
public class ClClient implements Serializable {

    @Id
    private String clientId;
    private String secret;
    private String state;
    private String name;
    private String type;
    private String cjsj;
    private String cjr;



    public enum InnerColumn {
        clientId("client_id"),
        secret("secret"),
        state("state"),
        name("name"),
        type("type"),
        cjsj("cjsj"),
        cjr("cjr"),
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
