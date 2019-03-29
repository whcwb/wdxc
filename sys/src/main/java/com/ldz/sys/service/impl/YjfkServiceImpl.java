package com.ldz.sys.service.impl;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.mapper.SysYjfkMapper;
import com.ldz.sys.model.SysYjfk;
import com.ldz.sys.service.YjfkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

@Service
public class YjfkServiceImpl extends BaseServiceImpl<SysYjfk,String> implements YjfkService {
    @Autowired
    private SysYjfkMapper yjfkMapper;
    @Override
    protected Mapper<SysYjfk> getBaseMapper() {
        return yjfkMapper;
    }
}
