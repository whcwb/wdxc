package com.ldz.wechat.module.service;

import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.SysZdxm;

import java.util.List;

/**
 * auther chenwei
 * create at 2018/2/27
 */
public interface ZdxmService extends BaseService<SysZdxm,String> {
    List<SysZdxm> findByTypeCode(String typeCode);
}
