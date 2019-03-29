package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClXlcl;

public interface XlclService extends BaseService<ClXlcl,String>{
    ApiResponse<String> saveEntity(ClXlcl entity);
}
