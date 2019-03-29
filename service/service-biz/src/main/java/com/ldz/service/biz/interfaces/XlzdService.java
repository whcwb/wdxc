package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.model.ClXlzd;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

public interface XlzdService extends BaseService<ClXlzd,String>{
    ApiResponse<String> saveEntity(ClXlzd entity);
    ApiResponse<String> updateEntity(ClXlzd entity);
}
