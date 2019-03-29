package com.ldz.service.biz.impl;

import com.ldz.service.biz.interfaces.SbycsjjlService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.dao.biz.mapper.ClSbycsjjlMapper;
import com.ldz.dao.biz.model.ClSbycsjjl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class SbycsjjlServiceImpl extends BaseServiceImpl<ClSbycsjjl,String> implements SbycsjjlService {
    @Autowired
    private ClSbycsjjlMapper entityMapper;

    @Override
    protected Mapper<ClSbycsjjl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClSbycsjjl.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClSbycsjjl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
