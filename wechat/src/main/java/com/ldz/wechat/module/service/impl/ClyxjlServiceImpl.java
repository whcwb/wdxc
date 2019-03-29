package com.ldz.wechat.module.service.impl;

import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.module.mapper.ClClyxjlMapper;
import com.ldz.wechat.module.model.ClClyxjl;
import com.ldz.wechat.module.service.ClyxjlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class ClyxjlServiceImpl extends BaseServiceImpl<ClClyxjl,String> implements ClyxjlService {
    @Autowired
    private ClClyxjlMapper entityMapper;

    @Override
    protected Mapper<ClClyxjl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClClyxjl.class;
    }


}
