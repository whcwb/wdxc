package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.model.ClDd;
import com.ldz.dao.biz.model.ClDdrz;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.util.List;

public interface DdrzService extends BaseService<ClDdrz,String>{
    ApiResponse<String> saveEntity(ClDdrz entity);

    List<ClDdrz> getOrderList(String orderId);

    void log(ClDd order);
}
