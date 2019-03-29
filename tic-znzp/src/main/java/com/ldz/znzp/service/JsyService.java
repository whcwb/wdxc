package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClJsy;

public interface JsyService extends BaseService<ClJsy,String>{
    ApiResponse<String> saveEntity(ClJsy entity);
}
