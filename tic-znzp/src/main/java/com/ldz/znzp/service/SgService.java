package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClSg;

public interface SgService extends BaseService<ClSg,String>{
    ApiResponse<String> saveEntity(ClSg entity);
}
