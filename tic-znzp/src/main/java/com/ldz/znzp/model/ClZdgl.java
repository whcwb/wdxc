package com.ldz.znzp.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_ZDGL")
public class ClZdgl implements Serializable {
    /**
     * 终端编号
     */
    @Id
    @Column(name = "ZDBH")
    private String zdbh;

    /**
     * 型号
     */
    @Column(name = "XH")
    private String xh;

    /**
     * 名称
     */
    @Column(name = "MC")
    private String mc;

    /**
     * 厂商
     */
    @Column(name = "CS")
    private String cs;

    /**
     * 状态
     */
    @Column(name = "ZT")
    private String zt;

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
     * 在线状态
     */
    @Column(name = "ZXZT")
    private String zxzt;

    /**
     * 在线时间
     */
    @Column(name = "ZXSJ")
    private Date zxsj;

    /**
     * 机构代码
     */
    @Column(name = "JGDM")
    private String jgdm;

    /**
     * 机构名称
     */
    @Column(name = "JGMC")
    private String jgmc;

    /**
     * 设备版本
     */
    @Column(name = "VERSION")
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
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
     * 获取在线状态
     *
     * @return ZXZT - 在线状态
     */
    public String getZxzt() {
        return zxzt;
    }

    /**
     * 设置在线状态
     *
     * @param zxzt 在线状态
     */
    public void setZxzt(String zxzt) {
        this.zxzt = zxzt;
    }

    /**
     * 获取在线时间
     *
     * @return ZXSJ - 在线时间
     */
    public Date getZxsj() {
        return zxsj;
    }

    /**
     * 设置在线时间
     *
     * @param zxsj 在线时间
     */
    public void setZxsj(Date zxsj) {
        this.zxsj = zxsj;
    }

    public enum InnerColumn {
        zdbh("ZDBH"),
        xh("XH"),
        mc("MC"),
        cs("CS"),
        zt("ZT"),
        cjr("CJR"),
        jgdm("JGDM"),
        cjsj("CJSJ"),
        xgr("XGR"),
        xgsj("XGSJ"),
        zxzt("ZXZT"),
        zxsj("ZXSJ");

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
