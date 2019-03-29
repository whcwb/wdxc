package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClZdgl;

public interface ZdglService extends BaseService<ClZdgl,String>{
    ApiResponse<String> saveEntity(ClZdgl entity);
}
