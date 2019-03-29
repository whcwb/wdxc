package com.ldz.sys.service.impl;

import com.github.pagehelper.PageInfo;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.sys.mapper.SysZdlmMapper;
import com.ldz.sys.mapper.SysZdxmMapper;
import com.ldz.sys.model.SysZdlm;
import com.ldz.sys.model.SysZdxm;
import com.ldz.sys.service.ZdlmService;
import com.ldz.sys.service.ZdxmService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * auther chenwei
 * create at 2018/2/27
 */
@Service
public class ZdlmServiceImpl extends BaseServiceImpl<SysZdlm,String> implements ZdlmService{
    @Autowired
    private SysZdlmMapper zdlmMapper;
    @Autowired
    private SysZdxmMapper zdxmMapper;
    @Autowired
    private ZdxmService zdxmService;

    @Override
    protected Mapper<SysZdlm> getBaseMapper() {
        return zdlmMapper;
    }

    /**
     * 按指定字段进行排序
     * @param condition
     * @return
     */
    @Override
    public boolean fillCondition(LimitedCondition condition){
        condition.setOrderByClause("qz desc");
        return true;
    }


    @Override
    public void afterPager(PageInfo<SysZdlm> resultPage){
        List<SysZdlm> list = resultPage.getList();
        List<String> lmdms = list.stream().map(SysZdlm::getLmdm).collect(Collectors.toList());
        List<SysZdxm> zdxms = zdxmService.findByZdlms(lmdms);
        Map<String,SysZdlm> zdlmMap = list.stream().collect(Collectors.toMap(SysZdlm::getLmdm,p->p));
        for (SysZdxm zdxm : zdxms) {
            SysZdlm zdlm = zdlmMap.get(zdxm.getZdlmdm());
            if (zdlm == null)continue;
            if (zdlm.getZdxmList() == null){
                List<SysZdxm> zdxmList = new ArrayList<>();
                zdxmList.add(zdxm);
                zdlm.setZdxmList(zdxmList);
            }else{
                zdlm.getZdxmList().add(zdxm);
            }
        }
    }

    @Override
    public ApiResponse<String> add(SysZdlm zdlm) {
        RuntimeCheck.ifBlank(zdlm.getLmdm(),"类目代码不能为空");
        RuntimeCheck.ifBlank(zdlm.getLmmc(),"类目名称不能为空");
//        RuntimeCheck.ifTrue(ifExists(SysZdlm.InnerColumn.lmdm.getValue(),zdlm.getLmdm()),"类目代码已存在");

        zdlm.setCjr(getOperateUser());
        zdlm.setCjsj(new Date());
        zdlmMapper.insertSelective(zdlm);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<String> removeWithCheck(String id) {
        return removesWithCheck(Collections.singletonList(id));
    }

    @Override
    public ApiResponse<String> removesWithCheck(List<String> lmdms) {
        SimpleCondition condition = new SimpleCondition(SysZdxm.class);
        condition.in(SysZdxm.InnerColumn.zdlmdm,lmdms);
        int count = zdxmMapper.selectCountByExample(condition);
        RuntimeCheck.ifTrue(count > 0,"该字典类目下面存在字典项目，请先删除字典项目");

        condition = new SimpleCondition(SysZdlm.class);
        condition.in(SysZdlm.InnerColumn.lmdm,lmdms);
        zdlmMapper.deleteByExample(condition);
        return ApiResponse.success();
    }

    @Override
    public void setZdxms(List<SysZdlm> zdlmList) {
        if (zdlmList == null || zdlmList.size() == 0)return;
        List<String> lmdms = zdlmList.stream().map(SysZdlm::getLmdm).collect(Collectors.toList());
        List<SysZdxm> zdxms = zdxmService.findIn(SysZdxm.InnerColumn.zdlmdm,lmdms);
        if (zdxms.size() == 0)return;

        Map<String,SysZdlm> zdlmMap = zdlmList.stream().collect(Collectors.toMap(SysZdlm::getLmdm,p->p));
        for (SysZdxm zdxm : zdxms) {
            String lmdm = zdxm.getZdlmdm();
            if (!zdlmMap.containsKey(lmdm))continue;
            SysZdlm zdlm = zdlmMap.get(lmdm);
            if (zdlm.getZdxmList() == null){
                List<SysZdxm> zdxmList = new ArrayList<>();
                zdxmList.add(zdxm);
                zdlm.setZdxmList(zdxmList);
            }else{
                zdlm.getZdxmList().add(zdxm);
            }
        }
    }
}
