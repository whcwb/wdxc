package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClCssdMapper;
import com.ldz.znzp.model.ClCssd;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.CssdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class CssdServiceImpl extends BaseServiceImpl<ClCssd,String> implements CssdService{
    @Autowired
    private ClCssdMapper entityMapper;

    @Override
    protected Mapper<ClCssd> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClCssd.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClCssd entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
