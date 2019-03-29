package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClSgMapper;
import com.ldz.znzp.model.ClSg;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.SgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class SgServiceImpl extends BaseServiceImpl<ClSg,String> implements SgService{
    @Autowired
    private ClSgMapper entityMapper;

    @Override
    protected Mapper<ClSg> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClSg.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClSg entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
