package com.ldz.sys.model;

import com.ldz.sys.util.OrgUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "SYS_PTJG")
public class SysJg implements Serializable {
    /**
     * 机构代码生成方式：
     * 一级机构只有3位（001，002，003），二级有6位（001001，001002，002001），以此类推
     */
    @Id
    @Column(name = "JGDM")
    private String jgdm;
    /**
     * 机构等级
     */
    @Column(name = "JGDJ")
    private Integer jgdj;

    @Column(name = "JGMC")
    private String jgmc;

    @Column(name = "JGLX")
    private String jglx;

    @Column(name = "ZT")
    private String zt;

    @Column(name = "CJSJ")
    private Date cjsj;

    @Column(name = "CJR")
    private String cjr;

    @Column(name = "XGR")
    private String xgr;

    @Column(name = "XGSJ")
    private Date xgsj;

    @Column(name = "GLY")
    private String gly;

    @Column(name = "GLYXM")
    private String glyxm;

    @Column(name = "FJGDM")
    private String fjgdm;

    @Column(name = "BZ")
    private String bz;

    @Column( name = "JGBM")
    private String jgbm;

    @Column(name = "JGSM")
    private String jgsm;

    /**
     * 机构允许接入数量
     */
    @Column(name = "ZDSL")
    private Integer zdsl;

    /**
     * 联系电话
     */
    @Column(name ="LXDH")
    private String lxdh;

    /**
     * 地址
     */
    @Column(name = "DZ")
    private String dz;

    /**
     * 接口授权状态  00 授权   10 停用
     */
    @Column(name = "JK_SQ")
    private String jkSq;

    /**
     * 接口Ip
     */
    @Column(name = "JK_IP")
    private String jkIp;

    /**
     * 授权码
     */
    @Column(name = "JK_CODE")
    private String jkCode;



    @Transient
    private List<SysJg> children;
    @Transient
    private String title;


    public String getJkSq() {
        return jkSq;
    }

    public void setJkSq(String jkSq) {
        this.jkSq = jkSq;
    }

    public String getJkIp() {
        return jkIp;
    }

    public void setJkIp(String jkIp) {
        this.jkIp = jkIp;
    }

    public String getJkCode() {
        return jkCode;
    }

    public void setJkCode(String jkCode) {
        this.jkCode = jkCode;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public Integer getZdsl() {
        return zdsl;
    }

    public void setZdsl(Integer zdsl) {
        this.zdsl = zdsl;
    }

    public String getJgbm() {
        return jgbm;
    }

    public void setJgbm(String jgbm) {
        this.jgbm = jgbm;
    }

    public String getJgsm() {
        return jgsm;
    }

    public void setJgsm(String jgsm) {
        this.jgsm = jgsm;
    }

    public String getTitle() {
        return jgmc;
    }

    public Integer getJgdj() {
        return jgdj;
    }

    public void setJgdj(Integer jgdj) {
        this.jgdj = jgdj;
    }

    public List<SysJg> getChildren() {
        return children == null ? new ArrayList<>() : children;
    }

    public void setChildren(List<SysJg> children) {
        this.children = children;
    }

    private static final long serialVersionUID = 1L;

    public Integer getLevel(){
        return OrgUtil.getLevel(jgdm);
    }

    /**
     * @return JGDM
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
     * @return JGMC
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
     * @return JGLX
     */
    public String getJglx() {
        return jglx;
    }

    /**
     * @param jglx
     */
    public void setJglx(String jglx) {
        this.jglx = jglx;
    }

    /**
     * @return ZT
     */
    public String getZt() {
        return zt;
    }

    /**
     * @param zt
     */
    public void setZt(String zt) {
        this.zt = zt;
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
     * @return XGR
     */
    public String getXgr() {
        return xgr;
    }

    /**
     * @param xgr
     */
    public void setXgr(String xgr) {
        this.xgr = xgr;
    }

    /**
     * @return XGSJ
     */
    public Date getXgsj() {
        return xgsj;
    }

    /**
     * @param xgsj
     */
    public void setXgsj(Date xgsj) {
        this.xgsj = xgsj;
    }

    /**
     * @return GLY
     */
    public String getGly() {
        return gly;
    }

    /**
     * @param gly
     */
    public void setGly(String gly) {
        this.gly = gly;
    }

    /**
     * @return GLYXM
     */
    public String getGlyxm() {
        return glyxm;
    }

    /**
     * @param glyxm
     */
    public void setGlyxm(String glyxm) {
        this.glyxm = glyxm;
    }

    /**
     * @return FJGDM
     */
    public String getFjgdm() {
        return fjgdm;
    }

    /**
     * @param fjgdm
     */
    public void setFjgdm(String fjgdm) {
        this.fjgdm = fjgdm;
    }

    /**
     * @return BZ
     */
    public String getBz() {
        return bz;
    }

    /**
     * @param bz
     */
    public void setBz(String bz) {
        this.bz = bz;
    }

    public enum InnerColumn {
        jgdm("JGDM"),
        jgdj("JGDJ"),
        jgmc("JGMC"),
        jglx("JGLX"),
        zt("ZT"),
        cjsj("CJSJ"),
        cjr("CJR"),
        xgr("XGR"),
        xgsj("XGSJ"),
        gly("GLY"),
        glyxm("GLYXM"),
        fjgdm("FJGDM"),
        bz("BZ"),
        jgbm("JGBM"),
        jgsm("JGSM"),
        zdsl("ZDSL");

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
