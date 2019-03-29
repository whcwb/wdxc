package com.ldz.dao.biz.model;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "cl_yh")
public class ClYh implements Serializable {

    @Id
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 设备id
     */
    @Column(name = "client_id")
    private String clientId;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    private String jgdm;

    /**
     * 备注
     */
    private String remark;

    /**
     * 用户来源
     */
    private String source;

    /**
     * 用户头像
     */
    @Column(name = "h_img")
    private String hImg;

    /**
     * 性别
     */
    private String gender;

    /**
     * 出生日期
     */
    @Column(name = "birth_day")
    private String birthDay;

    /**
     * 地址
     */
    private String address;

    /**
     * e-mail
     */
    @Column(name = "e_mail")
    private String eMail;

    /**
     * 学历
     */
    @Column(name = "edu_record")
    private String eduRecoed;

    /**
     * 驾照照片
     */
    @Column(name = "d_img")
    private String dImg;

    /**
     * 驾驶证号码
     */
    @Column(name = "d_code")
    private String dCode;

    /**
     * 驾驶证档案编号
     */
    @Column(name = "d_no")
    private String dNo;

    /**
     * 驾驶证初领日期
     */
    @Column(name = "d_date")
    private String dDate;

    private static final long serialVersionUID = 1L;


    public String gethImg() {
        return hImg;
    }

    public void sethImg(String hImg) {
        this.hImg = hImg;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getEduRecoed() {
        return eduRecoed;
    }

    public void setEduRecoed(String eduRecoed) {
        this.eduRecoed = eduRecoed;
    }

    public String getdImg() {
        return dImg;
    }

    public void setdImg(String dImg) {
        this.dImg = dImg;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String getdNo() {
        return dNo;
    }

    public void setdNo(String dNo) {
        this.dNo = dNo;
    }

    public String getdDate() {
        return dDate;
    }

    public void setdDate(String dDate) {
        this.dDate = dDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取设备id
     *
     * @return client_id - 设备id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 设置设备id
     *
     * @param clientId 设备id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @return jgdm
     */
    public String getJgdm() {
        return jgdm;
    }

    /**
     * @param jgdm
     */
    public void setJgdm(String jgdm) {
        this.jgdm = jgdm;
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
        id("Id"),
        username("username"),
        password("password"),
        clientId("client_id"),
        nickName("nick_name"),
        jgdm("jgdm"),
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