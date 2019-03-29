package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClLscMapper;
import com.ldz.znzp.model.ClLsc;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.LscService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class LscServiceImpl extends BaseServiceImpl<ClLsc,String> implements LscService{
    @Autowired
    private ClLscMapper entityMapper;

    @Override
    protected Mapper<ClLsc> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClLsc.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClLsc entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
