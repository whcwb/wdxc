package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClDzwlClMapper;
import com.ldz.znzp.model.ClDzwlCl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.DzwlClService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class DzwlClServiceImpl extends BaseServiceImpl<ClDzwlCl,String> implements DzwlClService{
    @Autowired
    private ClDzwlClMapper entityMapper;

    @Override
    protected Mapper<ClDzwlCl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClDzwlCl.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClDzwlCl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
