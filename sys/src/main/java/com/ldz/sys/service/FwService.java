package com.ldz.sys.service;

import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysFw;
import com.ldz.util.bean.ApiResponse;

import java.util.List;

public interface FwService extends BaseService<SysFw, String> {
    ApiResponse<String> saveEntity(SysFw entity);
    ApiResponse<String> updateEntity(SysFw entity);

    /**
     * 设置机构服务
     * @param orgCode 机构代码
     * @param serviceCodes 服务代码
     * @return 执行结果
     */
    ApiResponse<String> setOrgService(String orgCode,List<String> serviceCodes);

    /**
     * 查询机构拥有的功能列表
     * @param jgdm 机构代码
     * @return 功能列表
     */
    List<SysFw> findByJgdm(String jgdm);

    ApiResponse<String> removeIdsWithCheck(String[] ids);
}
