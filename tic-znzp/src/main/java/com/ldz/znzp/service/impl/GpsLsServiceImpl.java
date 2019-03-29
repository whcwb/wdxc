package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClGpsLsMapper;
import com.ldz.znzp.model.ClGpsLs;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.GpsLsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class GpsLsServiceImpl extends BaseServiceImpl<ClGpsLs,String> implements GpsLsService{
    @Autowired
    private ClGpsLsMapper entityMapper;

    @Override
    protected Mapper<ClGpsLs> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClGpsLs.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClGpsLs entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
