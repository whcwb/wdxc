package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClLsdw;

public interface LsdwService extends BaseService<ClLsdw,String>{
    ApiResponse<String> saveEntity(ClLsdw entity);

    ApiResponse<String> delUnit(String id);
}
