package com.ldz.util.bean;

import java.io.Serializable;

/**
 * 4.3.4 请求/上传 GPS+OBD 混合信息
 * Created by Administrator on 2018/4/11.
 */
public class GpsObdMessageBean implements Serializable {
    private static final long serialVersionUID = -8366929034561774130L;

    /**
     * 数据类型 1 0x00 或者 0x01,其中 0x00 表示盲区数据，0x01 表示实时数据
     * 00表示盲区 10 表示实时数据 20未知状态
     */
    private String dataType;
    /**
     * 行程ID 长度：2    该数据属于哪个
     */
    private String schedulingCode;
    /**
     * 创建日期 长度：3 日月年表示.年省略”20”。比如 2014 年 4 月 28 日为 0x28 0x04 0x14 BCD 码
     */
    private String creatorDate;
    /**
     * 创建时间 长度：3 时分秒表示,为格林威治时间。比如 9 点 0 分 4 秒为 0x09 0x00 0x04 BCD 码
     */
    private String creatortime;
    /**
     * 纬度  长度:4 实际纬度乘以 10000 的值,DDMM.MMMM 格式，比如 2233.4567     则上传 0x22 0x33 0x45 0x67
     */
    private String latitude;
    /**
     * 经度 4.5   实际经度乘以 10000 的值,DDDMM.MMMM 格式11334.5678 则上传 0x11 0x33 0x45 0x67 0x
     */
    private String longitude;

    /**
     * 位指示 GPS 是否定位,东西经及南北纬等.请参见
     */
    private String indication;
    /**
     * 速度 1 当前 GPS 速度,以 km/h 为单位
     */
    private String gpsTempo;
    /**
     * 方向 长度：1 当前方向,以度为单位,设备在上传对其进行了除 2 处理,所以,系统需 乘以 2,则还原为实际方向角度
     * 公式 *2
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
     * 里程 长度：4 当设备能读到原车里程的时候，该里程为仪表盘里程。否则为标定的 里程 + OBD 累加里程,单位 KM
     */
    private String mileage;

/**
 * 设备状态 长度：4 4 个字节，其中第一个字节暂时保留，第二个字节的定义如(表十)
 *  所示，第三个字节的定义如(表十一)所示，第四个字节(表十二)所示 暂时没有处理
 */


    /**
     * 负荷计算值。 长度：1  先进行10进制转换，然后使用公式：BYTE*100/255（165*100/255）=64（取整）
     */
    private String loadValue;
    /**
     * 冷却液温度  长度：1 BYTE – 40，单位℃
     */
    private String CoolantTemperature;
    /**
     * 发动机转速  长度：2 ((BYTE1*256)+BYTE2)/4，(如 0x30 0x08 则 0x30 表示 BYTE1，
     0x08 表示 BYTE2，以下类同，不再重复)，单位 RPM
     */
    private String engineSpeed;
    /**
     * OBD 车速 长度：1 单位 KM/H
     */
    private String obdSpeed;
    /**
     * 点火提前角 1 BYTE – 64，单位
     */
    private String ignitionAngle;
    /**
     * 进气歧管绝对压力 1 BYTE，单位 kpa
     */
    private String intakePressure;

    /**
     * 控制模块电压 1 BYTE/10，单位 V
     */
    private String controlVoltage;
    /**
     * 进气温度 1 BYTE-40，单位℃
     */
    private String intakeTemperature;
    /**
     * 空气流量 2 ((BYTE1*256)+BYTE2)/100，单位 g/s
     */
    private String intakeFlux;
    /**
     * 节气门相对位置 1 BYTE*100/255，单位%
     */
    private String jqmxdwz;
    /**
     * 长期燃油修正 1 (BYTE1-128)*100/128，单位%
     */
    private String chryxz;
    /**
     * 空燃比系数 2 ((BYTE1*256)+BYTE2)*0.0000305
     */
    private String klbxs;
    /**
     * 节气门绝对位置 1 BYTE*100/255，单位%
     */
    private String jqmxjwz;
    /**
     * 燃油压力 1 BYTE*3,单位 kpa
     */
    private String ryyl;
    /**
     * 瞬间油耗 L/H 2 ((BYTE1*256)+BYTE2)*0.1,单位 L/H
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

    public String getGpsTempo() {
        return gpsTempo;
    }

    public void setGpsTempo(String gpsTempo) {
        this.gpsTempo = gpsTempo;
    }

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
                ",速度[gpsTempo]='" + gpsTempo + '\'' +
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
