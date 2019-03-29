package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_ZNZP")
public class ClZnzp implements Serializable {
    /**
     * 终端编号
     */
    @Id
    @Column(name = "ZDBH")
    @NotNull(message="终端编号不能为空")
    private String zdbh;

    /**
     * 名称
     */
    @Column(name = "MC")
    @NotNull(message="名称不能为空")
    private String mc;

    /**
     * 型号
     */
    @Column(name = "XH")
    @NotNull(message="型号不能为空")
    private String xh;

    /**
     * 厂商
     */
    @Column(name = "CS")
    @NotNull(message="厂商不能为空")
    private String cs;

    /**
     * 地址
     */
    @Column(name = "DZ")
    @NotNull(message="地址不能为空")
    private String dz;

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

    /**
     * 修改时间
     */
    @Column(name = "XGSJ")
    private Date xgsj;

    /**
     * 修改人
     */
    @Column(name = "XGR")
    private String xgr;

    /**
     * 站点ID
     */
    @Column(name = "ZD_ID")
    private String zdId;

    /**
     * 备注
     */
    @Column(name = "TIPS")
    private String tips;
    /**
     * 在线状态
     */
    @Column(name = "ZXZT")
    private String zxzt;

    @Transient
    private String xlIds;

    public String getXlIds() {
        return xlIds;
    }

    public void setXlIds(String xlIds) {
        this.xlIds = xlIds;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getZxzt() {
        return zxzt;
    }

    public void setZxzt(String zxzt) {
        this.zxzt = zxzt;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 获取终端编号
     *
     * @return ZDBH - 终端编号
     */
    public String getZdbh() {
        return zdbh;
    }

    /**
     * 设置终端编号
     *
     * @param zdbh 终端编号
     */
    public void setZdbh(String zdbh) {
        this.zdbh = zdbh;
    }

    /**
     * 获取名称
     *
     * @return MC - 名称
     */
    public String getMc() {
        return mc;
    }

    /**
     * 设置名称
     *
     * @param mc 名称
     */
    public void setMc(String mc) {
        this.mc = mc;
    }

    /**
     * 获取型号
     *
     * @return XH - 型号
     */
    public String getXh() {
        return xh;
    }

    /**
     * 设置型号
     *
     * @param xh 型号
     */
    public void setXh(String xh) {
        this.xh = xh;
    }

    /**
     * 获取厂商
     *
     * @return CS - 厂商
     */
    public String getCs() {
        return cs;
    }

    /**
     * 设置厂商
     *
     * @param cs 厂商
     */
    public void setCs(String cs) {
        this.cs = cs;
    }

    /**
     * 获取地址
     *
     * @return DZ - 地址
     */
    public String getDz() {
        return dz;
    }

    /**
     * 设置地址
     *
     * @param dz 地址
     */
    public void setDz(String dz) {
        this.dz = dz;
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
     * 获取站点ID
     *
     * @return ZD_ID - 站点ID
     */
    public String getZdId() {
        return zdId;
    }

    /**
     * 设置站点ID
     *
     * @param zdId 站点ID
     */
    public void setZdId(String zdId) {
        this.zdId = zdId;
    }

    public enum InnerColumn {
        zdbh("ZDBH"),
        mc("MC"),
        xh("XH"),
        cs("CS"),
        dz("DZ"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        zxzt("ZXZT"),
        zdId("ZD_ID");

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
