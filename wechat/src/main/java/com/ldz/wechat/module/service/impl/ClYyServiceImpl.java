package com.ldz.wechat.module.service.impl;

import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.module.mapper.ClyyMapper;
import com.ldz.wechat.module.model.Clyy;
import com.ldz.wechat.module.service.ClYyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Service
public class ClYyServiceImpl extends BaseServiceImpl<Clyy,String> implements ClYyService {

    @Autowired
    private ClyyMapper clyyMapper;

    @Override
    protected Mapper<Clyy> getBaseMapper() {
        return clyyMapper;
    }

}
