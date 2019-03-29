package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClXlcl;

public interface XlclService extends BaseService<ClXlcl,String>{
    ApiResponse<String> saveEntity(ClXlcl entity);
}
