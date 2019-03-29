package com.ldz.wechat.module.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.wechat.base.BaseServiceImpl;
import com.ldz.wechat.exception.RuntimeCheck;
import com.ldz.wechat.module.bean.ClClyxjlModel;
import com.ldz.wechat.module.bean.DdClModel;
import com.ldz.wechat.module.mapper.ClXlMapper;
import com.ldz.wechat.module.mapper.ClXlzdMapper;
import com.ldz.wechat.module.mapper.ClZdMapper;
import com.ldz.wechat.module.model.ClClyxjl;
import com.ldz.wechat.module.model.ClXl;
import com.ldz.wechat.module.model.ClXlzd;
import com.ldz.wechat.module.model.ClZd;
import com.ldz.wechat.module.service.ClyxjlService;
import com.ldz.wechat.module.service.XlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


@Service("wxXlService")
public class XlServiceImpl extends BaseServiceImpl<ClXl, String> implements XlService {
	@Autowired
	private ClXlMapper entityMapper;
	@Autowired
	private ClXlzdMapper xlzdMapper;
	@Autowired
	private ClZdMapper zdMapper;
	@Autowired
	private ClyxjlService clyxjlService;

	@Override
	protected Mapper<ClXl> getBaseMapper() {
		return entityMapper;
	}

	@Override
	protected Class<?> getEntityCls() {
		return ClXl.class;
	}

   public List<ClXl> getAll(String lx){
	   RuntimeCheck.ifNull(lx,"线路类型不能为空，请核实！");
	   ClXl clXl=new ClXl();
	   clXl.setLx(lx);
	   clXl.setZt("00");
	   return findByEntity(clXl);
   }

	public ApiResponse<Map<String,Object>> getBySiteVehicleList(String xlId){
   		ClXl clXl=findById(xlId);
		RuntimeCheck.ifNull(clXl,"线路ID有误，请核实！");
		List<DdClModel> clZds=entityMapper.getBySiteVehicleList(xlId);
		if(clZds!=null){
			SimpleCondition condition = new SimpleCondition(ClClyxjl.class);
			Date today = new Date();
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);
			condition.gte(ClClyxjl.InnerColumn.cjsj,today);
			condition.eq(ClClyxjl.InnerColumn.xlId,xlId);
			condition.and().andNotEqualTo(ClClyxjl.InnerColumn.zt.name(),"off");
			condition.setOrderByClause(ClClyxjl.InnerColumn.cjsj.asc());
			List<ClClyxjl> xlzds = clyxjlService.findByCondition(condition);
			if(xlzds!=null&&xlzds.size()>0){
				for(DdClModel clZd:clZds){
					//到站车辆
					long entryCount=0;
					//出站车辆
					long exportCount=0;
					Iterator<ClClyxjl> iter = xlzds.iterator();
					if(clZd.getVehicleCount()>0){
						List<ClClyxjlModel> list=new ArrayList<ClClyxjlModel>();
						while (iter.hasNext()) {
							ClClyxjl item = iter.next();
							//判断车辆是否在当前站点
							if(StringUtils.equals(item.getZdId(),clZd.getZdId())){
								ClClyxjlModel model=new ClClyxjlModel();
								model.setCphm(item.getCphm());//车牌号码
								model.setCjsj(item.getCjsj());//创建时间
								//车辆出站状态  0未出站 1、已出站
								String clczzt="1";//
								if(item.getZdjl()!=null&& clZd.getVehicleScope()<item.getZdjl()){
									clczzt="0";
									entryCount++;
								}else{
									exportCount++;
								}
								model.setClczzt(clczzt);//车辆出站状态  0未出站
								model.setCjsj(item.getCjsj());
								model.setZdjl(item.getZdjl());
								list.add(model);
//								iter.remove();
							}
						}
						clZd.setVehicleList(list);
					}
					clZd.setEntryCount(entryCount);
					clZd.setExportCount(exportCount);
				}
			}
		}
		ApiResponse<Map<String,Object>> apiResponse=new ApiResponse<Map<String,Object>>();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("list",clZds);
		map.put("name",clXl.getXlmc());
		map.put("yxkssj",clXl.getYxkssj());
		map.put("yxjssj",clXl.getYxjssj());

		apiResponse.setResult(map);
		return apiResponse;
	}

	@Override
	public ApiResponse<List<Integer>> getNextCars(String xlId, String zdId) {
		RuntimeCheck.ifBlank(xlId,"参数错误，xlId");
		RuntimeCheck.ifBlank(zdId,"参数错误，zdId");
		ApiResponse<Map<String,Object>> carsRes = getBySiteVehicleList(xlId);
		Map<String,Object> res = carsRes.getResult();
		List<DdClModel> clZds = (List<DdClModel>) res.get("list");
		List<Map<String, Object>> result = new ArrayList<>();
		DdClModel currentZd = null;
		int currentZdIndex = 0;
		List<Integer> stationNumbers = new ArrayList<>();
		for (int i = clZds.size() - 1 ;i>=0;i--){
			DdClModel zd = clZds.get(i);
			if (currentZd == null){ // 查找当前站点
				if (!zdId.equals(zd.getZdId())){
					continue;
				}else{
					currentZdIndex = i;
					currentZd = zd;
				}
			}else{
				if (zd.getVehicleCount() > 0){
					stationNumbers.add(currentZdIndex - i);
				}
			}
		}
		return ApiResponse.success(stationNumbers);
	}

	@Override
	public List<ClZd> getStationList(String xlId) {
		SimpleCondition condition = new SimpleCondition(ClXlzd.class);
		condition.eq(ClXlzd.InnerColumn.xlId,xlId);
		List<ClXlzd> xlZds = xlzdMapper.selectByExample(condition);
		if (xlZds.size() == 0)new ArrayList<>();
		List<String> stationIds = xlZds.stream().map(ClXlzd::getZdId).collect(Collectors.toList());
		condition = new SimpleCondition(ClZd.class);
		condition.in(ClZd.InnerColumn.id,stationIds);
		List<ClZd> stations = zdMapper.selectByExample(condition);
		return stations;
	}

	@Override
	public ApiResponse<List<Map<String,Object>>> getStationGpsList(String xlId) {
		List<ClZd> stations = getStationList(xlId);
		if (stations.size() == 0){
			return ApiResponse.success(new ArrayList<>());
		}
		List<Map<String,Object>> list = new ArrayList<>(stations.size());
		for (ClZd station : stations) {
			Map<String,Object> map = new HashMap<>();
			map.put("jd",station.getJd());
			map.put("wd",station.getWd());
			list.add(map);
		}
		return ApiResponse.success(list);
	}

	@Override
	public ApiResponse<List<ClClyxjl>> getBusPositions(String xlId) {
		RuntimeCheck.ifBlank(xlId,"请输入线路");
		String today = DateUtils.getToday() + " 00:00:00";
		Date todayDate = null;
		try {
			todayDate = DateUtils.getDate(today,"yyyy-MM-dd hh:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleCondition condition = new SimpleCondition(ClClyxjl.class);
		if (!StringUtils.isEmpty(xlId)){
			condition.eq(ClClyxjl.InnerColumn.xlId,xlId);
		}
		condition.gte(ClClyxjl.InnerColumn.cjsj,todayDate);
		List<ClClyxjl> clClyxjls = clyxjlService.findByCondition(condition);
		return ApiResponse.success(clClyxjls);
	}
}
