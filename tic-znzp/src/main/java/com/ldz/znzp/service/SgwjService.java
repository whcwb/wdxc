package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClSgwj;

public interface SgwjService extends BaseService<ClSgwj,String>{
    ApiResponse<String> saveEntity(ClSgwj entity);
}
