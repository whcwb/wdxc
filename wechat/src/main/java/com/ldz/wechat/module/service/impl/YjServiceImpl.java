package com.ldz.wechat.module.service.impl;

import java.util.Date;

import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.wechat.base.LimitedCondition;
import com.ldz.wechat.module.model.ClJsy;
import com.ldz.wechat.module.model.SysJzgxx;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldz.util.bean.ApiResponse;
import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.module.mapper.SysYjfkMapper;
import com.ldz.wechat.module.model.SysYjfk;
import com.ldz.wechat.module.service.YjService;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.common.Mapper;

import javax.servlet.http.HttpServletRequest;


@Service
public class YjServiceImpl extends BaseServiceImpl<SysYjfk,String> implements YjService{
    @Autowired
    private SysYjfkMapper yjfkMapper;
    @Override
    protected Mapper<SysYjfk> getBaseMapper() {
        return yjfkMapper;
    }

	/**
	 * 分页补充   按全部、已付款、待付款 来进行查询
	 * @param condition
	 *
	 * @return
	 */
	@Override
	public boolean fillCondition(LimitedCondition condition){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String yjLx = request.getParameter("yjLx");
		if(StringUtils.isEmpty(yjLx)){
			yjLx = request.getParameter("yjlx");
		}
		if(StringUtils.isNotEmpty(yjLx)){
			if(StringUtils.equals(yjLx,"00")||StringUtils.equals(yjLx,"10")||StringUtils.equals(yjLx,"20")){
				condition.eq(SysYjfk.InnerColumn.yjlx.name(),yjLx);
			}
		}

		String type = (String) request.getAttribute("type");
		String userInfo = (String) request.getAttribute("userInfo");
		if ("jzg".equals(type)){
			SysJzgxx jzg = JsonUtil.toBean(userInfo,SysJzgxx.class);
			if(jzg!=null){
				condition.eq(SysYjfk.InnerColumn.userId.name(),jzg.getId());
			}
		}else if ("jsy".equals(type)){
			ClJsy jsy = JsonUtil.toBean(userInfo,ClJsy.class);
			if(jsy!=null){
				condition.eq(SysYjfk.InnerColumn.sjId.name(),jsy.getSfzhm());
			}
		}else{
			condition.and().andIsNull(SysYjfk.InnerColumn.userId.name());
			condition.and().andIsNull(SysYjfk.InnerColumn.sjId.name());
		}

//		//审核状态 00 是未审核  10 是已审核
//		condition.eq(SysYjfk.InnerColumn.zt.name(),"10");
		return true;
	}
	public boolean fillPagerCondition(LimitedCondition condition){
		condition.and().andEqualTo(SysYjfk.InnerColumn.zt.name(),"10");
		return true;
	}

	@Override
	public ApiResponse<String> saveEntity(SysYjfk entity) {
		//默认待处理
		entity.setZt("00");
		entity.setYjId(genId());
		entity.setCjsj(new Date());
		save(entity);
		return ApiResponse.saveSuccess();
	}

	@Override
	public ApiResponse<String> updateEntity(SysYjfk entity) {
		
//		entity.setXgsj(new Date());
//		update(entity);
		return ApiResponse.updateSuccess();
	}
}
