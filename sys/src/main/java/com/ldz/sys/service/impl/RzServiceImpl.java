package com.ldz.sys.service.impl;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.mapper.SysPtrzMapper;
import com.ldz.sys.model.SysRz;
import com.ldz.sys.service.RzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class RzServiceImpl extends BaseServiceImpl<SysRz,String> implements RzService {
    @Autowired
    private SysPtrzMapper entityMapper;

    @Override
    protected Mapper<SysRz> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return SysRz.class;
    }

    @Override
    public boolean fillPagerCondition(LimitedCondition condition){
        condition.setOrderByClause("czsj desc");
        return true;
    }
}
