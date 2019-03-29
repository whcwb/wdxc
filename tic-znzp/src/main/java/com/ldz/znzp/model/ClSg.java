package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_SG")
public class ClSg implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 车牌号
     */
    @Column(name = "CPH")
    private String cph;

    /**
     * 车队编号
     */
    @Column(name = "CDBH")
    private String cdbh;

    /**
     * 机构代码
     */
    @Column(name = "JGDM")
    private String jgdm;

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
     * 事故地点
     */
    @Column(name = "SGDD")
    private String sgdd;

    /**
     * 事故时间
     */
    @Column(name = "SGSJ")
    private Date sgsj;

    /**
     * 处理结果
     */
    @Column(name = "CLJG")
    private String cljg;

    /**
     * 处理人
     */
    @Column(name = "CLR")
    private String clr;

    /**
     * 事故描述
     */
    @Column(name = "SGMS")
    private String sgms;

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
    @Column(name = "XGNR")
    private Date xgnr;

    /**
     * 修改人
     */
    @Column(name = "XGR")
    private String xgr;

    /**
     * 车辆id
     */
    @Column(name = "CL_ID")
    private String clId;

    /**
     * 处理时间
     */
    @Column(name = "CLSJ")
    private Date clsj;

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
     * 获取事故地点
     *
     * @return SGDD - 事故地点
     */
    public String getSgdd() {
        return sgdd;
    }

    /**
     * 设置事故地点
     *
     * @param sgdd 事故地点
     */
    public void setSgdd(String sgdd) {
        this.sgdd = sgdd;
    }

    /**
     * 获取事故时间
     *
     * @return SGSJ - 事故时间
     */
    public Date getSgsj() {
        return sgsj;
    }

    /**
     * 设置事故时间
     *
     * @param sgsj 事故时间
     */
    public void setSgsj(Date sgsj) {
        this.sgsj = sgsj;
    }

    /**
     * 获取处理结果
     *
     * @return CLJG - 处理结果
     */
    public String getCljg() {
        return cljg;
    }

    /**
     * 设置处理结果
     *
     * @param cljg 处理结果
     */
    public void setCljg(String cljg) {
        this.cljg = cljg;
    }

    /**
     * 获取处理人
     *
     * @return CLR - 处理人
     */
    public String getClr() {
        return clr;
    }

    /**
     * 设置处理人
     *
     * @param clr 处理人
     */
    public void setClr(String clr) {
        this.clr = clr;
    }

    /**
     * 获取事故描述
     *
     * @return SGMS - 事故描述
     */
    public String getSgms() {
        return sgms;
    }

    /**
     * 设置事故描述
     *
     * @param sgms 事故描述
     */
    public void setSgms(String sgms) {
        this.sgms = sgms;
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
     * @return XGNR - 修改时间
     */
    public Date getXgnr() {
        return xgnr;
    }

    /**
     * 设置修改时间
     *
     * @param xgnr 修改时间
     */
    public void setXgnr(Date xgnr) {
        this.xgnr = xgnr;
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
     * 获取处理时间
     *
     * @return CLSJ - 处理时间
     */
    public Date getClsj() {
        return clsj;
    }

    /**
     * 设置处理时间
     *
     * @param clsj 处理时间
     */
    public void setClsj(Date clsj) {
        this.clsj = clsj;
    }

    public enum InnerColumn {
        id("ID"),
        cph("CPH"),
        cdbh("CDBH"),
        jgdm("JGDM"),
        sj("SJ"),
        sjxm("SJXM"),
        sgdd("SGDD"),
        sgsj("SGSJ"),
        cljg("CLJG"),
        clr("CLR"),
        sgms("SGMS"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgnr("XGNR"),
        xgr("XGR"),
        clId("CL_ID"),
        clsj("CLSJ");

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