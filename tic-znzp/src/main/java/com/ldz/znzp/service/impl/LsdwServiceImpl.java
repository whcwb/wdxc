package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClLsdwMapper;
import com.ldz.znzp.model.ClLsdw;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.LsdwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class LsdwServiceImpl extends BaseServiceImpl<ClLsdw,String> implements LsdwService{
    @Autowired
    private ClLsdwMapper entityMapper;

    @Override
    protected Mapper<ClLsdw> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClLsdw.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClLsdw entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
