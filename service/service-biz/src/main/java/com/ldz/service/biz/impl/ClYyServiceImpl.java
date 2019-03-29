package com.ldz.service.biz.impl;

import com.ldz.dao.biz.mapper.ClyyMapper;
import com.ldz.dao.biz.model.Clyy;
import com.ldz.service.biz.interfaces.ClYyService;
import com.ldz.sys.base.BaseServiceImpl;
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

    @Override
    public void saveBatch(List<Clyy> clyys) {
        clyys.forEach(clyy -> clyy.setId(genId()));
        clyyMapper.insertList(clyys);

    }
}
