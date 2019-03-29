package com.ldz.sys.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "SYS_YH_JS")
public class SysYhJs implements Serializable {
    @Id
    @Column(name = "YHJS_ID")
    private String yhjsId;

    @Column(name = "YH_ID")
    private String yhId;

    @Column(name = "JS_ID")
    private String jsId;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "CJSJ")
    private Date cjsj;

    private static final long serialVersionUID = 1L;

    /**
     * @return YHJS_ID
     */
    public String getYhjsId() {
        return yhjsId;
    }

    /**
     * @param yhjsId
     */
    public void setYhjsId(String yhjsId) {
        this.yhjsId = yhjsId;
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
     * @return JS_ID
     */
    public String getJsId() {
        return jsId;
    }

    /**
     * @param jsId
     */
    public void setJsId(String jsId) {
        this.jsId = jsId;
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

    public enum InnerColumn {
        yhjsId("YHJS_ID"),
        yhId("YH_ID"),
        jsId("JS_ID"),
        cjr("CJR"),
        cjsj("CJSJ");

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