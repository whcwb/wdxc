package com.ldz.wechat.module.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 车辆表
 * @author Lee
 *
 */
@ToString
@Getter
@Setter
@Table(name = "CL_CL")
public class ClCl implements Serializable {
    /**
     * 车辆id
     */
    @Id
    @Column(name = "CL_ID")
    private String clId;

    /**
     * 车牌号
     */
    @Column(name = "CPH")
    @NotNull(message="车牌号不为空")
    private String cph;

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
     * 车型
     */
    @Column(name = "CX")
    @NotNull(message="车型不为空")
    private String cx;

    /**
     * 载客量
     */
    @Column(name = "ZKL")
    @NotNull(message="载客量不为空")
    private Short zkl;

    /**
     * 等级
     */
    @Column(name = "DL")
    private String dl;

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
     * 司机id
     */
    @Column(name = "SJ_ID")
    private String sjId;

    /**
     * 司机姓名
     */
    @Column(name = "SJXM")
    private String sjxm;

    /**
     * 状态
     */
    @Column(name = "ZT")
    @NotNull(message="状态不为空")
    private String zt;

    /**
     * 图片
     */
    @Column(name = "TP")
    private String tp;

    /**
     * 生产商
     */
    @Column(name = "SCS")
    private String scs;

    /**
     * 型号
     */
    @Column(name = "XH")
    private String xh;

    /**
     * 终端编号
     */
    @Column(name = "ZDBH")
    private String zdbh;

    /**
     * 初次登记日期
     */
    @Column(name = "CCDJRQ")
    private Date ccdjrq;

    /**
     * 车队编号
     */
    @Column(name = "CDBH")
    private String cdbh;

    /**
     * 保险公司名称
     */
    @Column(name = "BXGSMC")
    private String bxgsmc;

    /**
     * 保险时间
     */
    @Column(name = "BXSJ")
    private Date bxsj;

    /**
     * 年审时间
     */
    @Column(name = "NSSJ")
    private Date nssj;
    /**
     * OBD编号
     */
    @Column(name = "OBD_CODE")
    private String obdCode;


	private static final long serialVersionUID = 1L;

    /**
     * 获取车辆id
     *
     * @return CL_ID - 车辆id
     */
    public String getClId() {
        return clId;
    }

    /**
     * 设置车辆id
     *
     * @param clId 车辆id
     */
    public void setClId(String clId) {
        this.clId = clId;
    }

    /**
     * 获取车牌号
     *
     * @return CPH - 车牌号
     */
    public String getCph() {
        return cph;
    }

    /**
     * 设置车牌号
     *
     * @param cph 车牌号
     */
    public void setCph(String cph) {
        this.cph = cph;
    }

    /**
     * 获取机构代码
     *
     * @return JGDM - 机构代码
     */
    public String getJgdm() {
        return jgdm;
    }

    /**
     * 设置机构代码
     *
     * @param jgdm 机构代码
     */
    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
    }

    /**
     * 获取机构名称
     *
     * @return JGMC - 机构名称
     */
    public String getJgmc() {
        return jgmc;
    }

    /**
     * 设置机构名称
     *
     * @param jgmc 机构名称
     */
    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    /**
     * 获取车型
     *
     * @return CX - 车型
     */
    public String getCx() {
        return cx;
    }

    /**
     * 设置车型
     *
     * @param cx 车型
     */
    public void setCx(String cx) {
        this.cx = cx;
    }

    /**
     * 获取载客量
     *
     * @return ZKL - 载客量
     */
    public Short getZkl() {
        return zkl;
    }

    /**
     * 设置载客量
     *
     * @param zkl 载客量
     */
    public void setZkl(Short zkl) {
        this.zkl = zkl;
    }

    /**
     * 获取等级
     *
     * @return DL - 等级
     */
    public String getDl() {
        return dl;
    }

    /**
     * 设置等级
     *
     * @param dl 等级
     */
    public void setDl(String dl) {
        this.dl = dl;
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
     * 获取司机id
     *
     * @return SJ_ID - 司机id
     */
    public String getSjId() {
        return sjId;
    }

    /**
     * 设置司机id
     *
     * @param sjId 司机id
     */
    public void setSjId(String sjId) {
        this.sjId = sjId;
    }

    /**
     * 获取司机姓名
     *
     * @return SJXM - 司机姓名
     */
    public String getSjxm() {
        return sjxm;
    }

    /**
     * 设置司机姓名
     *
     * @param sjxm 司机姓名
     */
    public void setSjxm(String sjxm) {
        this.sjxm = sjxm;
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
     * 获取图片
     *
     * @return TP - 图片
     */
    public String getTp() {
        return tp;
    }

    /**
     * 设置图片
     *
     * @param tp 图片
     */
    public void setTp(String tp) {
        this.tp = tp;
    }

    /**
     * 获取生产商
     *
     * @return SCS - 生产商
     */
    public String getScs() {
        return scs;
    }

    /**
     * 设置生产商
     *
     * @param scs 生产商
     */
    public void setScs(String scs) {
        this.scs = scs;
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
     * 获取初次登记日期
     *
     * @return CCDJRQ - 初次登记日期
     */
    public Date getCcdjrq() {
        return ccdjrq;
    }

    /**
     * 设置初次登记日期
     *
     * @param ccdjrq 初次登记日期
     */
    public void setCcdjrq(Date ccdjrq) {
        this.ccdjrq = ccdjrq;
    }

    /**
     * 获取车队编号
     *
     * @return CDBH - 车队编号
     */
    public String getCdbh() {
        return cdbh;
    }

    /**
     * 设置车队编号
     *
     * @param cdbh 车队编号
     */
    public void setCdbh(String cdbh) {
        this.cdbh = cdbh;
    }

    /**
     * 获取保险公司名称
     *
     * @return BXGSMC - 保险公司名称
     */
    public String getBxgsmc() {
        return bxgsmc;
    }

    /**
     * 设置保险公司名称
     *
     * @param bxgsmc 保险公司名称
     */
    public void setBxgsmc(String bxgsmc) {
        this.bxgsmc = bxgsmc;
    }

    /**
     * 获取保险时间
     *
     * @return BXSJ - 保险时间
     */
    public Date getBxsj() {
        return bxsj;
    }

    /**
     * 设置保险时间
     *
     * @param bxsj 保险时间
     */
    public void setBxsj(Date bxsj) {
        this.bxsj = bxsj;
    }

    /**
     * 获取年审时间
     *
     * @return NSSJ - 年审时间
     */
    public Date getNssj() {
        return nssj;
    }

    /**
     * 设置年审时间
     *
     * @param nssj 年审时间
     */
    public void setNssj(Date nssj) {
        this.nssj = nssj;
    }

    public enum InnerColumn {
        clId("CL_ID"),
        cph("CPH"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        cx("CX"),
        zkl("ZKL"),
        dl("DL"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        sjId("SJ_ID"),
        sjxm("SJXM"),
        zt("ZT"),
        tp("TP"),
        scs("SCS"),
        xh("XH"),
        zdbh("ZDBH"),
        ccdjrq("CCDJRQ"),
        cdbh("CDBH"),
        bxgsmc("BXGSMC"),
        bxsj("BXSJ"),
        nssj("NSSJ");

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