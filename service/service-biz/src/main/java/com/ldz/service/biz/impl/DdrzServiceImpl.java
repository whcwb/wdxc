package com.ldz.service.biz.impl;

import java.util.Date;
import java.util.List;

import com.ldz.service.biz.interfaces.DdrzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.dao.biz.mapper.ClDdrzMapper;
import com.ldz.dao.biz.model.ClDd;
import com.ldz.dao.biz.model.ClDdrz;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;

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
    public ApiResponse<String> saveEntity(ClDdrz entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }

    /**
     * 通过订单ID查询该订单下的所有列表
     * @param orderId
     * @return
     */
    @Override
    public List<ClDdrz> getOrderList(String orderId) {
        SimpleCondition condition = new SimpleCondition(ClDdrz.class);
        condition.eq(ClDdrz.InnerColumn.ddId,orderId);
        condition.setOrderByClause(ClDdrz.InnerColumn.cjsj.asc());
        List<ClDdrz> oracleLog = findByCondition(condition);
        return oracleLog;
    }

    @Override
    public void log(ClDd order) {
        try{
            ClDdrz clDdrz=new ClDdrz();
            clDdrz.setId(genId());//主键ID
            clDdrz.setDdId(order.getId());//订单ID
            clDdrz.setCjsj(new Date());//创建时间
            clDdrz.setCjr(getOperateUser());//创建人
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
