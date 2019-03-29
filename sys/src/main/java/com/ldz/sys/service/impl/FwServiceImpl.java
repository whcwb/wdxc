package com.ldz.sys.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.sys.mapper.SysJgsqlbMapper;
import com.ldz.sys.mapper.SysPtfwMapper;
import com.ldz.sys.mapper.SysPtjgMapper;
import com.ldz.sys.mapper.SysResourceMapper;
import com.ldz.sys.mapper.SysRsRoleBizMapper;
import com.ldz.sys.model.SysBiz;
import com.ldz.sys.model.SysFw;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysJgsq;
import com.ldz.sys.model.SysResource;
import com.ldz.sys.model.SysRsRoleBiz;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.FwService;
import com.ldz.sys.service.JgService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

@Service
public class FwServiceImpl extends BaseServiceImpl<SysFw, String> implements FwService {
    @Autowired
    private JgService jgService;
   
    @Autowired
    private SysPtjgMapper ptjgMapper;
    @Autowired
    private SysJgsqlbMapper jgsqMapper;
    @Autowired
    private SysPtfwMapper bizMapper;
    @Autowired
    private SysRsRoleBizMapper roleBizMapper;
    @Autowired
    private SysResourceMapper resourceMapper;
    @Override
    protected Mapper<SysFw> getBaseMapper() {
        return bizMapper;
    }

    @Override
    public ApiResponse<String> saveEntity(SysFw entity) {
    	RuntimeCheck.ifBlank(entity.getFwdm(), "请输入服务代码");
    	RuntimeCheck.ifBlank(entity.getFwmc(), "请输入服务名称");
    	RuntimeCheck.ifBlank(entity.getApiQz(), "请输入服务api前缀");
        SysYh user = getCurrentUser();
        entity.setCjr(user.getYhid());
        entity.setCjsj(new Date());
        entity.setFwId(genId());
        bizMapper.insert(entity);
        return ApiResponse.success();
    }

    /**
     * 设置机构服务
     *
     * @param orgCode      机构代码
     * @param serviceCodes 服务代码
     * @return 执行结果
     */
    @Override
    public ApiResponse<String> setOrgService(String orgCode, List<String> serviceCodes) {
        SysYh user = getCurrentUser();
        RuntimeCheck.ifBlank(orgCode,"请选择机构");
        SysJg org = jgService.findByOrgCode(orgCode);
        RuntimeCheck.ifNull(org,"机构不存在");

        // 删除旧数据
        SimpleCondition condition = new SimpleCondition(SysJg.class);
        condition.eq(SysJg.InnerColumn.jgdm,orgCode);
        ptjgMapper.deleteByExample(condition);

        // 插入新数据
        Date now = new Date();
        for (String serviceCode : serviceCodes) {
            SysJgsq jgsq = new SysJgsq();
            jgsq.setCjr(user.getYhid());
            jgsq.setCjsj(now);
            jgsq.setFwdm(serviceCode);
            jgsq.setJgdm(orgCode);
            jgsqMapper.insertSelective(jgsq);
        }
        return ApiResponse.success();
    }

    /**
     * 查询机构拥有的功能列表
     *
     * @param orgCode 机构代码
     * @return 功能列表
     */
    @Override
    public List<SysFw> findByJgdm(String orgCode) {
        RuntimeCheck.ifBlank(orgCode,"请选择机构");
        SysJg org = jgService.findByOrgCode(orgCode);
        RuntimeCheck.ifNull(org,"机构不存在");

        SimpleCondition condition = new SimpleCondition(SysJgsq.class);
        condition.eq(SysJgsq.InnerColumn.jgdm,orgCode);
        List<SysJgsq> jgsqs = jgsqMapper.selectByExample(condition);
        if (jgsqs.size() == 0)return new ArrayList<>();

        List<String> serviceCodes = jgsqs.stream().map(SysJgsq::getJgdm).collect(Collectors.toList());
        return findIn(SysFw.InnerColumn.fwdm,serviceCodes);
    }


    @Override
    public ApiResponse<String> removeIdsWithCheck(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        Example resExample = new Example(SysResource.class);
        resExample.and().andEqualTo(SysResource.InnerColumn.resLevel.name(),1);
        resExample.and().andIn(SysResource.InnerColumn.resPid.name(),idList);
        int count = resourceMapper.selectCountByExample(resExample);
        RuntimeCheck.ifTrue(count != 0,"该服务下有关联资源，不能删除");

        Example bizExample = new Example(SysBiz.class);
        bizExample.and().andIn(SysBiz.InnerColumn.bizId.name(),idList);
        bizMapper.deleteByExample(bizExample);

        // 删除角色服务关联
        Example roleBizExample = new Example(SysRsRoleBiz.class);
        roleBizExample.and().andIn(SysRsRoleBiz.InnerColumn.bizId.name(),idList);
        roleBizMapper.deleteByExample(roleBizExample);
        return ApiResponse.success();
    }

	@Override
	public ApiResponse<String> updateEntity(SysFw entity) {  
		RuntimeCheck.ifBlank(entity.getFwdm(), "请输入服务代码");
    	RuntimeCheck.ifBlank(entity.getFwmc(), "请输入服务名称");
    	RuntimeCheck.ifBlank(entity.getApiQz(), "请输入服务api前缀");
		entity.setXgr(getOperateUser());
		entity.setXgsj(new Date());
		update(entity);
		return ApiResponse.updateSuccess();
	}


}
