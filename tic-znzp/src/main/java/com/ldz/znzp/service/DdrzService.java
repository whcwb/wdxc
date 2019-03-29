package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClDdrz;

public interface DdrzService extends BaseService<ClDdrz,String>{
    ApiResponse<String> saveEntity(ClDdrz entity);
}
