package com.ldz.wechat.module.service;


import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.SysJg;


/**
 * auther chenwei
 * create at 2018/2/26
 */
public interface JgService extends BaseService<SysJg,String> {

    SysJg findByOrgCode(String orgCode);
}
