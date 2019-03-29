package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClDdMapper;
import com.ldz.znzp.model.ClDd;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.DdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class DdServiceImpl extends BaseServiceImpl<ClDd,String> implements DdService{
    @Autowired
    private ClDdMapper entityMapper;

    @Override
    protected Mapper<ClDd> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClDd.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClDd entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
