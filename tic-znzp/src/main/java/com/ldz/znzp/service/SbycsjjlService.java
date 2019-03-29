package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClSbycsjjl;

public interface SbycsjjlService extends BaseService<ClSbycsjjl,String>{
    ApiResponse<String> saveEntity(ClSbycsjjl entity);
}
