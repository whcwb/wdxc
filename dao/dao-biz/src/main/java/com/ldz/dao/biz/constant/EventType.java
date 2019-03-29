package com.ldz.dao.biz.constant;

public enum EventType {
    SHARP_REVVING("10","急加速"),
    SHARP_BRAKING("20","急刹车"),
    SHARP_TURN("30","急转弯"),
    OVERSPEED("40","超速"),
    IGNITION("50","点火"),
    FLAMEOUT("60","熄火"),
    OUT_OF_FANCE("70","不在电子围栏范围"),
    OFFLINE("80","离线"),
    ;

    private String code;
    private String desc;

    EventType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public boolean equals(String code){
        return this.code.equals(code);
    }

    public static EventType toEmun(String code){
        for (EventType value : EventType.values()) {
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
