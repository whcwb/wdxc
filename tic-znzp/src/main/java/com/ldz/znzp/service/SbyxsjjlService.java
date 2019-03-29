package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClSbyxsjjl;

public interface SbyxsjjlService extends BaseService<ClSbyxsjjl,String>{
    ApiResponse<String> saveEntity(ClSbyxsjjl entity);
}
