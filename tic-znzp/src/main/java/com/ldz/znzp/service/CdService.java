package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClCd;

public interface CdService extends BaseService<ClCd,String>{
    ApiResponse<String> saveEntity(ClCd entity);
}
