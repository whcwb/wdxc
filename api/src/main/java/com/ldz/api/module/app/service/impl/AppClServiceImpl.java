package com.ldz.api.module.app.service.impl;

import com.ldz.api.module.app.service.AppClService;
import com.ldz.api.util.ContextUtil;
import com.ldz.dao.biz.mapper.ClClMapper;
import com.ldz.dao.biz.model.ClCl;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 * auther chenwei
 * create at 2019/1/6
 */
@Service
public class AppClServiceImpl extends BaseServiceImpl<ClCl,String> implements AppClService {
    @Autowired
    private ClClMapper clMapper;
    @Override
    protected Mapper<ClCl> getBaseMapper() {
        return clMapper;
    }

    @Override
    public boolean fillPagerCondition(LimitedCondition condition){
        return true;
    }

}
