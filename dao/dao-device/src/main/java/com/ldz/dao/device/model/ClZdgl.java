package com.ldz.dao.device.model;



import com.ldz.util.bean.GpsObdMessageBean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_ZDGL")
public class ClZdgl implements Serializable {
    /**
     * 终端编号
     */
    @Id
    @Column(name = "ZDBH")
    @NotNull(message="终端编号不能为空")
    private String zdbh;

    /**
     * 型号
     */
    @Column(name = "XH")
    @NotNull(message="型号不能为空")
    private String xh;

    /**
     * 名称
     */
    @Column(name = "MC")
    @NotNull(message="名称不能为空")
    private String mc;

    /**
     * 厂商
     */
    @Column(name = "CS")
    @NotNull(message="厂商不能为空")
    private String cs;

    /**
     * 状态
     * 00 正常
     * 10 停用
     */
    @Column(name = "ZT")
    @NotNull(message="状态不能为空")
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
     * 00点火
     * 10熄火
     * 20离线
     */
    @Column(name = "ZXZT")
    private String zxzt;

    /**
     * 在线时间
     */
    @Column(name = "ZXSJ")
    private Date zxsj;

    /**
     * 加速灵敏度
     */

    @Column(name = "JSLMD")
    private String jslmd;

    /**
     * 上传模式
     */
    @Column(name = "SCMS")
    private String scms;


    /**
     * 碰撞灵敏度
     */

    @Column(name = "PZLMD")
    private String pzlmd;

    /**
     * 设置上报地址
     */

    @Column(name = "CMD")
    private String cmd;


    /**
     * 上传视屏模式
     */

    @Column(name = "SPSCMS")
    private String spscms;
    /**
     * gps心跳
     */
    @Column(name = "GPSXT")
    private String gpsxt;


    /**
     * 是否将设备上传到鹰眼
     */
    @Column(name = "SFYY")
    private String sfyy;


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
     * 终端有效期开始时间
     */
    @Column(name = "ZD_KSSJ")
    private String zdKssj;
    /**
     * 终端有效期结束时间
     */
    @Column(name = "ZD_JSSJ")
    private String zdJssj;
    /**
     * 终端类型
     */
    @Column(name = "ZD_LX")
    private String zdLx;


    @Transient
    private short cssd;

    /**
     * 设备版本
     */
    @Column(name = "VERSION")
    private String version;

    /**
     * 服务年限
     */
    @Column(name = "FWNX")
    private String fwnx;

    /**
     * 最后上线时间
     * @return
     */
    @Column(name = "ZHSXSJ")
    private String zhsxsj;

    /**
     * 激活状态
     */
    @Column(name = "JHZT")
    private String jhzt;
    
    /**
     * 备注信息
     */
    @Column(name = "BZ")
    private String bz;


    @Transient
    private GpsObdMessageBean obdInfo;

    public String getJhzt() {
        return jhzt;
    }

    public void setJhzt(String jhzt) {
        this.jhzt = jhzt;
    }

    public GpsObdMessageBean getObdInfo() {
        return obdInfo;
    }

    public void setObdInfo(GpsObdMessageBean obdInfo) {
        this.obdInfo = obdInfo;
    }

    public String getZhsxsj() {
        return zhsxsj;
    }

    public void setZhsxsj(String zhsxsj) {
        this.zhsxsj = zhsxsj;
    }

    public String getFwnx() {
        return fwnx;
    }

    public void setFwnx(String fwnx) {
        this.fwnx = fwnx;
    }

    public String getZdKssj() {
        return zdKssj;
    }

    public void setZdKssj(String zdKssj) {
        this.zdKssj = zdKssj;
    }

    public String getZdJssj() {
        return zdJssj;
    }

    public void setZdJssj(String zdJssj) {
        this.zdJssj = zdJssj;
    }

    public String getZdLx() {
        return zdLx;
    }

    public void setZdLx(String zdLx) {
        this.zdLx = zdLx;
    }

    public String getJgdm() {
        return jgdm;
    }

    public String getJgmc() {
        return jgmc;
    }

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

    public String getSfyy() {
		return sfyy;
	}

	public void setSfyy(String sfyy) {
		this.sfyy = sfyy;
	}

	public String getGpsxt() {
		return gpsxt;
	}

	public void setGpsxt(String gpsxt) {
		this.gpsxt = gpsxt;
	}

	public String getSpscms() {
		return spscms;
	}

	public void setSpscms(String spscms) {
		this.spscms = spscms;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}


	/**
     * 车牌号码
     */
    @Transient
    private String cph;

    private static final long serialVersionUID = 1L;

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }

    public String getJslmd() {
		return jslmd;
	}

	public void setJslmd(String jslmd) {
		this.jslmd = jslmd;
	}

	public String getPzlmd() {
		return pzlmd;
	}

	public void setPzlmd(String pzlmd) {
		this.pzlmd = pzlmd;
	}

	public String getScms() {
		return scms;
	}

	public void setScms(String scms) {
		this.scms = scms;
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

    public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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

    /**
     * 获取超速速度值
     * @return
     */
    public short getCssd() {
        return cssd;
    }

    /**
     * 设置超速速度值
     * @param cssd
     */
    public void setCssd(short cssd) {
        this.cssd = cssd;
    }

    public enum InnerColumn {
        zdbh("ZDBH"),
        xh("XH"),
        mc("MC"),
        cs("CS"),
        zt("ZT"),
        cjr("CJR"),
        cjsj("CJSJ"),
        xgr("XGR"),
        jgdm("JGDM"),
        xgsj("XGSJ"),
        zxzt("ZXZT"),
        zxsj("ZXSJ"),
        jslmd("JSLMD"),
        scms("SCMS"),
        cssd("CSSD"),
        pzlmd("PZLMD"),
        zdKssj("ZD_KSSJ"),
        zdJssj("ZD_JSSJ"),
        zdLx("ZD_Lx"),
        zwnz("FWNX"),
        zhsxsj("ZHSXSJ"),
        jhzt("JHZT");

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
