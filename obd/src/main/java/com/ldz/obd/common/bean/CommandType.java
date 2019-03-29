package com.ldz.obd.common.bean;

/**
 * 序号	命令码	含义
 1	0x30 0x88	上传行程报告
 */
public enum CommandType {
    GET_SET_CAR_TYPE("3003","3083"),
    TRAVEL_REPORT("","3088"),

	;

    CommandType(String code, String back) {
        this.code = code;
        this.back = back;
    }

    private String code;
    private String back;
    public static CommandType findByCode(String code) {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.getCode().equals(code)){
                return commandType;
            }
        }
        return null;
    }
    public static CommandType findByBack(String back) {
        for (CommandType commandType : CommandType.values()) {
            if (commandType.getBack().equals(back)){
                return commandType;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getBack() {
        return back;
    }
}