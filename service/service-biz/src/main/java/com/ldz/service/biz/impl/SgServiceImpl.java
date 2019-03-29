package com.ldz.service.biz.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.PageInfo;
import com.ldz.dao.biz.mapper.ClJsyMapper;
import com.ldz.dao.biz.model.ClJsy;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.dao.biz.mapper.ClSgMapper;
import com.ldz.dao.biz.mapper.ClSgwjMapper;
import com.ldz.dao.biz.model.ClSg;
import com.ldz.dao.biz.model.ClSgwj;
import com.ldz.service.biz.interfaces.SgService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.sys.model.SysYh;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;

import tk.mybatis.mapper.common.Mapper;

@Service
public class SgServiceImpl extends BaseServiceImpl<ClSg,String> implements SgService{
    @Autowired
    private ClSgMapper entityMapper;
   
    @Autowired
    private ClSgwjMapper sgwjMapper;
    @Autowired
    private ClJsyMapper jsyMapper;

    @Override
    protected Mapper<ClSg> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClSg.class;
    }

    @Override
    public void afterPager(PageInfo<ClSg> pageInfo){
        List<ClSg> list = pageInfo.getList();
        List<String > sgIds = list.stream().map(ClSg::getId).collect(Collectors.toList());
        SimpleCondition condition = new SimpleCondition(ClSgwj.class);
        condition.in(ClSgwj.InnerColumn.sgId,sgIds);
        List<ClSgwj> sgwjs = sgwjMapper.selectByExample(condition);
        Map<String,List<ClSgwj>> wjMap = new HashMap<>();
        for (ClSgwj sgwj : sgwjs) {
            String sgId = sgwj.getSgId();
            if (StringUtils.isEmpty(sgId))continue;
            if (wjMap.containsKey(sgId)){
                wjMap.get(sgId).add(sgwj);
            }else{
                List<ClSgwj> sgwjList = new ArrayList<>();
                sgwjList.add(sgwj);
                wjMap.put(sgId,sgwjList);
            }
        }
        for (ClSg sg : list) {
            if (!wjMap.containsKey(sg.getId()))continue;
            List<ClSgwj> sgwjsList = wjMap.get(sg.getId());
            StringBuilder filePaths = new StringBuilder();
            for (ClSgwj sgwj : sgwjsList) {
                filePaths.append(sgwj.getWldz()).append(",");
            }
            sg.setFilePaths(filePaths.toString());
        }
    }

    @Override
    public ApiResponse<String> validAndSave(ClSg entity) {
        SysYh user = getCurrentUser();
        Date now = new Date();
        entity.setCjr(getOperateUser());
        entity.setCjsj(now);
        entity.setId(genId());
        entity.setJgdm(user.getJgdm());
        if (StringUtils.isNotEmpty(entity.getSj())){
            ClJsy jsy = jsyMapper.selectByPrimaryKey(entity.getSj());
            RuntimeCheck.ifNull(jsy,"驾驶员不存在");
            entity.setLxdh(jsy.getSjh());
        }
        save(entity);
        updateSgwj(entity);
        return ApiResponse.saveSuccess();
    }

    @Override
    public ApiResponse<String> validAndUpdate(ClSg entity) {
        ClSg oldRecord = findById(entity.getId());
        RuntimeCheck.ifNull(oldRecord,"未找到记录");
        entity.setXgr(getOperateUser());
        update(entity);
        updateSgwj(entity);
        return ApiResponse.success();
    }

    private void updateSgwj(ClSg sg){
        if (StringUtils.isEmpty(sg.getFilePaths())){
            return;
        }
        SimpleCondition condition = new SimpleCondition(ClSgwj.class);
        condition.eq(ClSgwj.InnerColumn.sgId,sg.getId());
        sgwjMapper.deleteByExample(condition);

        String creator = getOperateUser();
        Date now = new Date();
        String[] paths = sg.getFilePaths().split(",");
        for (String path : paths) {
            ClSgwj sgwj = new ClSgwj();
            sgwj.setCjr(creator);
            sgwj.setCjsj(now);
            sgwj.setId(genId());
            sgwj.setSgId(sg.getId());
            sgwj.setWldz(path);
            sgwjMapper.insertSelective(sgwj);
        }
    }
}
