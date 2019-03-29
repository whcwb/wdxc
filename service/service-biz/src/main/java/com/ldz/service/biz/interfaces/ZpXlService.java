package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClZpXl;

public interface ZpXlService extends BaseService<ClZpXl,String>{
    ApiResponse<String> saveEntity(ClZpXl entity);
}
