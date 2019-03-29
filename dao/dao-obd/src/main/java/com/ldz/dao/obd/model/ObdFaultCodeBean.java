package com.ldz.dao.obd.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 发动机故障码上传
 * Created by Administrator on 2018/4/12.
 */
@Table(name = "cl_obd_code")
@Getter
@Setter
@ToString
public class ObdFaultCodeBean implements Serializable {
    private static final long serialVersionUID = -8366929034511774130L;
    @Column(name = "device_id")
    private String deviceId;

    @Id
    private String id;

    /**
     * 故障码
     */
    @Column(name = "Fault_Code")
    private String faultCode;
    /**
     * 第 3 字节 01 表示已决，02 表示未决
     * 故障处理状态
     * 10 已解决 20未解决  00未知状态
     */
    @Column(name = "Fault_Type")
    private String faultType;
    /**
     * 创建时间  这里的时间是取的当前系统时间
     */
    @Column(name = "creation_Time")
    private Date creationTime;


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
