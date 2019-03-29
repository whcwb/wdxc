package com.ldz.service.biz.impl;

import com.ldz.service.biz.interfaces.SgwjService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.dao.biz.mapper.ClSgwjMapper;
import com.ldz.dao.biz.model.ClSgwj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class SgwjServiceImpl extends BaseServiceImpl<ClSgwj,String> implements SgwjService {
    @Autowired
    private ClSgwjMapper entityMapper;

    @Override
    protected Mapper<ClSgwj> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClSgwj.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClSgwj entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
