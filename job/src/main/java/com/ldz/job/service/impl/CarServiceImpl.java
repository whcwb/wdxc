package com.ldz.job.service.impl;

import com.ldz.dao.biz.mapper.CarMapper;
import com.ldz.dao.biz.model.Car;
import com.ldz.job.service.CarService;
import com.ldz.sys.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
@Service
public class CarServiceImpl extends BaseServiceImpl<Car,Long> implements CarService {

    @Autowired
    private CarMapper baseMapper;

    @Override
    protected Mapper<Car> getBaseMapper() {
        return baseMapper;
    }
}
