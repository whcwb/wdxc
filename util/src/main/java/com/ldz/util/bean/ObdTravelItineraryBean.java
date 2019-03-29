package com.ldz.util.bean;

import java.io.Serializable;

/**
 * 4.4.4 行程报告上传
 * Created by Administrator on 2018/4/12.
 */
public class ObdTravelItineraryBean implements Serializable {
    private static final long serialVersionUID = -8366929034561774132L;
    /**
     * 1、行程ID
     * 长度：2
     * 表示该报告属于哪个行程
     */
    private String schedulingCode;

    /**
     * 2、点火日期
     * 长度：3		日月年表示.年省略”20”，BCD 码表示
     */
    private String ignitionDate;

    /**
     * 3、点火时间		长度：3		时分秒表示,为格林威治时间。BCD 码表示
     */
    private String ignitionTime;

    /**
     * 4、熄火日期		长度：3
     * 同上(点火时间和熄火时间为 GPS 时间，下面的时间为设备定时器累加的时间，单位 S，每次点火熄火时间相减的时间和下面累加的时间可能有几 S 的误差)
     */
    private String flameoutDate;

    /**
     * 5、熄火时间		长度：3		同上
     */
    private String flameoutTime;

    /**
     * 6、该次行驶时间		长度：2		单位 S，该时间为设备定时器累加的时间，当 GPS 时间无效的时候，可用该时间显示行驶的时间
     */
    private String timeConsuming ;

    /**
     * 7、该次耗油量		长度：4		单位 ML
     */
    private String oilWear;

    /**
     * 8、该次里程		长度：4		单位 M
     */
    private String mileage;

    /**
     * 9、最高速度		长度：1		KM/H
     */
    private String maximumSpeed;

    /**
     * 10、发动机最高转速		长度：2
     * ((BYTE1*256)+BYTE2)/4,单位 rpm
     */
    private String maximumRev;

    /**
     * 11、冷却液最高温度		长度：1		BYTE - 40
     */
    private String maximumTemperature;

    /**
     * 12、急加速次数		长度：1		次
     */
    private String rapidAccelerationCount;

    /**
     * 13、急减速次数		长度：1		次
     */
    private String rapidDecelerationCount;

    /**
     * 14、超速行驶时间(>120km/h)		长度：2		单位 S
     */
    private String overspeedTime ;

    /**
     * 15、超速行驶的里程		长度：4		单位 M
     */
    private String overspeedMileage;

    /**
     * 16、超速行驶的耗油量		长度：4		单位 ML.
     */
    private String overspeedOilWear;

    /**
     * 17、高速行驶的时间(80km/h-120km/h)		长度：2		S
     */
    private String highspeedTime;

    /**
     * 18、高速行驶的里程		长度：4		M
     */
    private String highspeedMileage;

    /**
     * 19、高速行驶的耗油量		长度：4		单位 ML.
     */
    private String highspeedOilWear;

    /**
     * 20、中速行驶的时间(40km/h-80km/h)		长度：2		S
     */
    private String intermediateTime;

    /**
     * 21、中速行驶的里程		长度：4		M
     */
    private String intermediateMileage;

    /**
     * 22、中速行驶的耗油量		长度：4		单位 ML.
     */
    private String intermediateOilWear;

    /**
     * 23、低速行驶的时间(1km/h-40km/h)		长度：2		S
     */
    private String lowspeedTime;

    /**
     * 24、低速行驶的里程		长度：4		M
     */
    private String lowspeedMileage;

    /**
     * 25、低速行驶的耗油量		长度：4		单位 ML.
     */
    private String lowspeedOilWear;

    /**
     * 26、怠速的时间		长度：2		S
     */
    private String idlingTime;

    /**
     * 27、怠速的耗油量		长度：4		ML
     */
    private String idlingOilWear;

    /**
     * 28、急转弯次数		长度：1		次
     */
    private String jzwCount;

    @Override
    public String toString() {
        return "ObdTravelItineraryBean{" +
                    "行程ID[schedulingCode]='" +schedulingCode+ '\'' +
                    ",点火日期[ignitionDate]='" +ignitionDate+ '\'' +
                    ",点火时间[ignitionTime]='" +ignitionTime+ '\'' +
                    ",熄火日期[flameoutDate]='" +flameoutDate+ '\'' +
                    ",熄火时间[flameoutTime]='" +flameoutTime+ '\'' +
                    ",该次行驶时间[timeConsuming ]='" +timeConsuming + '\'' +
                    ",该次耗油量[oilWear]='" +oilWear+ '\'' +
                    ",该次里程[mileage]='" +mileage+ '\'' +
                    ",最高速度[maximumSpeed]='" +maximumSpeed+ '\'' +
                    ",发动机最高转速[maximumRev]='" +maximumRev+ '\'' +
                    ",冷却液最高温度[maximumTemperature]='" +maximumTemperature+ '\'' +
                    ",急加速次数[rapidAccelerationCount]='" +rapidAccelerationCount+ '\'' +
                    ",急减速次数[rapidDecelerationCount]='" +rapidDecelerationCount+ '\'' +
                    ",超速行驶时间(>120km/h)[overspeedTime ]='" +overspeedTime + '\'' +
                    ",超速行驶的里程[overspeedMileage]='" +overspeedMileage+ '\'' +
                    ",超速行驶的耗油量[overspeedOilWear]='" +overspeedOilWear+ '\'' +
                    ",高速行驶的时间(80km/h-120km/h)[highspeedTime]='" +highspeedTime+ '\'' +
                    ",高速行驶的里程[highspeedMileage]='" +highspeedMileage+ '\'' +
                    ",高速行驶的耗油量[highspeedOilWear]='" +highspeedOilWear+ '\'' +
                    ",中速行驶的时间(40km/h-80km/h)[intermediateTime]='" +intermediateTime+ '\'' +
                    ",中速行驶的里程[intermediateMileage]='" +intermediateMileage+ '\'' +
                    ",中速行驶的耗油量[intermediateOilWear]='" +intermediateOilWear+ '\'' +
                    ",低速行驶的时间(1km/h-40km/h)[lowspeedTime]='" +lowspeedTime+ '\'' +
                    ",低速行驶的里程[lowspeedMileage]='" +lowspeedMileage+ '\'' +
                    ",低速行驶的耗油量[lowspeedOilWear]='" +lowspeedOilWear+ '\'' +
                    ",怠速的时间[idlingTime]='" +idlingTime+ '\'' +
                    ",怠速的耗油量[idlingOilWear]='" +idlingOilWear+ '\'' +
                    ",急转弯次数[jzwCount]='" +jzwCount+ '\'' +
                '}';
    }

