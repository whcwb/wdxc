package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClSpk;

public interface SpkService extends BaseService<ClSpk,String>{
    ApiResponse<String> saveEntity(ClSpk entity);
}
