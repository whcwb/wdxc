package com.ldz.service.biz.interfaces;

import com.ldz.dao.biz.model.ClCd;
import com.ldz.dao.biz.model.ClCl;
import com.ldz.dao.biz.model.ClJsy;
import com.ldz.sys.base.BaseService;
import com.ldz.util.bean.ApiResponse;

import java.util.List;

public interface CdService extends BaseService<ClCd,String> {
    ApiResponse<String> saveEntity(ClCd entity);

    ApiResponse<String> updateEntity(ClCd entity);

    ApiResponse<List<ClCl>> notBindCarList();

    ApiResponse<List<ClJsy>> notBindDriverList();
}
