package com.ldz.obd.bean;

import io.netty.channel.ChannelHandlerContext;

/**
 * 整体报文
 * Created by Administrator on 2018/4/11.
 */
public class PackageData {

    /**
     * 设备ID  6byte
     * 设备的 ID 号,固定为 6 字节长度(即 12 个 ASCII 字符，BCD 码表示，
     * 2 位表示产品编号，两位表示生产日期的年，2013 年即用 13 表示，
     * 4 位表示月日，还剩 4 位表示该日期生产的编号，从 0000-9999。
     * 当然该 ID 也可以根据客户要求设置成其他形式，比如直接采用
     * SIM 卡号，但是长度必须不超过 12 位，且必须都是数字)
     */
    protected String equipmentId;
    /**
     * 命令字 2byte
     * 一个字节命令类型(表一)，一个字节表示命令编号，具体参见协议
     */
    protected int orderCode;
    /**
     * 指令长度 2byte
     * 命令内容的长度
     */
    protected int bodyLength;

    // 消息体字节数组
    protected byte[] bodyBytes;

    /**
     * 校验码 1byte
     */
    protected int checkSum;

    /**
     * 校验结果 t or f
     */
    private Boolean checkType;
    private ChannelHandlerContext ctx;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public Boolean getCheckType() {
        return checkType;
    }

    public void setCheckType(Boolean checkType) {
        this.checkType = checkType;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    public void setBodyBytes(byte[] bodyBytes) {
        this.bodyBytes = bodyBytes;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }
}
