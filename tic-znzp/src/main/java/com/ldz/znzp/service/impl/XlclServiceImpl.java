package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClXlclMapper;
import com.ldz.znzp.model.ClXlcl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.XlclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class XlclServiceImpl extends BaseServiceImpl<ClXlcl,String> implements XlclService{
    @Autowired
    private ClXlclMapper entityMapper;

    @Override
    protected Mapper<ClXlcl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClXlcl.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClXlcl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
