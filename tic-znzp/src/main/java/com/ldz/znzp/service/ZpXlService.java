package com.ldz.znzp.service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.base.BaseService;
import com.ldz.znzp.model.ClZnzp;
import com.ldz.znzp.model.ClZpXl;

import java.util.List;

public interface ZpXlService extends BaseService<ClZpXl,String>{
    ApiResponse<String> saveEntity(ClZpXl entity);
}
