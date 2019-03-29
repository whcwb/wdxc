package com.ldz.dao.biz.bean;

import java.io.Serializable;

/**
 * 请求/上传 GPS+OBD 混合信息
 * Created by Administrator on 2018/4/11.
 */
public class ObdMessageBean implements Serializable {
    private static final long serialVersionUID = -8366929034561774130L;

    /**
     * 00表示盲区 10 表示实时数据 20未知状态
     */
    private String dataType;
    /**
     * 行程ID 长度：2    该数据属于哪个
     */
    private String schedulingCode;
    /**
     * 创建日期 比如 2014 年 4 月 28 日
     */
    private String creatorDate;
    /**
     * 创建时间 比如 9 点 0 分 4 秒
     */
    private String creatortime;
    /**
     * 纬度 比如 2233.4567
     */
    private String latitude;
    /**
     * 经度 格式11334.5678
     */
    private String longitude;

    /**
     * 位指示 GPS 是否定位,东西经及南北纬等.请参见
     */
    private String indication;
    /**
     * 方向
     */
    private String direction;
    /**
     * GPS 卫星个数 长度：1 当前 GPS 卫星的个数
     */
    private String gpsCount;
    /**
     * GSM 信号质量 长度：1 表明当前 GSM 的信号强弱,GSM 信号强度最大为 31
     */
    private String gpsSignalIntensity;
    /**
     * 里程 单位 KM
     */
    private String mileage;

/**
 * 设备状态 长度：4 4 个字节，其中第一个字节暂时保留，第二个字节的定义如(表十)
 *  所示，第三个字节的定义如(表十一)所示，第四个字节(表十二)所示 暂时没有处理
 */


    /**
     * 负荷计算值。
     */
    private String loadValue;
    /**
     * 冷却液温度  单位℃
     */
    private String CoolantTemperature;
    /**
     * 发动机转速  单位 RPM
     */
    private String engineSpeed;
    /**
     * OBD 车速 长度： KM/H
     */
    private String obdSpeed;
    /**
     * 点火提前角 单位
     */
    private String ignitionAngle;
    /**
     * 进气歧管绝对压力 单位 kpa
     */
    private String intakePressure;

    /**
     * 控制模块电压 单位 V
     */
    private String controlVoltage;
    /**
     * 进气温度 单位℃
     */
    private String intakeTemperature;
    /**
     * 空气流量 单位 g/s
     */
    private String intakeFlux;
    /**
     * 节气门相对位置 单位%
     */
    private String jqmxdwz;
    /**
     * 长期燃油修正  单位%
     */
    private String chryxz;
    /**
     * 空燃比系数
     */
    private String klbxs;
    /**
     * 节气门绝对位置 单位%
     */
    private String jqmxjwz;
    /**
     * 燃油压力单位 kpa
     */
    private String ryyl;
    /**
     * 瞬间油耗 单位 L/H
     */
    private String sjyh;
    /**
     * 剩余油量 2 if((BYTE1&0X80)==0X80)
     ((BYTE1-0X80)*256)+BYTE2)*0.1 单位%
     Else (BYTE1*256+BYTE2)*0.1 单位 L
     */
    private String syyl;
    /**
     *基站 8 4 字节运营商代码(IMSI 前 5 位),2 字节 LAC,2 字节 CELL
     */
    private String jz;
    /**
     *耗油量 4 OBD 累计耗油量,单位 L
     */
    private String hyl;
    /**
     * 流水号 1 从 0 -255 一直循环累加，可以用来检测是否有包丢失
     */
    private String seq;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSchedulingCode() {
        return schedulingCode;
    }

    public void setSchedulingCode(String schedulingCode) {
        this.schedulingCode = schedulingCode;
    }

    public String getCreatorDate() {
        return creatorDate;
    }

    public void setCreatorDate(String creatorDate) {
        this.creatorDate = creatorDate;
    }

    public String getCreatortime() {
        return creatortime;
    }

