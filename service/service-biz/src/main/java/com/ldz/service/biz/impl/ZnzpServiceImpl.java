package com.ldz.service.biz.impl;

import com.ldz.dao.biz.mapper.ClZpXlMapper;
import com.ldz.dao.biz.model.ClZpXl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.dao.biz.mapper.ClZnzpMapper;
import com.ldz.dao.biz.model.ClZnzp;
import com.ldz.service.biz.interfaces.ZnzpService;
import com.ldz.util.bean.SimpleCondition;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZnzpServiceImpl extends BaseServiceImpl<ClZnzp,String> implements ZnzpService{
    @Autowired
    private ClZnzpMapper entityMapper;
    @Autowired
    private ClZpXlMapper zpXlMapper;

    @Override
    protected Mapper<ClZnzp> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClZnzp.class;
    }


    @Override
    public ApiResponse<String> saveEntity(ClZnzp entity) {
        entity.setCjr(getOperateUser());
        entity.setCjsj(new Date());
        save(entity);
        return ApiResponse.saveSuccess();
    }

    @Override
    public ApiResponse<String> updateEntity(ClZnzp znzp) {
        znzp.setXgr(getOperateUser());
        znzp.setXgsj(new Date());
        update(znzp);
        if (StringUtils.isNotEmpty(znzp.getXlIds())){
            List<String> xlIds = Arrays.asList(znzp.getXlIds().split(","));
            setZnzpXl(xlIds,znzp.getZdbh());
        }
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<List<String>> getXlIds(String zpId) {
        SimpleCondition condition = new SimpleCondition(ClZpXl.class);
        condition.eq(ClZpXl.InnerColumn.zpId,zpId);
        List<ClZpXl> zpXls = zpXlMapper.selectByExample(condition);
        if (zpXls.size() == 0)return new ApiResponse<>(new ArrayList<>());
        List<String> xlIds = zpXls.stream().map(ClZpXl::getXlId).collect(Collectors.toList());
        return new ApiResponse<>(xlIds);
    }

    private void setZnzpXl(List<String> xlIds, String znzpId){
        if (xlIds == null || xlIds.size() == 0){
            return;
        }
        String cjr = getOperateUser();
        Date cjsj = new Date();

        SimpleCondition condition = new SimpleCondition(ClZpXl.class);
        condition.eq(ClZpXl.InnerColumn.zpId,znzpId);
        zpXlMapper.deleteByExample(condition);

        for (String xlId : xlIds) {
            ClZpXl zpXl = new ClZpXl();
            zpXl.setId(genId());
            zpXl.setZpId(znzpId);
            zpXl.setXlId(xlId);
            zpXl.setCjr(cjr);
            zpXl.setCjsj(cjsj);
            zpXlMapper.insertSelective(zpXl);
        }
    }
}
