package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClXlzdMapper;
import com.ldz.znzp.model.ClXlzd;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.XlzdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class XlzdServiceImpl extends BaseServiceImpl<ClXlzd,String> implements XlzdService{
    @Autowired
    private ClXlzdMapper entityMapper;

    @Override
    protected Mapper<ClXlzd> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClXlzd.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClXlzd entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
