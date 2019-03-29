package com.ldz.dao.dwq.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Table(name = "cl_travel_data")
public class TravelData implements Serializable{
    @Id
    private Integer id;
    @Column(name = "start_time")
    private String startTime;
    @Column(name = "end_time")
    private String endTime;

    private int milage;
    @Column(name = "start_lng")
    private String startLng;
    @Column(name = "start_lat")
    private String startLat;
    @Column(name = "end_lat")
    private String endLat;
    @Column(name = "end_lng")
    private String endLng;
    @Column(name = "device_id")
    private String deviceId;

    public TravelData() {
    }
}
