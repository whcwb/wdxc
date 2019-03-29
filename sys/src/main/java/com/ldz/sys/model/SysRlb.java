package com.ldz.sys.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "SYS_RLB")
public class SysRlb implements Serializable {
    @Id
    @Column(name = "RQDM")
    @GeneratedValue(generator = "JDBC")
    private String rqdm;

    @Column(name = "XQ")
    private BigDecimal xq;

    @Column(name = "ZT")
    private String zt;

    @Column(name = "RQ")
    private String rq;

    private static final long serialVersionUID = 1L;

    /**
     * @return RQDM
     */
    public String getRqdm() {
        return rqdm;
    }

    /**
     * @param rqdm
     */
    public void setRqdm(String rqdm) {
        this.rqdm = rqdm;
    }

    /**
     * @return XQ
     */
    public BigDecimal getXq() {
        return xq;
    }

    /**
     * @param xq
     */
    public void setXq(BigDecimal xq) {
        this.xq = xq;
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

    /**
     * @return RQ
     */
    public String getRq() {
        return rq;
    }

    /**
     * @param rq
     */
    public void setRq(String rq) {
        this.rq = rq;
    }

    public enum InnerColumn {
        rqdm("RQDM"),
        xq("XQ"),
        zt("ZT"),
        rq("RQ");

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