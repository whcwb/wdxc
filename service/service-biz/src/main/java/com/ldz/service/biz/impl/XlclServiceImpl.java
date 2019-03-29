package com.ldz.service.biz.impl;

import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.dao.biz.mapper.ClXlclMapper;
import com.ldz.dao.biz.model.ClXlcl;
import com.ldz.service.biz.interfaces.XlclService;
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
