package com.ldz.wechat.module.service;

import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.SysYh;


public interface YhService extends BaseService<SysYh, String> {

	SysYh selectByKey(String userId);
}