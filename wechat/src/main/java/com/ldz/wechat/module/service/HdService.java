package com.ldz.wechat.module.service;

import com.ldz.wechat.base.BaseService;
import com.ldz.wechat.module.model.SysHdyx;

import java.util.List;

/**
 * @author chenwei
 * @since 2018/2/26
 */
public interface HdService extends BaseService<SysHdyx,String> {

    List<SysHdyx> getAdsenseList();
}
