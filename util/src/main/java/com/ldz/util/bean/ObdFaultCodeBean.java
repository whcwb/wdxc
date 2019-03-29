package com.ldz.util.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 发动机故障码上传
 * Created by Administrator on 2018/4/12.
 */
public class ObdFaultCodeBean implements Serializable {
    private static final long serialVersionUID = -8366929034511774130L;


    private String id;
    /**
     * 故障码
     */
    private String faultCode;
    /**
     * 第 3 字节 01 表示已决，02 表示未决
     * 故障处理状态
     * 10 已解决 20未解决  00未知状态
     */
    private String faultType;
    /**
     * 创建时间  这里的时间是取的当前系统时间
     */
    private Date creationTime;

    @Override
    public String toString() {
        return "ObdFaultCodeBean{" +
                "faultCode='" + faultCode + '\'' +
                ", faultType='" + faultType + '\'' +
                ", creationTime=" + creationTime +
                '}';
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
