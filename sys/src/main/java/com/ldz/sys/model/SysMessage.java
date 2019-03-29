package com.ldz.sys.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "SYS_MESSAGE")
public class SysMessage implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 创建时间
     */
    @Column(name = "CREATION_TIME")
    private Date creationTime;

    /**
     * 短信正文
     */
    @Column(name = "MESSAGE")
    private String message;

    /**
     * 下发消息类型
     * 1、短信
     */
    @Column(name = "TYPE")
    private String type;
    /**
     * 标题
     */
    @Column(name = "TITLE")
    private String title;
    /**
     * 消息体状态
     * 0、未发送 1、已发送  2、发送失败·
     */
    @Column(name = "STATUS")
    private String status;
    /**
     * 发送次数
     */
    @Column(name = "SEND_COUNT")
    private Integer sendCount;
    /**
     * 发送时间
     */
    @Column(name = "SEND_TIME")
    private Date sendTime;
    /**
     * 接收方编号(短信是手机号、微信是open_id)
     */
    @Column(name = "SENDEE_CODE")
    private String sendeeCode;
    /**
     * 业务编号(操作业务) (短信保存模板ID)
     */
    @Column(name = "BIZ_ID")
    private String bizId;

    /**
     * 备注
     */
    @Column(name = "REMARK")
    private String remark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendeeCode() {
        return sendeeCode;
    }

    public void setSendeeCode(String sendeeCode) {
        this.sendeeCode = sendeeCode;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public enum InnerColumn {
        id("ID"),
        creationTime("CREATION_TIME"),
        message("MESSAGE"),
        type("TYPE"),
        title("TITLE"),
        status("STATUS"),
        sendCount("SEND_COUNT"),
        sendTime("SEND_TIME"),
        sendeeCode("SENDEE_CODE");
//        bizId("BIZ_ID");

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