package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.model.ClSbyxsjjl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClClyxjl;

public interface ClyxjlService extends BaseService<ClClyxjl,String>{
    ApiResponse<String> saveEntity(ClClyxjl entity);


}
