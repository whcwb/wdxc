package com.ldz.service.biz.impl;

import com.ldz.dao.biz.bean.ClClModel;
import com.ldz.dao.biz.bean.SafedrivingModel;
import com.ldz.dao.biz.mapper.ClClMapper;
import com.ldz.dao.biz.model.*;
import com.ldz.service.biz.interfaces.ClService;
import com.ldz.service.biz.interfaces.ClyxjlService;
import com.ldz.service.biz.interfaces.JsyService;
import com.ldz.service.biz.interfaces.ZdglService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.model.SysJg;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.model.SysZdxm;
import com.ldz.sys.service.JgService;
import com.ldz.sys.service.ZdxmService;
import com.ldz.sys.util.RedisUtil;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.exception.RuntimeCheck;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ClServiceImpl extends BaseServiceImpl<ClCl, String> implements ClService {
	@Autowired
	private ClClMapper entityMapper;
	@Autowired
	private JgService jgService;
	@Autowired
	private ClService clService;
	@Autowired
	private JsyService jsyService;
	@Autowired
	private ZdxmService zdxmService;
	@Value("${znzpurl}")
	private String deleteZnzpRedisKeyUrl;
	@Autowired
	private ZdglService zdglService;
	@Autowired
	private ClyxjlService clyxjlService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	protected Mapper<ClCl> getBaseMapper() {
		return entityMapper;
	}

	@Override
	protected Class<?> getEntityCls() {
		return ClCl.class;
	}
//车辆删除后，同步移除
	@Override
	public void remove(String id) {
		int i=getBaseMapper().deleteByPrimaryKey(id);
		if(i==1){
//			移除 车辆运行记录表中的记录
			ClClyxjl clClyxjl=new ClClyxjl();
			clClyxjl.setClId(id);//车辆ID
			clyxjlService.remove(clClyxjl);
		}
	}

	@Override
	public void remove(ClCl entity) {
		int i=getBaseMapper().delete(entity);
		if(i==1){
//			移除 CL_CLYXJL
			ClClyxjl clClyxjl=new ClClyxjl();
			clClyxjl.setClId(entity.getClId());//车辆ID
			clyxjlService.remove(clClyxjl);
		}
	}

	@Override
	public ApiResponse<String> removeIds(String[] ids) {
		for (String id : ids) {
			int i=getBaseMapper().deleteByPrimaryKey(id);
			if(i==1){
//			移除 CL_CLYXJL
				ClClyxjl clClyxjl=new ClClyxjl();
				clClyxjl.setClId(id);//车辆ID
				clyxjlService.remove(clClyxjl);
			}
		}
		return ApiResponse.deleteSuccess();
	}

	@Override
	public ClCl findByOrgCode(String code) {
		List<ClCl> jgs = findEq(ClCl.InnerColumn.clId, code);
		if (jgs.size() == 0)
			return null;
		return jgs.get(0);
	}

	@Override
	public List<ClCl> getOrgCarList(String orgCode) {
		List<ClCl> carList = clService.findEq(ClCl.InnerColumn.jgdm, orgCode);
		return carList;
	}

	@Override
	public ApiResponse<String> saveEntity(ClCl entity) {
		SysYh user = getCurrentUser();
		SysJg org = jgService.findByOrgCode(user.getJgdm());
		Date now = new Date();
		entity.setCjr(getOperateUser());
		entity.setClId(genId());
		entity.setCjsj(now);
		entity.setJgdm(user.getJgdm());
		entity.setJgmc(org.getJgmc());
		if (StringUtils.isNotBlank(entity.getSjId())) {
			ClJsy jsy = jsyService.findById(entity.getSjId());
			entity.setSjxm(jsy.getXm());
		}

		SimpleCondition condition = new SimpleCondition(SysZdxm.class);
		condition.eq(SysZdxm.InnerColumn.zdlmdm, "ZDCLK0041");
		condition.eq(SysZdxm.InnerColumn.zddm, entity.getZkl());
		List<SysZdxm> zdxms = zdxmService.findByCondition(condition);
		if (CollectionUtils.isEmpty(zdxms)) {
			Short zkl = entity.getZkl();
			String zdCode = "ZDCLK0041";// 默认为载客量
			SysZdxm sysZdxm = new SysZdxm();
			sysZdxm.setZddm(zkl + "");// 字典代码不能为空
			sysZdxm.setZdlmdm(zdCode);// 字典类目代码不能为空
			sysZdxm.setZdmc(zkl + "");// 字典名称不能为空
			zdxmService.add(sysZdxm);
		}

		save(entity);
		redisUtil.deleteRedisKey(deleteZnzpRedisKeyUrl + "/deleteRedisKey","com.ldz.znzp.mapper.ClClMapper");
		return ApiResponse.saveSuccess();
	}

	@Override
	public ApiResponse<String> updateEntity(ClCl entity) {
		ClCl findById = findById(entity.getClId());
		RuntimeCheck.ifNull(findById, "未找到记录");
		entity.setXgr(getOperateUser());
		entity.setXgsj(new Date());
		update(entity);
		redisUtil.deleteRedisKey(deleteZnzpRedisKeyUrl + "/deleteRedisKey","com.ldz.znzp.mapper.ClClMapper");
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<List<Map<String, Object>>> getVehicleTypeStatistics(String zxzt) {
		// 1、定义初始变量
		ApiResponse<List<Map<String, Object>>> result = new ApiResponse<>();
		List<Map<String, Object>> carGroup = new ArrayList<>();
		SysYh user = getCurrentUser();
		String jgdm = user.getJgdm();// 机构ID
		zxzt = StringUtils.trim(zxzt);
		// 2、查询车辆信息
		List<ClClModel> carList = null;
		if (StringUtils.isNotEmpty(zxzt)) {
			carList = entityMapper.getVehicleTypeZxztStatistics(jgdm, zxzt);
		} else {
			carList = entityMapper.getVehicleTypeStatistics(jgdm);
		}

		if (carList.size() == 0){
			return result;
		}

		Map<String,List<ClClModel>> carMap = new HashMap<>();
		for (ClClModel model : carList) {
			String cx = model.getCxZtMc();
			if (!carMap.containsKey(cx)){
				List<ClClModel> modelList = new ArrayList<>();
				modelList.add(model);
				carMap.put(cx,modelList);
			}else{
				carMap.get(cx).add(model);
			}
		}

		for (Map.Entry<String, List<ClClModel>> entry : carMap.entrySet()) {
			String cx = entry.getKey();
			List<ClClModel> modelList = entry.getValue();
			Map<String, Object> group = new HashMap<>();
			List<Map<String,Object>> children = new ArrayList<>();
			group.put("title",cx);
			group.put("expand",true);
			group.put("children",children);
			carGroup.add(group);
			for (ClClModel model : modelList) {
				Map<String,Object> map = parseCarModel(model);
				children.add(map);
			}
		}
		result.setResult(carGroup);
		return result;
	}

	private Map<String,Object> parseCarModel(ClClModel model){
		Map<String,Object> map = new HashMap<>();
		map.put("title", model.getCph());
		map.put("clid", model.getClId());
		map.put("cx", model.getCx());
		Map<String,Object> point = new HashMap<>();
		point.put("lng",model.getBdWd());
		point.put("lat",model.getBdJd());
		map.put("mapCen",point);
		return map;
	}

	@Override
	public ApiResponse<List<ClCl>> nianshen(ClCl car) {
		ApiResponse<List<ClCl>> apiResponse = new ApiResponse<>();
		long now = new Date().getTime();

		LimitedCondition condition = new LimitedCondition(ClCl.class);
		if (StringUtils.isNotEmpty(car.getCph())){
			condition.like(ClCl.InnerColumn.cph,car.getCph());
		}
		List<ClCl> cllist = entityMapper.selectByExample(condition);
		List<ClCl> cls= new ArrayList<>();
		/*cllist.stream().filter(s -> s.getNssj() != null).filter(s -> (now-s.getNssj().getTime()) < a);*/
		// 年审时间正序
		cllist = cllist.stream().filter(p->p.getNssj() != null).collect(Collectors.toList());
		cllist.sort(Comparator.comparing(ClCl::getNssj));
		for (ClCl clCl : cllist) {
			if (clCl.getNssj()!=null) {
				long nianshen= clCl.getNssj().getTime();
				long time = (nianshen-now)/(24 * 60 * 60 * 1000);
				if (time<90) {
					cls.add(clCl);
				}
			}

		}
		apiResponse.setResult(cls);
		return apiResponse;
	}

	@Override
	public ApiResponse<Map<String, Object>> carAccStatistics(Integer days,String type) {
		SysYh user = getCurrentUser();
		String jgdm = user.getJgdm();
		Calendar now = Calendar.getInstance();
		if (days == null)days = -3;
		if (days > 0)days = -(days-1);
		now.add(Calendar.DATE, days);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		String weekStart = sdf.format(now.getTime());
		String dateRange = " and cjsj> to_date('"+weekStart+"','yyyy-mm-dd') ";
		String sql = "SELECT T1.sjxm,T1.cph,T2.speedupCount,t3.speedownCount,t4.wheelCount,t5.overspeedCount  FROM CL_CL t1 \n" +
				"  LEFT JOIN (select ZDBH ,count(SJLX) as speedupCount FROM CL_SBYXSJJL  t WHERE t.SJLX='10' "+dateRange+" GROUP BY zdbh) t2 on T1.ZDBH=T2.ZDBH\n" +
				"  LEFT JOIN (select ZDBH ,count(SJLX) as speedownCount FROM CL_SBYXSJJL  t WHERE t.SJLX='20' "+dateRange+"  GROUP BY zdbh) t3 on T1.ZDBH=T3.ZDBH\n" +
				"  LEFT JOIN (select ZDBH ,count(SJLX) as wheelCount FROM CL_SBYXSJJL  t WHERE t.SJLX='30' "+dateRange+"  GROUP BY zdbh) t4 on T1.ZDBH=T4.ZDBH\n" +
				"  LEFT JOIN (select ZDBH ,count(SJLX) as overspeedCount FROM CL_SBYXSJJL  t WHERE t.SJLX='40' "+dateRange+"  GROUP BY zdbh) t5 on T1.ZDBH=T5.ZDBH "+
				"  where t1.jgdm like '"+jgdm+"%' ";
		List result = jdbcTemplate.queryForList(sql);
		List<SafedrivingModel> list = new ArrayList<>(result.size());
		for (Object o : result) {
			Map<String,Object> map = (Map<String, Object>) o;
			SafedrivingModel model = new SafedrivingModel();
			model.setCph((String) map.get("cph"));
			model.setOverspeedCount(map.get("overspeedCount") == null ? 0 : Integer.parseInt(map.get("overspeedCount").toString()));
			model.setSpeedownCount(map.get("speedownCount") == null ? 0 : Integer.parseInt(map.get("speedownCount").toString()));
			model.setSpeedupCount(map.get("speedupCount") == null ? 0 : Integer.parseInt(map.get("speedupCount").toString()));
			model.setWheelCount(map.get("wheelCount") == null ? 0 : Integer.parseInt(map.get("wheelCount").toString()));
			model.setSjxm((String) map.get("sjxm"));

			if ("aqjs".equals(type)){
				int total = model.getSpeedupCount() +
						model.getSpeedupCount() +
						model.getWheelCount() +
						model.getOverspeedCount();
				model.setTotal(total);
			}
			list.add(model);
		}

		if ("aqjs".equals(type)){
			list.sort(Comparator.comparingInt(SafedrivingModel::getTotal).reversed());
			if (list.size()>10){
				list = list.subList(0,10);
			}
		}else if ("cstj".equals(type)){
			list.sort(Comparator.comparingInt(SafedrivingModel::getOverspeedCount).reversed());
			if (list.size()>10){
				list = list.subList(0,10);
			}
		}

		List<String> carNumberList = new ArrayList<>(list.size());
		List<Object> speedUpCountList = new ArrayList<>(list.size());
		List<Object> speedDownCountList = new ArrayList<>(list.size());
		List<Object> wheelCountList = new ArrayList<>(list.size());
		List<Object> overSpeedCountList = new ArrayList<>(list.size());
		List<String> driverNames = new ArrayList<>(list.size());
		for (SafedrivingModel model : list) {
			String carNumber = model.getCph();
			String driverName = model.getSjxm();
			carNumberList.add(carNumber);
			driverNames.add(driverName);

			speedUpCountList.add(model.getSpeedupCount());
			speedDownCountList.add(model.getSpeedownCount());
			wheelCountList.add(model.getWheelCount());
			overSpeedCountList.add(model.getOverspeedCount());
		}

		Map<String,Object> map = new HashMap<>();

		Map<String,Object> speedUpMap = new HashMap<>();
		speedUpMap.put("name","急加速");
		speedUpMap.put("yAxis",speedUpCountList);

		Map<String,Object> wheelMap = new HashMap<>();
		wheelMap.put("name","急转弯");
		wheelMap.put("yAxis",wheelCountList);


		Map<String,Object> breakMap = new HashMap<>();
		breakMap.put("name","急刹车");
		breakMap.put("yAxis",speedDownCountList);


		Map<String,Object> overSpeedMap = new HashMap<>();
		overSpeedMap.put("name","超速");
		overSpeedMap.put("yAxis",overSpeedCountList);

		map.put("driverNames",driverNames);
		map.put("carNumbers",carNumberList);
		map.put("xAxis",carNumberList);
		map.put("speedUpMap",speedUpMap);
		map.put("wheelMap",wheelMap);
		map.put("breakMap",breakMap);
		map.put("overSpeedMap",overSpeedMap);
		return ApiResponse.success(map);
	}


	private static String convertWeekDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了

		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天

		if (1 == dayWeek) {

			cal.add(Calendar.DAY_OF_MONTH, -1);

		}

		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天

		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		return sdf.format(cal.getTime()); // 周一时间

	}

	public static void main(String[] args) {

	}
	@Override
	public ApiResponse<Map<String, Integer>> getnianshen() {
		//获取当前登陆用户
		SysYh user = getCurrentUser();
		ClCl clCl= new ClCl();
		clCl.setJgdm(user.getJgdm());

		List<ClCl> cllist = entityMapper.select(clCl);


		int thirty=0;
		int sixty=0;
		int ninety=0;
		Date now = new Date();
		for (ClCl cl : cllist) {
			if (cl.getNssj()==null) {
				continue;
			}
			Date nssj = cl.getNssj();
			int cha = differentDaysByMillisecond(now,nssj);
			if (cha<=30) {
				thirty++;
			}
			if (cha>30&&cha<=60) {
				sixty++;
			}
			if (cha>60&&cha<=90) {
				ninety++;
			}

		}
		ApiResponse<Map<String, Integer>> apiResponse = new ApiResponse<>();
		Map<String, Integer> map = new HashMap<>();
		map.put("30", thirty);
		map.put("60", sixty);
		map.put("90", ninety);
		apiResponse.setResult(map);
		return apiResponse;
	}

	@Override
	public ApiResponse<String> unbindDevice(String carId) {
		RuntimeCheck.ifBlank(carId,"请选择车辆");
		ClCl car = entityMapper.selectByPrimaryKey(carId);
		RuntimeCheck.ifNull(car,"未找到车辆");
		car.setZdbh(null);
		entityMapper.updateByPrimaryKey(car);
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<String> unbindDriver(String carId) {
		RuntimeCheck.ifBlank(carId,"请选择车辆");
		ClCl car = entityMapper.selectByPrimaryKey(carId);
		RuntimeCheck.ifNull(car,"未找到车辆");
		car.setSjxm(null);
		car.setSjId(null);
		entityMapper.updateByPrimaryKey(car);
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<String> bindDriver(String carId, String driverId) {
		RuntimeCheck.ifBlank(carId,"请选择车辆");
		RuntimeCheck.ifBlank(driverId,"请选择驾驶员");
		ClCl car = entityMapper.selectByPrimaryKey(carId);
		RuntimeCheck.ifNull(car,"未找到车辆");
		ClJsy driver = jsyService.findById(driverId);
		RuntimeCheck.ifNull(driver,"未找到驾驶员");

		car.setSjxm(driver.getXm());
		car.setSjId(driverId);
		entityMapper.updateByPrimaryKeySelective(car);
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<String> bindDevice(String carId, String devcieId) {
		RuntimeCheck.ifBlank(carId,"请选择车辆");
		RuntimeCheck.ifBlank(devcieId,"请选择终端");
		ClCl car = entityMapper.selectByPrimaryKey(carId);
		RuntimeCheck.ifNull(car,"未找到车辆");
		ClZdgl device = zdglService.findById(devcieId);
		RuntimeCheck.ifNull(device,"未找到终端");

		List<ClCl> clCls = clService.findEq(ClCl.InnerColumn.zdbh, devcieId);
		RuntimeCheck.ifTrue(CollectionUtils.isNotEmpty(clCls), "此终端已经绑定其他车辆, 请勿重复绑定");
		car.setZdbh(devcieId);
		entityMapper.updateByPrimaryKeySelective(car);
		return ApiResponse.success();
	}

	public static int differentDaysByMillisecond(Date date1,Date date2)
	    {
	        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
	        return days;
	    }

}
