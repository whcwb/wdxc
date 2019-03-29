package com.ldz.sys.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_log")
public class SysLog implements Serializable {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 操作人
     */
    @Column(name = "operate_user")
    private String operateUser;

    /**
     * 操作时间
     */
    @Column(name = "operate_time")
    private Date operateTime;

    /**
     * 对象id
     */
    @Column(name = "object_id")
    private String objectId;

    /**
     * 对象类型
     */
    private String method;

    /**
     * 操作前
     */
    @Column(name = "before_operate")
    private String beforeOperate;

    /**
     * 参数
     */
    private String params;

    /**
     * 操作后
     */
    @Column(name = "after_operate")
    private String afterOperate;

    /**
     * 花费时间
     */
    @Column(name = "elapse_time")
    private Integer elapseTime;

    /**
     * 备注
     */
    private String remark;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取操作人
     *
     * @return operate_user - 操作人
     */
    public String getOperateUser() {
        return operateUser;
    }

    /**
     * 设置操作人
     *
     * @param operateUser 操作人
     */
    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }

    /**
     * 获取操作时间
     *
     * @return operate_time - 操作时间
     */
    public Date getOperateTime() {
        return operateTime;
    }

    /**
     * 设置操作时间
     *
     * @param operateTime 操作时间
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 获取对象id
     *
     * @return object_id - 对象id
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * 设置对象id
     *
     * @param objectId 对象id
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * 获取对象类型
     *
     * @return method - 对象类型
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置对象类型
     *
     * @param method 对象类型
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取操作前
     *
     * @return before - 操作前
     */
    public String getBeforeOperate() {
        return beforeOperate;
    }

    /**
     * 设置操作前
     *
     * @param beforeOperate 操作前
     */
    public void setBeforeOperate(String beforeOperate) {
        this.beforeOperate = beforeOperate;
    }

    /**
     * 获取参数
     *
     * @return params - 参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置参数
     *
     * @param params 参数
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 获取操作后
     *
     * @return afterOperate - 操作后
     */
    public String getAfterOperate() {
        return afterOperate;
    }

    /**
     * 设置操作后
     *
     * @param afterOperate 操作后
     */
    public void setAfterOperate(String afterOperate) {
        this.afterOperate = afterOperate;
    }

    /**
     * 获取花费时间
     *
     * @return elapseTime - 花费时间
     */
    public Integer getElapseTime() {
        return elapseTime;
    }

    /**
     * 设置花费时间
     *
     * @param elapseTime 花费时间
     */
    public void setElapseTime(Integer elapseTime) {
        this.elapseTime = elapseTime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public enum InnerColumn {
        id("id"),
        operateUser("operate_user"),
        operateTime("operate_time"),
        objectId("object_id"),
        method("method"),
        beforeOperate("before_operate"),
        params("params"),
        afterOperate("after_operate"),
        elapseTime("elapse_time"),
        remark("remark");

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