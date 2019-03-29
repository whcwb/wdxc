package com.ldz.wechat.module.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.module.mapper.ClDdrzMapper;
import com.ldz.wechat.module.model.ClDd;
import com.ldz.wechat.module.model.ClDdrz;
import com.ldz.wechat.module.service.DdrzService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.common.Mapper;

@Slf4j
@Service
public class DdrzServiceImpl extends BaseServiceImpl<ClDdrz,String> implements DdrzService {
    @Autowired
    private ClDdrzMapper entityMapper;

    @Override
    protected Mapper<ClDdrz> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClDdrz.class;
    }


    @Override
    public void log(ClDd order) {
        try{
            ClDdrz clDdrz=new ClDdrz();
            clDdrz.setId(genId());//主键ID
            clDdrz.setDdId(order.getId());//订单ID
            clDdrz.setCjsj(new Date());//创建时间
//            clDdrz.setCjr(getOperateUser());//创建人
            clDdrz.setJgdm(order.getJgdm());//机构代码
            clDdrz.setJgmc(order.getJgmc());//机构名称
            clDdrz.setDdzt(order.getDdzt());//订单状态
            clDdrz.setBz(order.getSy());
            entityMapper.insertSelective(clDdrz);
        }catch (Exception e){
            log.error("订单日志记录异常");
        }
    }
}
