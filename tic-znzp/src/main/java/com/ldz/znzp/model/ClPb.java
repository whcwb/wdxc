package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "CL_PB")
public class ClPb implements Serializable {
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
     * 线路id
     */
    @Column(name = "XL_ID")
    private String xlId;

    /**
     * 排班时间
     */
    @Column(name = "PBSJ")
    private Date pbsj;

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
    @Column(name = "XGJ")
    private Date xgj;

    /**
     * 修改人
     */
    @Column(name = "XGR")
    private String xgr;

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
     * 获取线路id
     *
     * @return XL_ID - 线路id
     */
    public String getXlId() {
        return xlId;
    }

    /**
     * 设置线路id
     *
     * @param xlId 线路id
     */
    public void setXlId(String xlId) {
        this.xlId = xlId;
    }

    /**
     * 获取排班时间
     *
     * @return PBSJ - 排班时间
     */
    public Date getPbsj() {
        return pbsj;
    }

    /**
     * 设置排班时间
     *
     * @param pbsj 排班时间
     */
    public void setPbsj(Date pbsj) {
        this.pbsj = pbsj;
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
     * @return XGJ - 修改时间
     */
    public Date getXgj() {
        return xgj;
    }

    /**
     * 设置修改时间
     *
     * @param xgj 修改时间
     */
    public void setXgj(Date xgj) {
        this.xgj = xgj;
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

    public enum InnerColumn {
        id("ID"),
        cph("CPH"),
        xlId("XL_ID"),
        pbsj("PBSJ"),
        sj("SJ"),
        sjxm("SJXM"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        clId("CL_ID"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgj("XGJ"),
        xgr("XGR");

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