    public void setCreatortime(String creatortime) {
        this.creatortime = creatortime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getGpsCount() {
        return gpsCount;
    }

    public void setGpsCount(String gpsCount) {
        this.gpsCount = gpsCount;
    }

    public String getGpsSignalIntensity() {
        return gpsSignalIntensity;
    }

    public void setGpsSignalIntensity(String gpsSignalIntensity) {
        this.gpsSignalIntensity = gpsSignalIntensity;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getLoadValue() {
        return loadValue;
    }

    public void setLoadValue(String loadValue) {
        this.loadValue = loadValue;
    }

    public String getCoolantTemperature() {
        return CoolantTemperature;
    }

    public void setCoolantTemperature(String coolantTemperature) {
        CoolantTemperature = coolantTemperature;
    }

    public String getEngineSpeed() {
        return engineSpeed;
    }

    public void setEngineSpeed(String engineSpeed) {
        this.engineSpeed = engineSpeed;
    }

    public String getObdSpeed() {
        return obdSpeed;
    }

    public void setObdSpeed(String obdSpeed) {
        this.obdSpeed = obdSpeed;
    }

    public String getIgnitionAngle() {
        return ignitionAngle;
    }

    public void setIgnitionAngle(String ignitionAngle) {
        this.ignitionAngle = ignitionAngle;
    }

    public String getIntakePressure() {
        return intakePressure;
    }

    public void setIntakePressure(String intakePressure) {
        this.intakePressure = intakePressure;
    }

    public String getControlVoltage() {
        return controlVoltage;
    }

    public void setControlVoltage(String controlVoltage) {
        this.controlVoltage = controlVoltage;
    }

    public String getIntakeTemperature() {
        return intakeTemperature;
    }

    public void setIntakeTemperature(String intakeTemperature) {
        this.intakeTemperature = intakeTemperature;
    }

    public String getIntakeFlux() {
        return intakeFlux;
    }

    public void setIntakeFlux(String intakeFlux) {
        this.intakeFlux = intakeFlux;
    }

    public String getJqmxdwz() {
        return jqmxdwz;
    }

    public void setJqmxdwz(String jqmxdwz) {
        this.jqmxdwz = jqmxdwz;
    }

    public String getChryxz() {
        return chryxz;
    }

    public void setChryxz(String chryxz) {
        this.chryxz = chryxz;
    }

    public String getKlbxs() {
        return klbxs;
    }

    public void setKlbxs(String klbxs) {
        this.klbxs = klbxs;
    }

    public String getJqmxjwz() {
        return jqmxjwz;
    }

    public void setJqmxjwz(String jqmxjwz) {
        this.jqmxjwz = jqmxjwz;
    }

    public String getRyyl() {
        return ryyl;
    }

    public void setRyyl(String ryyl) {
        this.ryyl = ryyl;
    }

    public String getSjyh() {
        return sjyh;
    }

    public void setSjyh(String sjyh) {
        this.sjyh = sjyh;
    }

    public String getSyyl() {
        return syyl;
    }

    public void setSyyl(String syyl) {
        this.syyl = syyl;
    }

    public String getJz() {
        return jz;
    }

    public void setJz(String jz) {
        this.jz = jz;
    }

    public String getHyl() {
        return hyl;
    }

    public void setHyl(String hyl) {
        this.hyl = hyl;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "GpsObdMessageBean{" +
                "数据类型[dataType]='" + dataType + '\'' +
                ",行程ID[schedulingCode]='" + schedulingCode + '\'' +
                ", 创建日期[creatorDate]='" + creatorDate + '\'' +
                ", 创建时间[creatortime]='" + creatortime + '\'' +
                ",纬度[latitude]='" + latitude + '\'' +
                ", 经度[longitude]='" + longitude + '\'' +
                ",位指示[indication]='" + indication + '\'' +
                ", 方向[direction]='" + direction + '\'' +
                ", GPS卫星个数[gpsCount]='" + gpsCount + '\'' +
                ", GSM信号质量[gpsSignalIntensity]='" + gpsSignalIntensity + '\'' +
                ", 里程[mileage]='" + mileage + '\'' +
                ", 负荷计算值[loadValue]='" + loadValue + '\'' +
                ", 冷却液温度[CoolantTemperature]='" + CoolantTemperature + '\'' +
                ", 发动机转速[engineSpeed]='" + engineSpeed + '\'' +
                ", OBD 车速[obdSpeed]='" + obdSpeed + '\'' +
                ", 点火提前角[ignitionAngle]='" + ignitionAngle + '\'' +
                ",进气歧管绝对压力[intakePressure]='" + intakePressure + '\'' +
                ", 控制模块电压[controlVoltage]='" + controlVoltage + '\'' +
                ", 进气温度[intakeTemperature]='" + intakeTemperature + '\'' +
                ", 空气流量[intakeFlux]='" + intakeFlux + '\'' +
                ", 节气门相对位置[jqmxdwz]='" + jqmxdwz + '\'' +
                ", 长期燃油修正[chryxz]='" + chryxz + '\'' +
                ", 空燃比系数[klbxs]='" + klbxs + '\'' +
                ", 节气门绝对位置[jqmxjwz]='" + jqmxjwz + '\'' +
                ", 燃油压力[ryyl]='" + ryyl + '\'' +
                ", 瞬间油耗[sjyh]='" + sjyh + '\'' +
                ", 剩余油量[syyl]='" + syyl + '\'' +
                ", 基站[jz]='" + jz + '\'' +
                ", 耗油量[hyl]='" + hyl + '\'' +
                ", 流水号[seq]='" + seq + '\'' +
                '}';
    }
}
