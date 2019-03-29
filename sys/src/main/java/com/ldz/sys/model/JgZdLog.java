package com.ldz.sys.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "jg_zd_log")
public class JgZdLog implements Serializable {
    @Id
    @Column(name = "ID")
    private String id;

    private String jgdm;

    private String jgmc;

    /**
     * 终端数量
     */
    @Column(name = "zd_sl")
    private Integer zdSl;

    /**
     * 增加还是减少
     */
    private String lx;

    private String cjsj;

    private String cjr;

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
     * @return jgdm
     */
    public String getJgdm() {
        return jgdm;
    }

    /**
     * @param jgdm
     */
    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }

    /**
     * @return jgmc
     */
    public String getJgmc() {
        return jgmc;
    }

    /**
     * @param jgmc
     */
    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    /**
     * 获取终端数量
     *
     * @return zd_sl - 终端数量
     */
    public Integer getZdSl() {
        return zdSl;
    }

    /**
     * 设置终端数量
     *
     * @param zdSl 终端数量
     */
    public void setZdSl(Integer zdSl) {
        this.zdSl = zdSl;
    }

    /**
     * 获取增加还是减少
     *
     * @return lx - 增加还是减少
     */
    public String getLx() {
        return lx;
    }

    /**
     * 设置增加还是减少
     *
     * @param lx 增加还是减少
     */
    public void setLx(String lx) {
        this.lx = lx;
    }

    /**
     * @return cjsj
     */
    public String getCjsj() {
        return cjsj;
    }

    /**
     * @param cjsj
     */
    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    /**
     * @return cjr
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

    public enum InnerColumn {
        id("ID"),
        jgdm("jgdm"),
        jgmc("jgmc"),
        zdSl("zd_sl"),
        lx("lx"),
        cjsj("cjsj"),
        cjr("cjr");

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