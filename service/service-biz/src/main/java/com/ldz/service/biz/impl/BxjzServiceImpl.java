package com.ldz.service.biz.impl;

import com.ldz.dao.biz.mapper.ClBxjzMapper;
import com.ldz.dao.biz.model.ClBxjz;
import com.ldz.service.biz.interfaces.BxjzService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.JgService;
import com.ldz.util.bean.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

@Service
public class BxjzServiceImpl extends BaseServiceImpl<ClBxjz,String> implements BxjzService {
    @Autowired
    private ClBxjzMapper bxjzMapper;
    @Autowired
    private JgService jgService;
    @Override
    protected Mapper<ClBxjz> getBaseMapper() {
        return bxjzMapper;
    }

    @Override
    public ApiResponse<String> validAndSave(ClBxjz e){
        SysYh user = getCurrentUser();
        e.setId(genId());
        e.setCjr(getOperateUser());
        e.setCjsj(new Date());
        e.setJgdm(user.getJgdm());
        SysJg org = jgService.findByOrgCode(user.getJgdm());
        e.setJgmc(org.getJgmc());
        save(e);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<String> validAndUpdate(ClBxjz e){
        update(e);
        return ApiResponse.success();
    }


    @Override
    public boolean fillPagerCondition(LimitedCondition condition){

        SysYh currentUser = getCurrentUser();
        condition.startWith(ClBxjz.InnerColumn.jgdm,currentUser.getJgdm());
        return true;
    }


}
