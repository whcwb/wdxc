package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClDzwlCl;

public interface DzwlClService extends BaseService<ClDzwlCl,String>{
    ApiResponse<String> saveEntity(ClDzwlCl entity);
}
