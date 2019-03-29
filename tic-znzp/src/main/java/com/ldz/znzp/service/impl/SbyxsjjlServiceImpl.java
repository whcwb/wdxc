package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClSbyxsjjlMapper;
import com.ldz.znzp.model.ClSbyxsjjl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.SbyxsjjlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class SbyxsjjlServiceImpl extends BaseServiceImpl<ClSbyxsjjl,String> implements SbyxsjjlService{
    @Autowired
    private ClSbyxsjjlMapper entityMapper;

    @Override
    protected Mapper<ClSbyxsjjl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClSbyxsjjl.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClSbyxsjjl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
