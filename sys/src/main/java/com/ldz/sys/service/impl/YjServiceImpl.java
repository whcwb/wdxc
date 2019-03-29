package com.ldz.sys.service.impl;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.mapper.SysYjfkMapper;
import com.ldz.sys.model.SysYjfk;
import com.ldz.sys.service.YjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author chenwei
 * @copyright
 * @category
 * @since 2018/2/26
 */
@Service
public class YjServiceImpl extends BaseServiceImpl<SysYjfk,String> implements YjService{
    @Autowired
    private SysYjfkMapper yjfkMapper;
    @Override
    protected Mapper<SysYjfk> getBaseMapper() {
        return yjfkMapper;
    }
}
