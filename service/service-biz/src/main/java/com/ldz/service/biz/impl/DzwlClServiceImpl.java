package com.ldz.service.biz.impl;

import com.github.pagehelper.PageInfo;
import com.ldz.dao.biz.mapper.ClClMapper;
import com.ldz.dao.biz.mapper.ClDzwlClMapper;
import com.ldz.dao.biz.model.ClCl;
import com.ldz.dao.biz.model.ClDzwl;
import com.ldz.dao.biz.model.ClDzwlCl;
import com.ldz.service.biz.interfaces.DzwlClService;
import com.ldz.service.biz.interfaces.DzwlService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.model.SysYh;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DzwlClServiceImpl extends BaseServiceImpl<ClDzwlCl,String> implements DzwlClService{
    @Autowired
    private ClDzwlClMapper entityMapper;
    @Autowired
    private DzwlService dzwlService;
    @Autowired
    private ClClMapper clClMapper;

    @Override
    protected Mapper<ClDzwlCl> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClDzwlCl.class;
    }

    @Override
    public boolean fillPagerCondition(LimitedCondition condition){
        SysYh user = getCurrentUser();
        SimpleCondition carCondition = new SimpleCondition(ClCl.class);
        carCondition.eq(ClCl.InnerColumn.jgdm,user.getJgdm());
        List<ClCl> cars = clClMapper.selectByExample(carCondition);
        if (cars.size() == 0)return false;
        List<String> carIds = cars.stream().map(ClCl::getClId).collect(Collectors.toList());
        condition.in(ClDzwlCl.InnerColumn.clId,carIds);
        return true;
    }

    @Override
    public ApiResponse<String> saveEntity(ClDzwlCl entity) {
        save(entity);
        return ApiResponse.saveSuccess();
    }


    @Override
    protected void afterPager(PageInfo<ClDzwlCl> resultPage){
        if (resultPage == null || resultPage.getList() == null ||resultPage.getList().size() == 0){
            return;
        }
        Set<String> wlIds = resultPage.getList().stream().map(ClDzwlCl::getWlId).collect(Collectors.toSet());
        List<ClDzwl> dzwls = dzwlService.findIn(ClDzwl.InnerColumn.id,wlIds);
        if (dzwls.size() == 0)return;
        Map<String,ClDzwl> dzwlMap = dzwls.stream().collect(Collectors.toMap(ClDzwl::getId,p->p));
        for (ClDzwlCl dzwlCl : resultPage.getList()) {
            String wlId = dzwlCl.getWlId();
            if (StringUtils.isEmpty(wlId))continue;
            ClDzwl dzwl = dzwlMap.get(wlId);
            if (dzwl == null)continue;
            dzwlCl.setWlmc(dzwl.getWlmc());
        }
    }
}
