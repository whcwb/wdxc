package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClJsyMapper;
import com.ldz.znzp.model.ClJsy;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.JsyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class JsyServiceImpl extends BaseServiceImpl<ClJsy,String> implements JsyService{
    @Autowired
    private ClJsyMapper entityMapper;

    @Override
    protected Mapper<ClJsy> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClJsy.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClJsy entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
