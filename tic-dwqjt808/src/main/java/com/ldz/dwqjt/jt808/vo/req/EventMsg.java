package com.ldz.dwqjt.jt808.vo.req;

import com.ldz.dwqjt.jt808.vo.PackageData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMsg extends PackageData {

    private int eventType;

    public EventMsg() {
    }

    public EventMsg(PackageData packageData) {
        this();
        this.channel = packageData.getChannel();
        this.checkSum = packageData.getCheckSum();
        this.msgBodyBytes = packageData.getMsgBodyBytes();
        this.msgHeader = packageData.getMsgHeader();

    }
}
