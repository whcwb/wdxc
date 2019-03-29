package com.ldz.service.biz.impl;

import com.ldz.dao.biz.model.ClPb;
import com.ldz.dao.biz.model.ClSbyxsjjl;
import com.ldz.service.biz.interfaces.PbService;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.dao.biz.mapper.ClClyxjlMapper;
import com.ldz.dao.biz.model.ClClyxjl;
import com.ldz.service.biz.interfaces.ClyxjlService;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClyxjlServiceImpl extends BaseServiceImpl<ClClyxjl,String> implements ClyxjlService{
    @Autowired
    private ClClyxjlMapper entityMapper;

    @Autowired
    private PbService pbService;

    @Override
    protected Mapper<ClClyxjl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClClyxjl.class;
    }


    @Override
    public boolean fillPagerCondition(LimitedCondition condition){
        //排班
        SimpleCondition pbCondition = new SimpleCondition(ClPb.class);
        pbCondition.and().andCondition(" TO_CHAR (PBSJ, 'yyyy-MM-dd') = ",DateUtils.getToday("yyyy-MM-dd"));
        List<ClPb> pbList=pbService.findByCondition(pbCondition);
        List<String> clList=null;
        if(pbList!=null&&pbList.size()>0){
            clList=pbList.stream().map(ClPb::getClId).collect(Collectors.toList());
            if(clList!=null&&clList.size()>0){
                condition.in(ClClyxjl.InnerColumn.clId,clList);
            }else {
                return false;
            }
        }else {
            return false;
        }
        condition.and().andCondition(" TO_CHAR(CJSJ, 'yyyy-MM-dd') = ",DateUtils.getToday("yyyy-MM-dd"));
        condition.setOrderByClause("cjsj desc");
        return true;
    }


    @Override
    public ApiResponse<String> saveEntity(ClClyxjl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }




}
