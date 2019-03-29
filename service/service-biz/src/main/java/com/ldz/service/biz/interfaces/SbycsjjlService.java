package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClSbycsjjl;

public interface SbycsjjlService extends BaseService<ClSbycsjjl,String>{
    ApiResponse<String> saveEntity(ClSbycsjjl entity);
}
