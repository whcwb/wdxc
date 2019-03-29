package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClClyxjl;

public interface ClyxjlService extends BaseService<ClClyxjl,String>{
    ApiResponse<String> saveEntity(ClClyxjl entity);
}
