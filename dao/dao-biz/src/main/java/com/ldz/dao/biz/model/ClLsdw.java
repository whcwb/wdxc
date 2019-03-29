package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_LSDW")
public class ClLsdw implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 单位名称
     */
    @Column(name = "DWMC")
    private String dwmc;

    /**
     * 创建人
     */
    @Column(name = "CJR")
    private String cjr;

    /**
     * 创建时间
     */
    @Column(name = "CJSJ")
    private Date cjsj;

    /**
     * 登记次数
     */
    @Column(name = "DJCS")
    private Short djcs;

    /**
     * 修改人
     */
    @Column(name = "XGR")
    private String xgr;

    /**
     * 修改时间
     */
    @Column(name = "XGSJ")
    private Date xgsj;

    /**
     * 单位编号
     */
    @Column(name = "DWBH")
    private String dwbh;

    /**
     * 状态
     */
    @Column(name = "ZT")
    private String zt;

    /**
     * 联系人
     */
    @Column(name = "LXR")
    private String lxr;

    /**
     * 联系电话
     */
    @Column(name = "LXDH")
    private String lxdh;

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
     * 获取单位名称
     *
     * @return DWMC - 单位名称
     */
    public String getDwmc() {
        return dwmc;
    }

    /**
     * 设置单位名称
     *
     * @param dwmc 单位名称
     */
    public void setDwmc(String dwmc) {
        this.dwmc = dwmc;
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
     * 获取登记次数
     *
     * @return DJCS - 登记次数
     */
    public Short getDjcs() {
        return djcs;
    }

    /**
     * 设置登记次数
     *
     * @param djcs 登记次数
     */
    public void setDjcs(Short djcs) {
        this.djcs = djcs;
    }

    /**
     * 获取修改人
     *
     * @return XGR - 修改人
     */
    public String getXgr() {
        return xgr;
    }

    /**
     * 设置修改人
     *
     * @param xgr 修改人
     */
    public void setXgr(String xgr) {
        this.xgr = xgr;
    }

    /**
     * 获取修改时间
     *
     * @return XGSJ - 修改时间
     */
    public Date getXgsj() {
        return xgsj;
    }

    /**
     * 设置修改时间
     *
     * @param xgsj 修改时间
     */
    public void setXgsj(Date xgsj) {
        this.xgsj = xgsj;
    }

    /**
     * 获取单位编号
     *
     * @return DWBH - 单位编号
     */
    public String getDwbh() {
        return dwbh;
    }

    /**
     * 设置单位编号
     *
     * @param dwbh 单位编号
     */
    public void setDwbh(String dwbh) {
        this.dwbh = dwbh;
    }

    /**
     * 获取状态
     *
     * @return ZT - 状态
     */
    public String getZt() {
        return zt;
    }

    /**
     * 设置状态
     *
     * @param zt 状态
     */
    public void setZt(String zt) {
        this.zt = zt;
    }

    /**
     * 获取联系人
     *
     * @return LXR - 联系人
     */
    public String getLxr() {
        return lxr;
    }

    /**
     * 设置联系人
     *
     * @param lxr 联系人
     */
    public void setLxr(String lxr) {
        this.lxr = lxr;
    }

    /**
     * 获取联系电话
     *
     * @return LXDH - 联系电话
     */
    public String getLxdh() {
        return lxdh;
    }

    /**
     * 设置联系电话
     *
     * @param lxdh 联系电话
     */
    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public enum InnerColumn {
        id("ID"),
        dwmc("DWMC"),
        cjr("CJR"),
        cjsj("CJSJ"),
        djcs("DJCS"),
        xgr("XGR"),
        xgsj("XGSJ"),
        dwbh("DWBH"),
        zt("ZT"),
        lxr("LXR"),
        lxdh("LXDH");

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