    public String getSchedulingCode() {
        return schedulingCode;
    }

    public void setSchedulingCode(String schedulingCode) {
        this.schedulingCode = schedulingCode;
    }

    public String getIgnitionDate() {
        return ignitionDate;
    }

    public void setIgnitionDate(String ignitionDate) {
        this.ignitionDate = ignitionDate;
    }

    public String getIgnitionTime() {
        return ignitionTime;
    }

    public void setIgnitionTime(String ignitionTime) {
        this.ignitionTime = ignitionTime;
    }

    public String getFlameoutDate() {
        return flameoutDate;
    }

    public void setFlameoutDate(String flameoutDate) {
        this.flameoutDate = flameoutDate;
    }

    public String getFlameoutTime() {
        return flameoutTime;
    }

    public void setFlameoutTime(String flameoutTime) {
        this.flameoutTime = flameoutTime;
    }

    public String getTimeConsuming() {
        return timeConsuming;
    }

    public void setTimeConsuming(String timeConsuming) {
        this.timeConsuming = timeConsuming;
    }

    public String getOilWear() {
        return oilWear;
    }

    public void setOilWear(String oilWear) {
        this.oilWear = oilWear;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getMaximumSpeed() {
        return maximumSpeed;
    }

    public void setMaximumSpeed(String maximumSpeed) {
        this.maximumSpeed = maximumSpeed;
    }

    public String getMaximumRev() {
        return maximumRev;
    }

    public void setMaximumRev(String maximumRev) {
        this.maximumRev = maximumRev;
    }

    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(String maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public String getRapidAccelerationCount() {
        return rapidAccelerationCount;
    }

    public void setRapidAccelerationCount(String rapidAccelerationCount) {
        this.rapidAccelerationCount = rapidAccelerationCount;
    }

    public String getRapidDecelerationCount() {
        return rapidDecelerationCount;
    }

    public void setRapidDecelerationCount(String rapidDecelerationCount) {
        this.rapidDecelerationCount = rapidDecelerationCount;
    }

    public String getOverspeedTime() {
        return overspeedTime;
    }

    public void setOverspeedTime(String overspeedTime) {
        this.overspeedTime = overspeedTime;
    }

    public String getOverspeedMileage() {
        return overspeedMileage;
    }

    public void setOverspeedMileage(String overspeedMileage) {
        this.overspeedMileage = overspeedMileage;
    }

    public String getOverspeedOilWear() {
        return overspeedOilWear;
    }

    public void setOverspeedOilWear(String overspeedOilWear) {
        this.overspeedOilWear = overspeedOilWear;
    }

    public String getHighspeedTime() {
        return highspeedTime;
    }

    public void setHighspeedTime(String highspeedTime) {
        this.highspeedTime = highspeedTime;
    }

    public String getHighspeedMileage() {
        return highspeedMileage;
    }

    public void setHighspeedMileage(String highspeedMileage) {
        this.highspeedMileage = highspeedMileage;
    }

    public String getHighspeedOilWear() {
        return highspeedOilWear;
    }

    public void setHighspeedOilWear(String highspeedOilWear) {
        this.highspeedOilWear = highspeedOilWear;
    }

    public String getIntermediateTime() {
        return intermediateTime;
    }

    public void setIntermediateTime(String intermediateTime) {
        this.intermediateTime = intermediateTime;
    }

    public String getIntermediateMileage() {
        return intermediateMileage;
    }

    public void setIntermediateMileage(String intermediateMileage) {
        this.intermediateMileage = intermediateMileage;
    }

    public String getIntermediateOilWear() {
        return intermediateOilWear;
    }

    public void setIntermediateOilWear(String intermediateOilWear) {
        this.intermediateOilWear = intermediateOilWear;
    }

    public String getLowspeedTime() {
        return lowspeedTime;
    }

    public void setLowspeedTime(String lowspeedTime) {
        this.lowspeedTime = lowspeedTime;
    }

    public String getLowspeedMileage() {
        return lowspeedMileage;
    }

    public void setLowspeedMileage(String lowspeedMileage) {
        this.lowspeedMileage = lowspeedMileage;
    }

    public String getLowspeedOilWear() {
        return lowspeedOilWear;
    }

    public void setLowspeedOilWear(String lowspeedOilWear) {
        this.lowspeedOilWear = lowspeedOilWear;
    }

    public String getIdlingTime() {
        return idlingTime;
    }

    public void setIdlingTime(String idlingTime) {
        this.idlingTime = idlingTime;
    }

    public String getIdlingOilWear() {
        return idlingOilWear;
    }

    public void setIdlingOilWear(String idlingOilWear) {
        this.idlingOilWear = idlingOilWear;
    }

    public String getJzwCount() {
        return jzwCount;
    }

    public void setJzwCount(String jzwCount) {
        this.jzwCount = jzwCount;
    }
}
