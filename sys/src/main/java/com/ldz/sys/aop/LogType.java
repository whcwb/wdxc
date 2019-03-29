package com.ldz.sys.aop;

public enum LogType {
    INSERT("1","新增"),
    UPDATE("2","更新"),
    REMOVE("3","删除"),
    LOGIN("4","登陆"),
    LOGOUT("5","注销"),
    MODIFY("7","修改"),
    ;

    private String code;
    private String desc;

    LogType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
