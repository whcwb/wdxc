package com.ldz.service.biz.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ldz.dao.biz.model.ClDd;
import com.ldz.service.biz.interfaces.DdService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.dao.biz.mapper.ClJsyMapper;
import com.ldz.dao.biz.model.ClCl;
import com.ldz.dao.biz.model.ClJsy;
import com.ldz.service.biz.interfaces.ClService;
import com.ldz.service.biz.interfaces.JsyService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.model.SysYh;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.exception.RuntimeCheck;

import tk.mybatis.mapper.common.Mapper;

@Service
public class JsyServiceImpl extends BaseServiceImpl<ClJsy,String> implements JsyService{
    @Autowired
    private ClJsyMapper entityMapper;
    @Autowired
    private ClService clService;
    @Autowired
    private DdService ddService;

   

    @Override
    protected Mapper<ClJsy> getBaseMapper() {
        return entityMapper;
    }

    @Override
    protected Class<?> getEntityCls(){
        return ClJsy.class;
    }

    @Override
    public ApiResponse<String> saveEntity(ClJsy entity) {
    	 SysYh user = getCurrentUser();
    	 RuntimeCheck.ifTrue(ifExists(ClJsy.InnerColumn.sfzhm.name(),entity.getSfzhm()),"身份证号码已存在");
         Date now = new Date();
         entity.setCjr(getOperateUser());
         entity.setCjsj(now);
         entity.setJgdm(user.getJgdm());
         entity.setPwd("11111");
         save(entity);
        return ApiResponse.saveSuccess();
    }

	@Override
	public ApiResponse<String> updateEntity(ClJsy entity) {
		 ClJsy findById = findById(entity.getSfzhm());
	        RuntimeCheck.ifNull(findById,"未找到记录");
	        entity.setXgr(getOperateUser());
	        entity.setXgsj(new Date());
            entity.setZt(null);//数据操作时，驾驶员状态是禁止被修改的。
	        update(entity);
		return ApiResponse.success();
	}

    @Override
    public ApiResponse<List<ClJsy>> notBindList(SysYh user) {
        // 查找已绑定的驾驶员
        SimpleCondition condition = new SimpleCondition(ClCl.class);
        condition.eq(ClCl.InnerColumn.jgdm,user.getJgdm());
        List<ClCl> cars = clService.findByCondition(condition);
        condition = new SimpleCondition(ClJsy.class);
        condition.eq(ClJsy.InnerColumn.jgdm,user.getJgdm());
        List<ClJsy> drivers = entityMapper.selectByExample(condition);
        if (cars.size() != 0){
            List<String> bindDriverIds = cars.stream().filter(p->p.getSjId() != null).map(ClCl::getSjId).collect(Collectors.toList());
            if (bindDriverIds.size() != 0){
                drivers.removeIf(jsy -> bindDriverIds.contains(jsy.getSfzhm()));
            }
        }
        return ApiResponse.success(drivers);
    }

    public ApiResponse<String> updateJsyType(ClJsy entity){
        ClJsy findById = findById(entity.getSfzhm());
        RuntimeCheck.ifNull(findById,"未找到记录");

        String zt=StringUtils.trim(entity.getZt());
        RuntimeCheck.ifTrue(StringUtils.isEmpty(zt),"入参错误，请重新填写参数！");
       if(StringUtils.equals("10",zt)){
            SimpleCondition condition = new SimpleCondition(ClDd.class);
            condition.eq(ClDd.InnerColumn.sj.name(), entity.getSfzhm());//通过司机查询订单
            condition.lte(ClDd.InnerColumn.yysj.name(), new Date());//查询
            condition.eq(ClDd.InnerColumn.ddzt.name(), "13");//订单状态
            Integer count = ddService.countByCondition(condition);
            RuntimeCheck.ifTrue(count > 0,"该司机还有未完成的订单，请完结订单、撤销订单后再休息！");
       }

        ClJsy obj=new ClJsy();
        obj.setSfzhm(entity.getSfzhm());//驾驶员身份证号码
        obj.setZt(entity.getZt());//
        int i=update(obj);
        return  i>0?ApiResponse.success():ApiResponse.fail("数据库操作失败");
    }
}
