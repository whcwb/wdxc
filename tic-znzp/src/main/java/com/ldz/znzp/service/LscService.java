package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClLsc;

public interface LscService extends BaseService<ClLsc,String>{
    ApiResponse<String> saveEntity(ClLsc entity);
}
