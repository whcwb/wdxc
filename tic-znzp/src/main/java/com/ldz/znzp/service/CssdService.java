package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClCssd;

public interface CssdService extends BaseService<ClCssd,String>{
    ApiResponse<String> saveEntity(ClCssd entity);
}
