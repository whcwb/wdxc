package com.ldz.sys.service;

import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysMessage;

/**
 * Created by Administrator on 2018/5/10.
 */
public interface SysMessageService extends BaseService<SysMessage,String> {
    void add(SysMessage sysMessage);
}
