package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_ZP_XL")
public class ClZpXl implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 站牌ID
     */
    @Column(name = "ZP_ID")
    private String zpId;

    /**
     * 线路ID
     */
    @Column(name = "XL_ID")
    private String xlId;

    /**
     * 创建时间
     */
    @Column(name = "CJSJ")
    private Date cjsj;

    /**
     * 创建人
     */
    @Column(name = "CJR")
    private String cjr;

    private static final long serialVersionUID = 1L;

    /**
     * 获取ID
     *
     * @return ID - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取站牌ID
     *
     * @return ZP_ID - 站牌ID
     */
    public String getZpId() {
        return zpId;
    }

    /**
     * 设置站牌ID
     *
     * @param zpId 站牌ID
     */
    public void setZpId(String zpId) {
        this.zpId = zpId;
    }

    /**
     * 获取线路ID
     *
     * @return XL_ID - 线路ID
     */
    public String getXlId() {
        return xlId;
    }

    /**
     * 设置线路ID
     *
     * @param xlId 线路ID
     */
    public void setXlId(String xlId) {
        this.xlId = xlId;
    }

    /**
     * 获取创建时间
     *
     * @return CJSJ - 创建时间
     */
    public Date getCjsj() {
        return cjsj;
    }

    /**
     * 设置创建时间
     *
     * @param cjsj 创建时间
     */
    public void setCjsj(Date cjsj) {
        this.cjsj = cjsj;
    }

    /**
     * 获取创建人
     *
     * @return CJR - 创建人
     */
    public String getCjr() {
        return cjr;
    }

    /**
     * 设置创建人
     *
     * @param cjr 创建人
     */
    public void setCjr(String cjr) {
        this.cjr = cjr;
    }

    public enum InnerColumn {
        id("ID"),
        zpId("ZP_ID"),
        xlId("XL_ID"),
        cjsj("CJSJ"),
        cjr("CJR");

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