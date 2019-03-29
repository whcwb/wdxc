package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClDzwlMapper;
import com.ldz.znzp.model.ClDzwl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.DzwlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class DzwlServiceImpl extends BaseServiceImpl<ClDzwl,String> implements DzwlService{
    @Autowired
    private ClDzwlMapper entityMapper;

    @Override
    protected Mapper<ClDzwl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClDzwl.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClDzwl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
