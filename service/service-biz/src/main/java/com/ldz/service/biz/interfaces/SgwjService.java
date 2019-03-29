package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClSgwj;

public interface SgwjService extends BaseService<ClSgwj,String>{
    ApiResponse<String> saveEntity(ClSgwj entity);
}
