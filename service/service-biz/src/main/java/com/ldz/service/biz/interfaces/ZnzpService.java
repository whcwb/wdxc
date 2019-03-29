package com.ldz.service.biz.interfaces;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseService;
import com.ldz.dao.biz.model.ClZnzp;

import java.util.List;

public interface ZnzpService extends BaseService<ClZnzp,String>{
    ApiResponse<String> saveEntity(ClZnzp entity);

    ApiResponse<String> updateEntity(ClZnzp znzp);

    ApiResponse<List<String>> getXlIds(String zpId);
}
