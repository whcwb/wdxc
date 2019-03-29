package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClSpkMapper;
import com.ldz.znzp.model.ClSpk;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.SpkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class SpkServiceImpl extends BaseServiceImpl<ClSpk,String> implements SpkService{
    @Autowired
    private ClSpkMapper entityMapper;

    @Override
    protected Mapper<ClSpk> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClSpk.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClSpk entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
