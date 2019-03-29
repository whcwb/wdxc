package com.ldz.wechat.module.service.impl;

import com.ldz.util.bean.SimpleCondition;
import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.module.mapper.SysZdxmMapper;
import com.ldz.wechat.module.model.SysZdxm;
import com.ldz.wechat.module.service.ZdxmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * auther chenwei
 * create at 2018/2/27
 */
@Service
public class ZdxmServiceImpl extends BaseServiceImpl<SysZdxm,String> implements ZdxmService {
    @Autowired
    private SysZdxmMapper zdxmMapper;
    @Override
    protected Mapper<SysZdxm> getBaseMapper() {
        return zdxmMapper;
    }

    @Override
    public List<SysZdxm> findByTypeCode(String typeCode) {
        SimpleCondition condition = new SimpleCondition(SysZdxm.class);
        condition.eq(SysZdxm.InnerColumn.zdlmdm,typeCode);
        return zdxmMapper.selectByExample(condition);
    }

	
}
