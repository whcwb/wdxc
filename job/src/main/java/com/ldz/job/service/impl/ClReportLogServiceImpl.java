package com.ldz.job.service.impl;

import com.ldz.dao.biz.mapper.ClReportLogMapper;
import com.ldz.dao.biz.model.ClReportLog;
import com.ldz.job.service.ClReportLogService;
import com.ldz.sys.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class ClReportLogServiceImpl extends BaseServiceImpl<ClReportLog,String> implements ClReportLogService {

    @Autowired
    private ClReportLogMapper baseMapper;

    @Override
    protected Mapper<ClReportLog> getBaseMapper() {
        return baseMapper;
    }
}
