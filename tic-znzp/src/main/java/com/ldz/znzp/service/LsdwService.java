package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClLsdw;

public interface LsdwService extends BaseService<ClLsdw,String>{
    ApiResponse<String> saveEntity(ClLsdw entity);
}
