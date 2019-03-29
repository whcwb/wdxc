package com.ldz.wechat.module.service.impl;

import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.module.mapper.ClXlzdMapper;
import com.ldz.wechat.module.model.ClXlzd;
import com.ldz.wechat.module.service.XlzdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;


@Service
public class XlzdServiceImpl extends BaseServiceImpl<ClXlzd,String> implements XlzdService {
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
}
