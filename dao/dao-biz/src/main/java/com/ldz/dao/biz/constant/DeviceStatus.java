package com.ldz.dao.biz.constant;

public enum DeviceStatus {
    IGNITION("00","正常运行"),
    FLAMEOUT("10","熄火"),
    OFFLINE("20","离线"),
    ;

    private String code;
    private String desc;

    DeviceStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public boolean equals(String code){
        return this.code.equals(code);
    }

    public static DeviceStatus toEmun(String code){
        for (DeviceStatus value : DeviceStatus.values()) {
            if (value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
