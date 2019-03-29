package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClDd;

public interface DdService extends BaseService<ClDd,String>{
    ApiResponse<String> saveEntity(ClDd entity);
}
