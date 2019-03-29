package com.ldz.service.biz.impl;

import com.ldz.dao.biz.mapper.ClReportLogMapper;
import com.ldz.dao.biz.model.ClReportLog;
import com.ldz.service.biz.interfaces.ClReportLogService;
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
