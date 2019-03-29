package com.ldz.service.biz.interfaces;

import java.util.List;

import com.ldz.dao.biz.model.ClXl;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

public interface XlService extends BaseService<ClXl,String>{
    ApiResponse<String> saveEntity(ClXl entity);
    ApiResponse<String> updateEntity(ClXl entity);
    List<ClXl> getByZdId(String zdId);
}
