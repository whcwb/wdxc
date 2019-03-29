package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClCdMapper;
import com.ldz.znzp.model.ClCd;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.CdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class CdServiceImpl extends BaseServiceImpl<ClCd,String> implements CdService{
    @Autowired
    private ClCdMapper entityMapper;

    @Override
    protected Mapper<ClCd> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClCd.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClCd entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
