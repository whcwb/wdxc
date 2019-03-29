package com.ldz.job.service.impl;


import com.ldz.dao.biz.model.ClXc;
import com.ldz.dao.biz.mapper.ClXcMapper;
import com.ldz.job.service.XcService;
import com.ldz.sys.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class XcServiceImpl extends BaseServiceImpl<ClXc,String> implements XcService {
    @Autowired
    private ClXcMapper entityMapper;

    @Override
    protected Mapper<ClXc> getBaseMapper() {
        return entityMapper;
    }

}
