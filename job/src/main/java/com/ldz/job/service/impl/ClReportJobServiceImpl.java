package com.ldz.job.service.impl;

import com.ldz.dao.biz.mapper.ClReportJobMapper;
import com.ldz.dao.biz.model.ClReportJob;
import com.ldz.job.service.ClReportJobService;
import com.ldz.sys.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class ClReportJobServiceImpl extends BaseServiceImpl<ClReportJob,String> implements ClReportJobService {

    @Autowired
    private ClReportJobMapper baseMapper;
    @Override
    protected Mapper<ClReportJob> getBaseMapper() {
        return baseMapper;
    }
}
