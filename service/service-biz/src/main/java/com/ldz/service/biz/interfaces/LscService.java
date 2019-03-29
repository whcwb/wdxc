package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClLsc;

public interface LscService extends BaseService<ClLsc,String>{
    ApiResponse<String> saveEntity(ClLsc entity);
}
