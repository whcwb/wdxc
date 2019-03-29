package com.ldz.znzp.service.impl;

import com.ldz.znzp.base.BaseServiceImpl;
import com.ldz.znzp.mapper.ClPbMapper;
import com.ldz.znzp.model.ClPb;
import com.ldz.util.bean.ApiResponse;
import com.ldz.znzp.service.PbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class PbServiceImpl extends BaseServiceImpl<ClPb,String> implements PbService{
    @Autowired
    private ClPbMapper entityMapper;

    @Override
    protected Mapper<ClPb> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClPb.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClPb entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }
}
