package com.ldz.dao.biz.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "CL_XLZD")
public class ClXlzd implements Serializable {
    /**
     * ID
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 线路id
     */
    @Column(name = "XL_ID")
    private String xlId;

    /**
     * 站点id
     */
    @Column(name = "ZD_ID")
    private String zdId;

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
     * 序号
     */
    @Column(name = "XH")
    private Short xh;

    /**
     * 方向
     */
    @Column(name = "FX")
    private String fx;

    /**
     * 预计到站时间
     */
    @Column(name = "YJDZSJ")
    private Short yjdzsj;

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
     * 获取站点id
     *
     * @return ZD_ID - 站点id
     */
    public String getZdId() {
        return zdId;
    }

    /**
     * 设置站点id
     *
     * @param zdId 站点id
     */
    public void setZdId(String zdId) {
        this.zdId = zdId;
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
     * 获取序号
     *
     * @return XH - 序号
     */
    public Short getXh() {
        return xh;
    }

    /**
     * 设置序号
     *
     * @param xh 序号
     */
    public void setXh(Short xh) {
        this.xh = xh;
    }

    /**
     * 获取方向
     *
     * @return FX - 方向
     */
    public String getFx() {
        return fx;
    }

    /**
     * 设置方向
     *
     * @param fx 方向
     */
    public void setFx(String fx) {
        this.fx = fx;
    }

    /**
     * 获取预计到站时间
     *
     * @return YJDZSJ - 预计到站时间
     */
    public Short getYjdzsj() {
        return yjdzsj;
    }

    /**
     * 设置预计到站时间
     *
     * @param yjdzsj 预计到站时间
     */
    public void setYjdzsj(Short yjdzsj) {
        this.yjdzsj = yjdzsj;
    }

    public enum InnerColumn {
        id("ID"),
        xlId("XL_ID"),
        zdId("ZD_ID"),
        zt("ZT"),
        cjr("CJR"),
        cjsj("CJSJ"),
        xgr("XGR"),
        xgsj("XGSJ"),
        xh("XH"),
        fx("FX"),
        yjdzsj("YJDZSJ");

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