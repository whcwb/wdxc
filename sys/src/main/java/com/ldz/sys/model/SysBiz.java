package com.ldz.sys.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "sys_biz")
public class SysBiz implements Serializable {
    /**
     * 编码ID
     */
    @Id
    @Column(name = "biz_id")
    private Long bizId;

    /**
     * 服务名称
     */
    @Column(name = "biz_name")
    private String bizName;

    /**
     * 服务编号
     */
    @Column(name = "biz_no")
    private String bizNo;

    /**
     * 服务描述
     */
    @Column(name = "biz_describe")
    private String bizDescribe;

    /**
     * 排序号
     */
    @Column(name = "biz_order")
    private Integer bizOrder;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_user")
    private Long updateUser;

    @Column(name = "update_time")
    private Date updateTime;

    @Transient
    private List<SysResource> resourceList;

    public List<SysResource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<SysResource> resourceList) {
        this.resourceList = resourceList;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 获取编码ID
     *
     * @return biz_id - 编码ID
     */
    public Long getBizId() {
        return bizId;
    }

    /**
     * 设置编码ID
     *
     * @param bizId 编码ID
     */
    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    /**
     * 获取服务名称
     *
     * @return biz_name - 服务名称
     */
    public String getBizName() {
        return bizName;
    }

    /**
     * 设置服务名称
     *
     * @param bizName 服务名称
     */
    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    /**
     * 获取服务编号
     *
     * @return biz_no - 服务编号
     */
    public String getBizNo() {
        return bizNo;
    }

    /**
     * 设置服务编号
     *
     * @param bizNo 服务编号
     */
    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    /**
     * 获取服务描述
     *
     * @return biz_describe - 服务描述
     */
    public String getBizDescribe() {
        return bizDescribe;
    }

    /**
     * 设置服务描述
     *
     * @param bizDescribe 服务描述
     */
    public void setBizDescribe(String bizDescribe) {
        this.bizDescribe = bizDescribe;
    }

    /**
     * 获取排序号
     *
     * @return biz_order - 排序号
     */
    public Integer getBizOrder() {
        return bizOrder;
    }

    /**
     * 设置排序号
     *
     * @param bizOrder 排序号
     */
    public void setBizOrder(Integer bizOrder) {
        this.bizOrder = bizOrder;
    }

    /**
     * @return create_user
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_user
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public enum InnerColumn {
        bizId("biz_id"),
        bizName("biz_name"),
        bizNo("biz_no"),
        bizDescribe("biz_describe"),
        bizOrder("biz_order"),
        createUser("create_user"),
        createTime("create_time"),
        updateUser("update_user"),
        updateTime("update_time");

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