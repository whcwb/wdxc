package com.ldz.obd.handler;

import com.ldz.obd.handler.biz.BizBaseHandler;
import com.ldz.obd.handler.biz.BizHandler3088;

public enum HandlerMap {
    TRAVEL_REPORT("3088", BizHandler3088.class,"行程报告上传");

    private String code;
    private Class<? extends BizBaseHandler> handlerCls;
    private String desc;

    HandlerMap(String code, Class<? extends BizBaseHandler> handlerCls, String desc) {
        this.code = code;
        this.handlerCls = handlerCls;
        this.desc = desc;
    }
}
