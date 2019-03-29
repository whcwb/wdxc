package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_DD")
public class ClDd implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 司机确认时间
     */
    @Column(name = "SJQRSJ")
    private Date sjqrsj;

    /**
     * 候车地址
     */
    @Column(name = "HCDZ")
    private String hcdz;

    /**
     * 目的地
     */
    @Column(name = "MDD")
    private String mdd;

    /**
     * 车牌号
     */
    @Column(name = "CPH")
    private String cph;

    /**
     * 司机
     */
    @Column(name = "SJ")
    private String sj;

    /**
     * 司机姓名
     */
    @Column(name = "SJXM")
    private String sjxm;

    /**
     * 总价
     */
    @Column(name = "ZJ")
    private Double zj;

    /**
     * 时长
     */
    @Column(name = "SC")
    private Short sc;

    /**
     * 单价
     */
    @Column(name = "DJ")
    private Double dj;

    /**
     * 里程
     */
    @Column(name = "LC")
    private Double lc;

    /**
     * 时长费
     */
    @Column(name = "SCF")
    private Double scf;

    /**
     * 里程费
     */
    @Column(name = "LCF")
    private Double lcf;

    /**
     * 乘客姓名
     */
    @Column(name = "CK")
    private String ck;

    /**
     * 乘客职务
     */
    @Column(name = "CKZW")
    private String ckzw;

    /**
     * 乘客联系电话
     */
    @Column(name = "CKLXDH")
    private String cklxdh;

    /**
     * 座位数
     */
    @Column(name = "ZWS")
    private Short zws;

    /**
     * 乘客类型
     */
    @Column(name = "CKLX")
    private String cklx;

    /**
     * 预约时间
     */
    @Column(name = "YYSJ")
    private Date yysj;

    /**
     * 订单状态
     */
    @Column(name = "DDZT")
    private String ddzt;

    /**
     * 付款状态
     */
    @Column(name = "FKZT")
    private String fkzt;

    /**
     * 付款方式
     */
    @Column(name = "FKFS")
    private String fkfs;

    /**
     * 付款时间
     */
    @Column(name = "FKSJ")
    private Date fksj;

    /**
     * 评价等级
     */
    @Column(name = "PJDJ")
    private Short pjdj;

    /**
     * 评价内容
     */
    @Column(name = "PJNR")
    private String pjnr;

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
     * 车辆id
     */
    @Column(name = "CL_ID")
    private String clId;

    /**
     * 过路费
     */
    @Column(name = "GLF")
    private Double glf;

    /**
     * 事由
     */
    @Column(name = "SY")
    private String sy;

    /**
     * 车辆类型
     */
    @Column(name = "CLLX")
    private String cllx;

    /**
     * 往返
     */
    @Column(name = "WF")
    private String wf;

    /**
     * 队长确认时间
     */
    @Column(name = "DZQRSJ")
    private Date dzqrsj;

    /**
     * 审核时间
     */
    @Column(name = "SHSJ")
    private Date shsj;

    /**
     * 队长姓名
     */
    @Column(name = "DZXM")
    private String dzxm;

    /**
     * 付款备注
     */
    @Column(name = "FKBZ")
    private String fkbz;

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
     * 获取司机确认时间
     *
     * @return SJQRSJ - 司机确认时间
     */
    public Date getSjqrsj() {
        return sjqrsj;
    }

    /**
     * 设置司机确认时间
     *
     * @param sjqrsj 司机确认时间
     */
    public void setSjqrsj(Date sjqrsj) {
        this.sjqrsj = sjqrsj;
    }

    /**
     * 获取候车地址
     *
     * @return HCDZ - 候车地址
     */
    public String getHcdz() {
        return hcdz;
    }

    /**
     * 设置候车地址
     *
     * @param hcdz 候车地址
     */
    public void setHcdz(String hcdz) {
        this.hcdz = hcdz;
    }

    /**
     * 获取目的地
     *
     * @return MDD - 目的地
     */
    public String getMdd() {
        return mdd;
    }

    /**
     * 设置目的地
     *
     * @param mdd 目的地
     */
    public void setMdd(String mdd) {
        this.mdd = mdd;
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
     * 获取司机
     *
     * @return SJ - 司机
     */
    public String getSj() {
        return sj;
    }

    /**
     * 设置司机
     *
     * @param sj 司机
     */
    public void setSj(String sj) {
        this.sj = sj;
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
     * 获取总价
     *
     * @return ZJ - 总价
     */
    public Double getZj() {
        return zj;
    }

    /**
     * 设置总价
     *
     * @param zj 总价
     */
    public void setZj(Double zj) {
        this.zj = zj;
    }

    /**
     * 获取时长
     *
     * @return SC - 时长
     */
    public Short getSc() {
        return sc;
    }

    /**
     * 设置时长
     *
     * @param sc 时长
     */
    public void setSc(Short sc) {
        this.sc = sc;
    }

    /**
     * 获取单价
     *
     * @return DJ - 单价
     */
    public Double getDj() {
        return dj;
    }

    /**
     * 设置单价
     *
     * @param dj 单价
     */
    public void setDj(Double dj) {
        this.dj = dj;
    }

    /**
     * 获取里程
     *
     * @return LC - 里程
     */
    public Double getLc() {
        return lc;
    }

    /**
     * 设置里程
     *
     * @param lc 里程
     */
    public void setLc(Double lc) {
        this.lc = lc;
    }

    /**
     * 获取时长费
     *
     * @return SCF - 时长费
     */
    public Double getScf() {
        return scf;
    }

    /**
     * 设置时长费
     *
     * @param scf 时长费
     */
    public void setScf(Double scf) {
        this.scf = scf;
    }

    /**
     * 获取里程费
     *
     * @return LCF - 里程费
     */
    public Double getLcf() {
        return lcf;
    }

    /**
     * 设置里程费
     *
     * @param lcf 里程费
     */
    public void setLcf(Double lcf) {
        this.lcf = lcf;
    }

    /**
     * 获取乘客姓名
     *
     * @return CK - 乘客姓名
     */
    public String getCk() {
        return ck;
    }

    /**
     * 设置乘客姓名
     *
     * @param ck 乘客姓名
     */
    public void setCk(String ck) {
        this.ck = ck;
    }

    /**
     * 获取乘客职务
     *
     * @return CKZW - 乘客职务
     */
    public String getCkzw() {
        return ckzw;
    }

    /**
     * 设置乘客职务
     *
     * @param ckzw 乘客职务
     */
    public void setCkzw(String ckzw) {
        this.ckzw = ckzw;
    }

    /**
     * 获取乘客联系电话
     *
     * @return CKLXDH - 乘客联系电话
     */
    public String getCklxdh() {
        return cklxdh;
    }

    /**
     * 设置乘客联系电话
     *
     * @param cklxdh 乘客联系电话
     */
    public void setCklxdh(String cklxdh) {
        this.cklxdh = cklxdh;
    }

    /**
     * 获取座位数
     *
     * @return ZWS - 座位数
     */
    public Short getZws() {
        return zws;
    }

    /**
     * 设置座位数
     *
     * @param zws 座位数
     */
    public void setZws(Short zws) {
        this.zws = zws;
    }

    /**
     * 获取乘客类型
     *
     * @return CKLX - 乘客类型
     */
    public String getCklx() {
        return cklx;
    }

    /**
     * 设置乘客类型
     *
     * @param cklx 乘客类型
     */
    public void setCklx(String cklx) {
        this.cklx = cklx;
    }

    /**
     * 获取预约时间
     *
     * @return YYSJ - 预约时间
     */
    public Date getYysj() {
        return yysj;
    }

    /**
     * 设置预约时间
     *
     * @param yysj 预约时间
     */
    public void setYysj(Date yysj) {
        this.yysj = yysj;
    }

    /**
     * 获取订单状态
     *
     * @return DDZT - 订单状态
     */
    public String getDdzt() {
        return ddzt;
    }

    /**
     * 设置订单状态
     *
     * @param ddzt 订单状态
     */
    public void setDdzt(String ddzt) {
        this.ddzt = ddzt;
    }

    /**
     * 获取付款状态
     *
     * @return FKZT - 付款状态
     */
    public String getFkzt() {
        return fkzt;
    }

    /**
     * 设置付款状态
     *
     * @param fkzt 付款状态
     */
    public void setFkzt(String fkzt) {
        this.fkzt = fkzt;
    }

    /**
     * 获取付款方式
     *
     * @return FKFS - 付款方式
     */
    public String getFkfs() {
        return fkfs;
    }

    /**
     * 设置付款方式
     *
     * @param fkfs 付款方式
     */
    public void setFkfs(String fkfs) {
        this.fkfs = fkfs;
    }

    /**
     * 获取付款时间
     *
     * @return FKSJ - 付款时间
     */
    public Date getFksj() {
        return fksj;
    }

    /**
     * 设置付款时间
     *
     * @param fksj 付款时间
     */
    public void setFksj(Date fksj) {
        this.fksj = fksj;
    }

    /**
     * 获取评价等级
     *
     * @return PJDJ - 评价等级
     */
    public Short getPjdj() {
        return pjdj;
    }

    /**
     * 设置评价等级
     *
     * @param pjdj 评价等级
     */
    public void setPjdj(Short pjdj) {
        this.pjdj = pjdj;
    }

    /**
     * 获取评价内容
     *
     * @return PJNR - 评价内容
     */
    public String getPjnr() {
        return pjnr;
    }

    /**
     * 设置评价内容
     *
     * @param pjnr 评价内容
     */
    public void setPjnr(String pjnr) {
        this.pjnr = pjnr;
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
     * 获取过路费
     *
     * @return GLF - 过路费
     */
    public Double getGlf() {
        return glf;
    }

    /**
     * 设置过路费
     *
     * @param glf 过路费
     */
    public void setGlf(Double glf) {
        this.glf = glf;
    }

    /**
     * 获取事由
     *
     * @return SY - 事由
     */
    public String getSy() {
        return sy;
    }

    /**
     * 设置事由
     *
     * @param sy 事由
     */
    public void setSy(String sy) {
        this.sy = sy;
    }

    /**
     * 获取车辆类型
     *
     * @return CLLX - 车辆类型
     */
    public String getCllx() {
        return cllx;
    }

    /**
     * 设置车辆类型
     *
     * @param cllx 车辆类型
     */
    public void setCllx(String cllx) {
        this.cllx = cllx;
    }

    /**
     * 获取往返
     *
     * @return WF - 往返
     */
    public String getWf() {
        return wf;
    }

    /**
     * 设置往返
     *
     * @param wf 往返
     */
    public void setWf(String wf) {
        this.wf = wf;
    }

    /**
     * 获取队长确认时间
     *
     * @return DZQRSJ - 队长确认时间
     */
    public Date getDzqrsj() {
        return dzqrsj;
    }

    /**
     * 设置队长确认时间
     *
     * @param dzqrsj 队长确认时间
     */
    public void setDzqrsj(Date dzqrsj) {
        this.dzqrsj = dzqrsj;
    }

    /**
     * 获取审核时间
     *
     * @return SHSJ - 审核时间
     */
    public Date getShsj() {
        return shsj;
    }

    /**
     * 设置审核时间
     *
     * @param shsj 审核时间
     */
    public void setShsj(Date shsj) {
        this.shsj = shsj;
    }

    /**
     * 获取队长姓名
     *
     * @return DZXM - 队长姓名
     */
    public String getDzxm() {
        return dzxm;
    }

    /**
     * 设置队长姓名
     *
     * @param dzxm 队长姓名
     */
    public void setDzxm(String dzxm) {
        this.dzxm = dzxm;
    }

    /**
     * 获取付款备注
     *
     * @return FKBZ - 付款备注
     */
    public String getFkbz() {
        return fkbz;
    }

    /**
     * 设置付款备注
     *
     * @param fkbz 付款备注
     */
    public void setFkbz(String fkbz) {
        this.fkbz = fkbz;
    }

    public enum InnerColumn {
        id("ID"),
        sjqrsj("SJQRSJ"),
        hcdz("HCDZ"),
        mdd("MDD"),
        cph("CPH"),
        sj("SJ"),
        sjxm("SJXM"),
        zj("ZJ"),
        sc("SC"),
        dj("DJ"),
        lc("LC"),
        scf("SCF"),
        lcf("LCF"),
        ck("CK"),
        ckzw("CKZW"),
        cklxdh("CKLXDH"),
        zws("ZWS"),
        cklx("CKLX"),
        yysj("YYSJ"),
        ddzt("DDZT"),
        fkzt("FKZT"),
        fkfs("FKFS"),
        fksj("FKSJ"),
        pjdj("PJDJ"),
        pjnr("PJNR"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgsj("XGSJ"),
        xgr("XGR"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        clId("CL_ID"),
        glf("GLF"),
        sy("SY"),
        cllx("CLLX"),
        wf("WF"),
        dzqrsj("DZQRSJ"),
        shsj("SHSJ"),
        dzxm("DZXM"),
        fkbz("FKBZ");

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