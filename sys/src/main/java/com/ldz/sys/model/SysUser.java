package com.ldz.sys.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "sys_user")
public class SysUser implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 登陆名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 密码
     */
    @Column(name = "user_pwd")
    @JsonIgnore
    private String userPwd;

    /**
     * 用户类型。00:管理员；01:库管员；
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 联系电话
     */
    @Column(name = "user_phone")
    private String userPhone;

    /**
     * 姓名
     */
    @Column(name = "user_name")
    private String userName;
    /**
     * 头像
     */
    @Column(name = "head_img")
    private String headImg;

    /**
     * 用户状态。00:禁用；01:正常
     */
    @Column(name = "user_status")
    private String userStatus;
    @Column(name = "create_date")
    private Date createDate;

    @Transient
    private Integer deviceCount = 0;


    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    private static final long serialVersionUID = 1L;


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取主键
     *
     * @return user_id - 主键
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置主键
     *
     * @param userId 主键
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取登陆名
     *
     * @return login_name - 登陆名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置登陆名
     *
     * @param loginName 登陆名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取密码
     *
     * @return user_pwd - 密码
     */
    public String getUserPwd() {
        return userPwd;
    }

    /**
     * 设置密码
     *
     * @param userPwd 密码
     */
    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    /**
     * 获取用户类型。00:管理员；01:库管员；
     *
     * @return user_type - 用户类型。00:管理员；01:库管员；
     */
    public String getUserType() {
        return userType;
    }

    /**
     * 设置用户类型。00:管理员；01:库管员；
     *
     * @param userType 用户类型。00:管理员；01:库管员；
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * 获取联系电话
     *
     * @return user_phone - 联系电话
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * 设置联系电话
     *
     * @param userPhone 联系电话
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * 获取姓名
     *
     * @return user_name - 姓名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置姓名
     *
     * @param userName 姓名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取用户状态。00:禁用；01:正常
     *
     * @return user_status - 用户状态。00:禁用；01:正常
     */
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * 设置用户状态。00:禁用；01:正常
     *
     * @param userStatus 用户状态。00:禁用；01:正常
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public enum InnerColumn {
        userId("user_id"),
        loginName("login_name"),
        userPwd("user_pwd"),
        userType("user_type"),
        userPhone("user_phone"),
        userName("user_name"),
        userStatus("user_status"),
        createDate("create_date");

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