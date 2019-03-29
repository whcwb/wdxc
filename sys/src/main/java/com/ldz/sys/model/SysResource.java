package com.ldz.sys.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_resource")
public class SysResource implements Serializable {
    /**
     * 主键ID
     */
    @Id
    @Column(name = "res_id")
    @GeneratedValue(generator = "JDBC")
    private Long resId;

    /**
     * 资源名称
     */
    @Column(name = "res_name")
    private String resName;

    /**
     * 资源访问地址
     */
    @Column(name = "res_url")
    private String resUrl;

    /**
     * 资源icon
     */
    @Column(name = "res_icon")
    private String resIcon;

    /**
     * 资源排序号
     */
    @Column(name = "res_order")
    private Integer resOrder;

    /**
     * 资源对应API地址。可多个，多个之间使用';'分割
     */
    @Column(name = "api_url")
    private String apiUrl;

    /**
     * 资源层级
     */
    @Column(name = "res_level")
    private Integer resLevel;

    /**
     * 资源父ID
     */
    @Column(name = "res_pid")
    private Long resPid;

    /**
     * 类型。10：资源；20：内容
     */
    @Column(name = "res_type")
    private String resType;

    /**
     * 创建人
     */
    @Column(name = "create_user")
    private String createUser;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "update_user")
    private Long updateUser;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 路由访问前缀
     */
    @Column(name = "router_prefix")
    private String routerPrefix;

    /**
     * API前缀
     */
    @Column(name = "api_prefix")
    private String apiPrefix;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键ID
     *
     * @return res_id - 主键ID
     */
    public Long getResId() {
        return resId;
    }

    /**
     * 设置主键ID
     *
     * @param resId 主键ID
     */
    public void setResId(Long resId) {
        this.resId = resId;
    }

    /**
     * 获取资源名称
     *
     * @return res_name - 资源名称
     */
    public String getResName() {
        return resName;
    }

    /**
     * 设置资源名称
     *
     * @param resName 资源名称
     */
    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * 获取资源访问地址
     *
     * @return res_url - 资源访问地址
     */
    public String getResUrl() {
        return resUrl;
    }

    /**
     * 设置资源访问地址
     *
     * @param resUrl 资源访问地址
     */
    public void setResUrl(String resUrl) {
        this.resUrl = resUrl;
    }

    /**
     * 获取资源icon
     *
     * @return res_icon - 资源icon
     */
    public String getResIcon() {
        return resIcon;
    }

    /**
     * 设置资源icon
     *
     * @param resIcon 资源icon
     */
    public void setResIcon(String resIcon) {
        this.resIcon = resIcon;
    }

    /**
     * 获取资源排序号
     *
     * @return res_order - 资源排序号
     */
    public Integer getResOrder() {
        return resOrder;
    }

    /**
     * 设置资源排序号
     *
     * @param resOrder 资源排序号
     */
    public void setResOrder(Integer resOrder) {
        this.resOrder = resOrder;
    }

    /**
     * 获取资源对应API地址。可多个，多个之间使用';'分割
     *
     * @return api_url - 资源对应API地址。可多个，多个之间使用';'分割
     */
    public String getApiUrl() {
        return apiUrl;
    }

    /**
     * 设置资源对应API地址。可多个，多个之间使用';'分割
     *
     * @param apiUrl 资源对应API地址。可多个，多个之间使用';'分割
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * 获取资源层级
     *
     * @return res_level - 资源层级
     */
    public Integer getResLevel() {
        return resLevel;
    }

    /**
     * 设置资源层级
     *
     * @param resLevel 资源层级
     */
    public void setResLevel(Integer resLevel) {
        this.resLevel = resLevel;
    }

    /**
     * 获取资源父ID
     *
     * @return res_pid - 资源父ID
     */
    public Long getResPid() {
        return resPid;
    }

    /**
     * 设置资源父ID
     *
     * @param resPid 资源父ID
     */
    public void setResPid(Long resPid) {
        this.resPid = resPid;
    }

    /**
     * 获取类型。10：资源；20：内容
     *
     * @return res_type - 类型。10：资源；20：内容
     */
    public String getResType() {
        return resType;
    }

    /**
     * 设置类型。10：资源；20：内容
     *
     * @param resType 类型。10：资源；20：内容
     */
    public void setResType(String resType) {
        this.resType = resType;
    }

    /**
     * 获取创建人
     *
     * @return create_user - 创建人
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * 设置创建人
     *
     * @param createUser 创建人
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新人
     *
     * @return update_user - 更新人
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * 设置更新人
     *
     * @param updateUser 更新人
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取路由访问前缀
     *
     * @return router_prefix - 路由访问前缀
     */
    public String getRouterPrefix() {
        return routerPrefix;
    }

    /**
     * 设置路由访问前缀
     *
     * @param routerPrefix 路由访问前缀
     */
    public void setRouterPrefix(String routerPrefix) {
        this.routerPrefix = routerPrefix;
    }

    /**
     * 获取API前缀
     *
     * @return api_prefix - API前缀
     */
    public String getApiPrefix() {
        return apiPrefix;
    }

    /**
     * 设置API前缀
     *
     * @param apiPrefix API前缀
     */
    public void setApiPrefix(String apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    public enum InnerColumn {
        resId("res_id"),
        resName("res_name"),
        resUrl("res_url"),
        resIcon("res_icon"),
        resOrder("res_order"),
        apiUrl("api_url"),
        resLevel("res_level"),
        resPid("res_pid"),
        resType("res_type"),
        createUser("create_user"),
        createTime("create_time"),
        updateUser("update_user"),
        updateTime("update_time"),
        routerPrefix("router_prefix"),
        apiPrefix("api_prefix");

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