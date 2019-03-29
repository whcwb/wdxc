package com.ldz.dao.biz.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;

@ToString
@Getter
@Setter
@Table(name = "CL_ZD_YH")
public class ClZdYh {

    @Id
    private String id;
    private String deviceId;
    private String userId;

    public enum InnerColumn {
        id("Id"),
        deviceId("device_id"),
        userId("user_id"),
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
