package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClGpsLs;

public interface GpsLsService extends BaseService<ClGpsLs,String>{
    ApiResponse<String> saveEntity(ClGpsLs entity);
}
