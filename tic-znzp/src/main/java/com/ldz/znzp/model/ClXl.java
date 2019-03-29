package com.ldz.znzp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "CL_XL")
public class ClXl implements Serializable {
    /**
     * id
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 线路名称
     */
    @Column(name = "XLMC")
    private String xlmc;

    /**
     * 线路编号
     */
    @Column(name = "XLBH")
    private String xlbh;
    
    /**
     * 长度
     */
    @Column(name = "CD")
    private Double cd;

    /**
     * 平均时间
     */
    @Column(name = "PJSJ")
    private Double pjsj;

    /**
     * 运行开始时间
     */
    @Column(name = "YXKSSJ")
    private String yxkssj;

    /**
     * 运行结束时间
     */
    @Column(name = "YXJSSJ")
    private String yxjssj;

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
     * 状态
     */
    @Column(name = "ZT")
    private String zt;

    /**
     * 备注
     */
    @Column(name = "BZ")
    private String bz;

    /**
     * 运行方式
     */
    @Column(name = "YXFS")
    private String yxfs;

    /**
     * 类型
     */
    @Column(name = "LX")
    private String lx;

	private static final long serialVersionUID = 1L;

    public Double getCd() {
		return cd;
	}

	public void setCd(Double cd) {
		this.cd = cd;
	}

	public Double getPjsj() {
		return pjsj;
	}

	public void setPjsj(Double pjsj) {
		this.pjsj = pjsj;
	}

	public String getYxkssj() {
		return yxkssj;
	}

	public void setYxkssj(String yxkssj) {
		this.yxkssj = yxkssj;
	}

	public String getYxjssj() {
		return yxjssj;
	}

	public void setYxjssj(String yxjssj) {
		this.yxjssj = yxjssj;
	}

	public String getCjr() {
		return cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}

	public Date getCjsj() {
		return cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	public String getXgr() {
		return xgr;
	}

	public void setXgr(String xgr) {
		this.xgr = xgr;
	}

	public Date getXgsj() {
		return xgsj;
	}

	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}

	public String getJgdm() {
		return jgdm;
	}

	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getYxfs() {
		return yxfs;
	}

	public void setYxfs(String yxfs) {
		this.yxfs = yxfs;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	/**
     * 获取id
     *
     * @return id - id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取线路名称
     *
     * @return XLMC - 线路名称
     */
    public String getXlmc() {
        return xlmc;
    }

    /**
     * 设置线路名称
     *
     * @param xlmc 线路名称
     */
    public void setXlmc(String xlmc) {
        this.xlmc = xlmc;
    }

    /**
     * 获取线路编号
     *
     * @return XLBH - 线路编号
     */
    public String getXlbh() {
        return xlbh;
    }

    /**
     * 设置线路编号
     *
     * @param xlbh 线路编号
     */
    public void setXlbh(String xlbh) {
        this.xlbh = xlbh;
    }



    public enum InnerColumn {
        id("ID"),
        xlmc("XLMC"),
        xlbh("XLBH"),
        cd("CD"),
        pjsj("PJSJ"),
        yxkssj("YXKSSJ"),
        yxjssj("YXJSSJ"),
        cjr("CJR"),
        cjsj("CJSJ"),
        xgr("XGR"),
        xgsj("XGSJ"),
        jgdm("JGDM"),
        jgmc("JGMC"),
        zt("ZT"),
        bz("BZ"),
        yxfs("YXFS"),
        lx("LX");

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