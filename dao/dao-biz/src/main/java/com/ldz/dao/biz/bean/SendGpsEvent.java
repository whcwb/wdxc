package com.ldz.dao.biz.bean;


public class SendGpsEvent {

    private GpsInfo gpsInfo;

    public SendGpsEvent(GpsInfo gpsInfo) {
        this.gpsInfo = gpsInfo;
    }

    public GpsInfo getGpsInfo() {
        return gpsInfo;
    }

    public void setGpsInfo(GpsInfo gpsInfo) {
        this.gpsInfo = gpsInfo;
    }
}
