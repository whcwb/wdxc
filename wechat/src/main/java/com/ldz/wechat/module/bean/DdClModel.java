package com.ldz.wechat.module.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * 校巴监控-校巴定位信息
 * Created by Administrator on 2018/3/29.
 */
public class DdClModel {

    private String zdId;//站点ID
    private String zdName;//站点名称
    private long vehicleCount;//该站点下车辆的数量
    private long vehicleScope;//站点范围
    private BigDecimal jd;//站点经度
    private BigDecimal wd;//站点纬度
    private long entryCount=0;//到站车辆

    private long exportCount=0;//出站车辆
    private List<ClClyxjlModel> vehicleList;//当前站点的车辆节点

    public BigDecimal getJd() {
        return jd;
    }

    public void setJd(BigDecimal jd) {
        this.jd = jd;
    }

    public BigDecimal getWd() {
        return wd;
    }

    public void setWd(BigDecimal wd) {
        this.wd = wd;
    }

    public long getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(long entryCount) {
        this.entryCount = entryCount;
    }

    public long getExportCount() {
        return exportCount;
    }

    public void setExportCount(long exportCount) {
        this.exportCount = exportCount;
    }

    public String getZdId() {
        return zdId;
    }

    public void setZdId(String zdId) {
        this.zdId = zdId;
    }

    public String getZdName() {
        return zdName;
    }

    public void setZdName(String zdName) {
        this.zdName = zdName;
    }

    public long getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(long vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public List<ClClyxjlModel> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<ClClyxjlModel> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public long getVehicleScope() {
        return vehicleScope;
    }

    public void setVehicleScope(long vehicleScope) {
        this.vehicleScope = vehicleScope;
    }
}
