package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClXlzd;

public interface XlzdService extends BaseService<ClXlzd,String>{
    ApiResponse<String> saveEntity(ClXlzd entity);
}
