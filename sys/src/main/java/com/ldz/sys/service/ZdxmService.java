package com.ldz.sys.service;

import com.ldz.sys.base.BaseService;
import com.ldz.sys.model.SysZdxm;
import com.ldz.util.bean.ApiResponse;

import java.util.List;

/**
 * auther chenwei
 * create at 2018/2/27
 */
public interface ZdxmService extends BaseService<SysZdxm,String>{
    List<SysZdxm> findByTypeCode(String typeCode);
    List<SysZdxm> findByZdlms(List<String> zdlms);
    ApiResponse<String> add(SysZdxm zdxm);
}
