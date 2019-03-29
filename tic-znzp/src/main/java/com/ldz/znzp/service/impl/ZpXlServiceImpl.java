package com.ldz.znzp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClZpXlMapper;
import com.ldz.znzp.model.ClZpXl;
import com.ldz.znzp.service.ZpXlService;

import tk.mybatis.mapper.common.Mapper;

@Service
public class ZpXlServiceImpl extends BaseServiceImpl<ClZpXl,String> implements ZpXlService{
    @Autowired
    private ClZpXlMapper entityMapper;

    @Override
    protected Mapper<ClZpXl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClZpXl.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClZpXl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
