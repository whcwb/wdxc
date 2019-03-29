package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_YH_XX")
public class SysYhXx implements Serializable {
    @Id
    private String id;

    @Column(name = "YH_ID")
    private String yhId;

    @Column(name = "XX_ID")
    private String xxId;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "ZT")
    private String zt;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
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
     * @return YH_ID
     */
    public String getYhId() {
        return yhId;
    }

    /**
     * @param yhId
     */
    public void setYhId(String yhId) {
        this.yhId = yhId;
    }

    /**
     * @return XX_ID
     */
    public String getXxId() {
        return xxId;
    }

    /**
     * @param xxId
     */
    public void setXxId(String xxId) {
        this.xxId = xxId;
    }

    /**
     * @return CJSJ
     */
    public Date getCjsj() {
        return cjsj;
    }

    /**
     * @param cjsj
     */
    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    /**
     * @return ZT
     */
    public String getZt() {
        return zt;
    }

    /**
     * @param zt
     */
    public void setZt(String zt) {
        this.zt = zt;
    }

    public enum InnerColumn {
        id("id"),
        yhId("YH_ID"),
        xxId("XX_ID"),
        cjsj("CJSJ"),
        zt("ZT");

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