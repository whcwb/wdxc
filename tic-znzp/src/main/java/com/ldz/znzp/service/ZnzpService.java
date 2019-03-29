package com.ldz.znzp.service;

import com.ldz.znzp.base.BaseService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.model.ClZnzp;

import java.util.List;

public interface ZnzpService extends BaseService<ClZnzp,String>{
    ApiResponse<String> saveEntity(ClZnzp entity);

    ApiResponse<String> updateMedia(String jgdm);

    List<ClZnzp> getByXlId(String xlId);
}
