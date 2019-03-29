package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClDzwl;

public interface DzwlService extends BaseService<ClDzwl,String>{
    ApiResponse<String> saveEntity(ClDzwl entity);
}
