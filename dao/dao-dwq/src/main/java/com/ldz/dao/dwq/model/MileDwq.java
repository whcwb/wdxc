package com.ldz.dao.dwq.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "Cl_Mile_dwq")
public class MileDwq {

    @Id
    private String id;

    private String imei;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    private String mileage;

    @Column(name = "device_id")
    private String deviceId;


}
