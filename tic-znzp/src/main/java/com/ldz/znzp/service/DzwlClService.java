package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClDzwlCl;

public interface DzwlClService extends BaseService<ClDzwlCl,String>{
    ApiResponse<String> saveEntity(ClDzwlCl entity);
}
