package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "SYS_JS_GN")
public class SysJsGn implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "JSDM")
    private String jsdm;

    @Column(name = "GNDM")
    private String gndm;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "FWDM")
    private String fwdm;

    @Column(name = "FGNDM")
    private String fgndm;

    private static final long serialVersionUID = 1L;

    /**
     * @return ID
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
     * @return JSDM
     */
    public String getJsdm() {
        return jsdm;
    }

    /**
     * @param jsdm
     */
    public void setJsdm(String jsdm) {
        this.jsdm = jsdm;
    }

    /**
     * @return GNDM
     */
    public String getGndm() {
        return gndm;
    }

    /**
     * @param gndm
     */
    public void setGndm(String gndm) {
        this.gndm = gndm;
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
     * @return CJR
     */
    public String getCjr() {
        return cjr;
    }

    /**
     * @param cjr
     */
    public void setCjr(String cjr) {
        this.cjr = cjr;
    }

    /**
     * @return FWDM
     */
    public String getFwdm() {
        return fwdm;
    }

    /**
     * @param fwdm
     */
    public void setFwdm(String fwdm) {
        this.fwdm = fwdm;
    }

    /**
     * @return FGNDM
     */
    public String getFgndm() {
        return fgndm;
    }

    /**
     * @param fgndm
     */
    public void setFgndm(String fgndm) {
        this.fgndm = fgndm;
    }

    public enum InnerColumn {
        id("ID"),
        jsdm("JSDM"),
        gndm("GNDM"),
        cjsj("CJSJ"),
        cjr("CJR"),
        fwdm("FWDM"),
        fgndm("FGNDM"),
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