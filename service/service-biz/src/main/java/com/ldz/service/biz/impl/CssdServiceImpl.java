package com.ldz.service.biz.impl;

import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.mapper.ClClMapper;
import com.ldz.dao.biz.mapper.ClCssdMapper;
import com.ldz.dao.biz.model.ClCl;
import com.ldz.dao.biz.model.ClCssd;
import com.ldz.service.biz.interfaces.CssdService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.JgService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.commonUtil.HttpUtil;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.exception.RuntimeCheck;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CssdServiceImpl extends BaseServiceImpl<ClCssd, String> implements CssdService {
	@Autowired
	private ClCssdMapper entityMapper;
	@Autowired
	private JgService jgService;
	@Autowired
	private ClClMapper clclmapper;
	@Value("${apiurl}")
	private String carcmdUrl;
	@Override
	protected Mapper<ClCssd> getBaseMapper() {
		return entityMapper;
	}

	@Override
	protected Class<?> getEntityCls() {
		return ClCssd.class;
	}

	@Override
	public ApiResponse<String> saveEntity(ClCssd entity) {
		SysYh user = getCurrentUser();
		SysJg org = jgService.findByOrgCode(user.getJgdm());
		Date now = new Date();
		entity.setCjr(getOperateUser());
		entity.setCjsj(now);
		entity.setId(genId());
		entity.setJgdm(user.getJgdm());
		entity.setJgmc(org.getJgmc());
		save(entity);
		return ApiResponse.saveSuccess();
	}

	@Override
	public ApiResponse<String> updateEntity(ClCssd entity) {
		ClCssd oldRecord = findById(entity.getId());
		RuntimeCheck.ifNull(oldRecord, "未找到记录");
		entity.setXgr(getOperateUser());
		entity.setXgsj(new Date());
		update(entity);
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<List<ClCssd>> getByCx(String cx) {
		// 自带机构筛选条件
		LimitedCondition condition = new LimitedCondition(ClCssd.class);
		condition.eq(ClCssd.InnerColumn.cx, cx);
		List<ClCssd> cssds = findByCondition(condition);
		return ApiResponse.success(cssds);
	}

	@Override
	public ApiResponse<String> setCssds(String cphs, String csz) {
		RuntimeCheck.ifNull(cphs,"车牌号码不能为空！");
		RuntimeCheck.ifNull(csz,"超级值设置不能为空！");
		int count = 0;
		SysYh user = getCurrentUser();
		SysJg org = jgService.findByOrgCode(user.getJgdm());
		// 将车牌号,车辆信息缓存
		List<ClCl> selectAll = clclmapper.selectAll();
		Map<String, ClCl> clmap = selectAll.stream().filter(s -> StringUtils.isNotEmpty(s.getCph()))
				.collect(Collectors.toMap(ClCl::getCph, ClCl -> ClCl));

		List<String> cphss = Arrays.asList(cphs.split(","));
		for (String cph : cphss) {
			GpsInfo gpsInfo = new GpsInfo();
			ClCssd cssd = new ClCssd();

			ClCl clCl = clmap.get(cph);
			gpsInfo.setDeviceId(clCl.getZdbh());
			gpsInfo.setCmdType("01");
			gpsInfo.setCmd(csz);
			ApiResponse<String> senZl = senZl(gpsInfo);
			if (senZl.getCode() != 200) {
				count++;
				continue;
			}
			cssd.setCjr(getOperateUser());
			cssd.setCjsj(new Date());
			cssd.setCph(cph);
			cssd.setJgdm(user.getJgdm());
			cssd.setJgmc(org.getJgmc());
			cssd.setSdsx(Short.valueOf(csz));
			insetAndUpdate(cssd);

		}
		return ApiResponse.success();
	}

	@Override
	public void insetAndUpdate(ClCssd entity) {

		boolean flag = ifExists("cph", entity.getCph());
		if (flag == true) {
		entity.setId(null);
        Example example = new Example(ClCssd.class);
        example.createCriteria().andCondition(" CPH=",entity.getCph());
        entityMapper.updateByExample(entity,example);
//			entityMapper.updateByExample()
//			update(entity);
		} else {
			entity.setId(genId());
			save(entity);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public ApiResponse<String> senZl(GpsInfo info) {
		String url =carcmdUrl + "/push/carcmd";
		String postEntity = JsonUtil.toJson(info);
		String result = "";
		ApiResponse<String> apiResponse = null;
		try {
			Map<String, String> postHeaders = new HashMap<>();
			postHeaders.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			result = HttpUtil.postJson(url, postHeaders, postEntity);
			apiResponse = (ApiResponse<String>) JsonUtil.toBean(result, ApiResponse.class);
			if (apiResponse.getCode() != 200) {
				return ApiResponse.fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apiResponse;
	}

	@Override
	public void saveBatch(List<ClCssd> clCssds) {
		entityMapper.insertList(clCssds);
	}

}
