package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClPb;

public interface PbService extends BaseService<ClPb,String>{
    ApiResponse<String> saveEntity(ClPb entity);
}
