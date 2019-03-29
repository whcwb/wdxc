package com.ldz.service.biz.impl;

import com.ldz.dao.biz.bean.*;
import com.ldz.dao.biz.mapper.*;
import com.ldz.dao.biz.model.*;
import com.ldz.service.biz.interfaces.DdService;
import com.ldz.service.biz.interfaces.DdrzService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.mapper.SysHsgsMapper;
import com.ldz.sys.mapper.SysRlbMapper;
import com.ldz.sys.model.*;
import com.ldz.sys.service.JgService;
import com.ldz.sys.service.SysMessageService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.JsonUtil;
import com.ldz.util.commonUtil.MathUtil;
import com.ldz.util.exception.RuntimeCheck;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.common.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class DdServiceImpl extends BaseServiceImpl<ClDd, String> implements DdService {
	//
	@Autowired
	private SysMessageService sysMessageService;

	@Autowired
	private ClDdMapper entityMapper;
	@Autowired
	private JgService jgService;
	@Autowired
	private SysRlbMapper rlbMapper;
	/*
	 * @Autowired private ClService clService;
	 */
	@Autowired
	private DdrzService ddrzService;

	@Autowired
	private ClDdrzMapper ddrzMapper;// 历史订单明细表
	@Autowired
	private ClDdlsbMapper ddlsbMapper;// 订单历史表(订单原始单据表)
	@Autowired
	private ClGpsLsMapper clGpsLsMapper;// 车辆GPS列表
	@Autowired
	private ClJsyMapper clJsyMapper;// 驾驶员表
	@Autowired
	private ClClMapper clClMapper;// 车辆表
	@Autowired
	private ClLscMapper clLscMapper;// 临时车表
	@Autowired
	private ClCdMapper clCdMapper;// 车队表
	@Autowired
	private SysHsgsMapper hsgsMapper;

	@Override
	protected Mapper<ClDd> getBaseMapper() {
		return entityMapper;
	}

	/**
	 * 订单查询，所有的数据必须查询本人机构下的订单
	 *
	 * @param condition
	 * @return
	 */
	@Override
	public boolean fillCondition(LimitedCondition condition) {
		SysYh user = getCurrentUser(true);// true
		String jgdm = user.getJgdm();
		condition.and().andLike("jgdm", jgdm + "%");

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String aaa = (String) request.getAttribute("cph");
		return true;
	}

	@Override
	public ApiResponse<String> saveEntity(ClDd entity, SysYh user) {
		String userId = user.getYhid();

		RuntimeCheck.ifBlank(entity.getJgdm(), "请选择用车单位");
		RuntimeCheck.ifFalse(entity.getJgdm().indexOf(user.getJgdm()) == 0, "您不能为非本机构员工创建订单");
		SysJg org = jgService.findByOrgCode(entity.getJgdm());
		RuntimeCheck.ifNull(org, "当前选择的用车单位有误，请核实！");
		RuntimeCheck.ifBlank(entity.getHcdz(), "候车地址不能为空");
		RuntimeCheck.ifBlank(entity.getMdd(), "目的地不能为空");
		RuntimeCheck.ifBlank(entity.getCllx(), "车辆车型不能为空");
		RuntimeCheck.ifNull(entity.getZws(), "乘客人数不能为空");
		RuntimeCheck.ifNull(entity.getYysj(), "乘客预车时间不能为空");
		RuntimeCheck.ifBlank(entity.getCk(), "乘客姓名不能为空");
		RuntimeCheck.ifBlank(entity.getCklxdh(), "乘客联系电话不能为空");

//		手机上一定会有，WEB端不一定会有
//		RuntimeCheck.ifFalse(entity.getOriginLat()!=null, "起始纬度不能为空");
//		RuntimeCheck.ifFalse(entity.getOriginLng()!=null, "起始经度不能为空");
//		RuntimeCheck.ifFalse(entity.getDestinationLat()!=null, "结束点纬度不能为空");
//		RuntimeCheck.ifFalse(entity.getDestinationLng()!=null, "结束点经度不能为空");


		String orderId = genId();
		entity.setId(orderId);
		entity.setCjr(userId);
		entity.setJgdm(org.getJgdm());
		entity.setJgmc(org.getJgmc());
		entity.setCjsj(new Date());
		entity.setFkzt("00"); // 未付款
		entity.setDdzt("10");// 10-订单创建；11-订单确认；12-订单驳回；13-已派单；20-司机确认(出车)；21-司机完成行程(行程结束)；30-队长确认
		int i = entityMapper.insertSelective(entity);
		RuntimeCheck.ifTrue(i == 0, "订单入库失败");


		ddrzService.log(entity);
		return ApiResponse.success();
	}

	/**
	 * 订单审核操作
	 *
	 * @param entity
	 * @return
	 */
	public ApiResponse<String> updateOrderAuditing(ClDd entity) {
		SysYh user = getCurrentUser(true);
		String userId = user.getYhid();

		// 订单状态入参验证 验证入参"ddzt"必须是11 或者 12
		Boolean orderTypeValid = true;
		orderTypeValid = StringUtils.equals(entity.getDdzt(), "11");
		if (!orderTypeValid) {
			orderTypeValid = StringUtils.equals(entity.getDdzt(), "12");
			RuntimeCheck.ifFalse(orderTypeValid, "设置订单状态有误");
		}

		RuntimeCheck.ifTrue(StringUtils.equals(entity.getDdzt(), "12") && StringUtils.isEmpty(entity.getBhyy()),
				"驳回原因不能为空");

		ClDd clDd = findById(entity.getId());
		RuntimeCheck.ifNull(clDd, "未找到订单记录");

		RuntimeCheck.ifFalse(clDd.getJgdm().indexOf(user.getJgdm()) == 0, "您不能对非本机构订单进行操作");

		String oracleType = clDd.getDdzt();// 订单状态
		RuntimeCheck.ifFalse(StringUtils.equals(oracleType, "10"), "当前订单不是待审核状态，无法进行该操作");

		ClDd ent = new ClDd();
		ent.setId(entity.getId());
		ent.setXgr(userId);// 修改人
		ent.setXgsj(new Date());// 修改时间
		ent.setDdzt(entity.getDdzt());// 订单状态
		ent.setShsj(new Date());// 审核时间
		int i = update(entity);// 根据主键更新属性不为null的值
		RuntimeCheck.ifTrue(i == 0, "订单审核操作失败");
		ddrzService.log(entity);
		return ApiResponse.success();
	}

	/**
	 * 订单查询页面
	 *
	 * @param entity
	 *            1、id 订单ID 必填
	 * @return
	 *
	 * 		oracleLog 订单日志表 gpsLog 车辆GPS列表 initialOracle 原始单据
	 */
	public ApiResponse<Map<String, Object>> orderdDetails(ClDd entity) {
		// 1、定义初始变量
		Map<String, Object> rMap = new HashMap<String, Object>();
		ApiResponse<Map<String, Object>> result = new ApiResponse<Map<String, Object>>();
		// 2、验证订单的有效性
		ClDd clDd = findById(entity.getId());
		RuntimeCheck.ifNull(clDd, "未找到订单记录");
		// 3、查询订单日志表
		List<ClDdrz> oracleLog = ddrzService.getOrderList(clDd.getId());
		if (oracleLog != null) {
			rMap.put("oracleLog", oracleLog);
		}

		if (clDd.getSjqrsj() != null) {
			rMap.put("sjqrsj", clDd.getSjqrsj());
			// 4、车辆GPS列表
			SimpleCondition condition = new SimpleCondition(ClGpsLs.class);
			condition.gte(ClGpsLs.InnerColumn.cjsj, clDd.getYysj());// 开始时间
			condition.lte(ClGpsLs.InnerColumn.cjsj, clDd.getSjqrsj());// 结束时间
			condition.eq(ClGpsLs.InnerColumn.zdbh, clDd.getZdbm());// 终端编码
			condition.setOrderByClause(ClGps.InnerColumn.cjsj.asc());// 创建时间
			List<ClGpsLs> gpsLog = clGpsLsMapper.selectByExample(condition);
			rMap.put("gpsLog", gpsLog);
		} else {
			rMap.put("gpsLog", new ArrayList());
			rMap.put("sjqrsj", null);
		}

		// 5、原始单据信息
		SimpleCondition condition = new SimpleCondition(ClDdlsb.class);
		condition.eq(ClDdlsb.InnerColumn.ddId, entity.getId());
		List<ClDdlsb> initialOracle = ddlsbMapper.selectByExample(condition);
		if (initialOracle != null) {
			rMap.put("initialOracle", initialOracle);
		}
		result.setResult(rMap);
		return result;
	}

	/**
	 * 待分配订单 查询订单状态为
	 *
	 * @param entity
	 * @return
	 */
	public ApiResponse<List<ClDd>> assignedOrderd(ClDd entity) {
		ApiResponse<List<ClDd>> result = new ApiResponse<List<ClDd>>();
		SimpleCondition condition = new SimpleCondition(ClDd.class);
		// 是否用乘客做查询条件了。
		if (StringUtils.isNotBlank(entity.getCk())) {
			condition.like(ClDd.InnerColumn.ck, entity.getCk());
		}
		String cllx = StringUtils.trim(entity.getCllx());
		RuntimeCheck.ifTrue(StringUtils.isBlank(cllx), "请填写车辆类型");
		// 车辆类型 车辆类型 10、小车 20、大车 30、校巴
		if (StringUtils.equals(cllx, "2030")) {
			List<String> li = new ArrayList<String>();
			li.add("20");
			li.add("30");
			condition.in(ClDd.InnerColumn.cllx, li);
		} else {
			condition.eq(ClDd.InnerColumn.cllx, entity.getCllx());
		}
		String zkl = StringUtils.trim(entity.getZkl());// 载客量
		if (StringUtils.isNotEmpty(zkl)) {
			condition.lte(ClDd.InnerColumn.zws, zkl);// zws
		}
		condition.eq(ClDd.InnerColumn.ddzt, "11");// TODO: 2018/3/18 这里的是否需要设置为常量？
		condition.setOrderByClause(ClDd.InnerColumn.yysj.desc() + " , " + ClDd.InnerColumn.zws.asc());
		// 这里还需要判断
		List<ClDd> orgs = findByCondition(condition);
		result.setResult(orgs);
		return result;
	}

	/**
	 * 运输中心-司机列表 派单司机列表
	 *
	 * @param entity
	 *            1、司机名 xm like 查询
	 *            2、驾驶员车型 zjcx 车辆类型 10、小车 20、大车 30、校巴 0203是查询大车、校巴  40 是外部车
	 *            3、载客量 zkl
	 * @return
	 */
	public ApiResponse<List<ClJsyModel>> driverList(ClJsy entity) {
		ApiResponse<List<ClJsyModel>> result = new ApiResponse<List<ClJsyModel>>();
		// 验证一个司机，只能绑定一个车辆，如果一个司机没有绑定或者绑定数超过的话，就不显示。
		// 1、排班表中该司机ID没有排班的，2、司机要与车辆有关联绑定
		String cllx = entity.getZjcx();
		RuntimeCheck.ifTrue(StringUtils.isBlank(cllx), "请填写车辆类型");
		// 车辆类型 车辆类型 10、小车 20、大车 30、校巴
		List<String> li = new ArrayList<String>();
		if (cllx.indexOf("2030")>-1) {
			li.add("20");
			li.add("30");
		} else {
			li.add(cllx);
		}
		String zkl = StringUtils.trim(entity.getZkl());
		zkl = StringUtils.isEmpty(zkl) ? null : zkl;
		List<ClJsyModel> list = clJsyMapper.getDispatchDriver(entity.getXm(), li, zkl);

		ClDd parameters = new ClDd();// 入参
		parameters.setSjSx("10");// 默认身份证号码
		if (list != null) {
			for (ClJsyModel obj : list) {
				parameters.setSj(obj.getSfzhm());// 设置身份证号码
				ApiResponse<List<ClDd>> retObject = affirmOrderList(parameters);
				if (retObject.isSuccess()) {
					List<ClDd> clDdList = retObject.getResult();
					obj.setClDdList(clDdList);
				}
			}
		} else {
			list = new ArrayList<ClJsyModel>();
		}
		//如果需要查外部车，就需要转40接口
		if(cllx.indexOf("40")>-1){
			SimpleCondition condition = new SimpleCondition(ClLsc.class);
			condition.eq(ClLsc.InnerColumn.zt, "00");
			List<ClLsc> lscList = clLscMapper.selectByExample(condition);
			parameters = new ClDd();// 入参
			parameters.setSjSx("11");// 默认身份证号码
			if(lscList!=null&& lscList.size()>0){
				for(ClLsc l:lscList){
					ClJsyModel obj=new ClJsyModel();
					obj.setCph(l.getCph());//车牌号
					obj.setZkl(l.getZws()+"");//坐位数
					obj.setXm(l.getLsdwmc());//临时车

					//设置订单列表
					parameters.setCph(l.getCph());// 设置车牌号
					ApiResponse<List<ClDd>> retObject = affirmOrderList(parameters);
					if (retObject.isSuccess()) {
						List<ClDd> clDdList = retObject.getResult();
						obj.setClDdList(clDdList);
					}
					list.add(obj);
				}
			}
		}
		result.setResult(list);
		return result;

	}

	/**
	 * 已分配订单查询
	 *
	 * @param entity
	 *            1、司机属性 sjSx 司机属性：10:内部司机，关联CL_JSY表 11：外部车，关联临时车表 2、驾驶员ID(驾驶员证件号)
	 *            SJ 当为内部车时，该值必填 3、车牌号 cph 当为外部车时，该值必填
	 * @return 订单列表
	 */
	public ApiResponse<List<ClDd>> affirmOrderList(ClDd entity) {
		ApiResponse<List<ClDd>> result = new ApiResponse<List<ClDd>>();
		SimpleCondition condition = new SimpleCondition(ClDd.class);

		if (StringUtils.equals(entity.getSjSx(), "10")) {
			String sj = StringUtils.trimToNull(entity.getSj());
			RuntimeCheck.ifNull(sj, "入参错误，内部车请传入驾驶员ID");
			condition.eq(ClDd.InnerColumn.sj, sj);
		} else if (StringUtils.equals(entity.getSjSx(), "11")) {
			String cph = StringUtils.trimToNull(entity.getCph());
			RuntimeCheck.ifNull(cph, "入参错误，外部车请传入车辆号");
			condition.eq(ClDd.InnerColumn.cph, cph);
		} else {
			RuntimeCheck.ifNull(null, "入参错误，请核实");
		}
		condition.eq(ClDd.InnerColumn.ddzt, "13");// TODO: 2018/3/18 这里的是否需要设置为常量？
		condition.setOrderByClause(ClDd.InnerColumn.yysj.desc());
		List<ClDd> orgs = findByCondition(condition);
		result.setResult(orgs);
		return result;
	}

	/**
	 * 派单操作 请求方式为post 1、验证订单ID是否存在 2、验证该订单是否处于：待派单状态 3、验证该订单是分配给内部（司机证件号） 还是外部 (车牌号)
	 * 3-1、验证司机证件号、车牌号的准备性 3-2、通过订单id修改派单操作 4、写入日志表。
	 *
	 * @param entity
	 *            1、订单id id 必填 2、司机属性 sjSx 司机属性：10:内部司机，关联CL_JSY表 11：外部车，关联临时车表
	 *            3、驾驶员ID(驾驶员证件号) SJ 当为内部车时，该值必填 4、车牌号 cph 当为外部车时，该值必填
	 * @return 成功或失败提示
	 */
	public ApiResponse<String> updateAssignOrder(ClDd entity) {
		SysYh user = getCurrentUser();
		String userId = user.getYhid();
		ClDd newClDd = new ClDd();
		String sjId = null;
		Map<String, Object> mapBody = null;
		// 1、查找该ID是否存在
		ClDd clDd = findById(entity.getId());
		RuntimeCheck.ifNull(clDd, "未找到订单记录");
		RuntimeCheck.ifFalse(clDd.getJgdm().indexOf(user.getJgdm()) == 0, "您不能对非本机构订单进行派单操作");

		// 2、验证当前状态必须是 11-订单确认状态
		String ddzt = clDd.getDdzt();
		RuntimeCheck.ifFalse(StringUtils.equals(ddzt, "11"), "订单没有处理待派单状态，不能进行派单操作");
		// 3、验证司机属性
		if (StringUtils.equals(entity.getSjSx(), "10")) {
			// 3-1、验证该司机是否存在
			ClJsy jsy = clJsyMapper.selectByPrimaryKey(entity.getSj());
			RuntimeCheck.ifNull(jsy, "未找到司机记录");
			// 3-2、验证该司机当前是否处于休息状态
			RuntimeCheck.ifFalse(StringUtils.equals(jsy.getZt(), "00"), "该司机正处理休息状态，不能进行派单操作");

			// 3-3、查询车辆表，检查出该司机对应的车辆的信息

			SimpleCondition condition = new SimpleCondition(ClCl.class);
			condition.eq(ClCl.InnerColumn.sjId, entity.getSj());

			List<ClCl> cl = clClMapper.selectByExample(condition);
			RuntimeCheck.ifEmpty(cl, "该司机未关联车辆，不能进行派单操作");

			ClCl clCl = cl.get(0);
			RuntimeCheck.ifNull(clCl, "该司机未关联车辆，不能进行派单操作");

			Short zkl = clCl.getZkl();//
			RuntimeCheck.ifNull(zkl, "，车辆" + clCl.getCph() + "载客量为空，不能进行派单操作");
			Short zws = clDd.getZws();
			RuntimeCheck.ifNull(zws, "该订单座位数为空，不能进行派单操作");

			RuntimeCheck.ifTrue(zws > zkl, "该车的载客量为:" + zkl + " 订单乘客人数:" + zws + ",不能进行派单操作");

			ClCd clcd = clCdMapper.selectByPrimaryKey(clCl.getCdbh());
			RuntimeCheck.ifNull(clcd, "未找到该车辆所关联的车队记录");

			// 3-4、对要修改的字段进行给值
			newClDd.setSjSx("10");// 司机属性 内部司机、外部司机
			newClDd.setSj(jsy.getSfzhm());// 司机
			newClDd.setSjxm(jsy.getXm());// 司机姓名
			newClDd.setClId(clCl.getClId());// 车辆ID
			newClDd.setCph(clCl.getCph());// 车牌号
			// newClDd.setZws((short) 0);//座位数
			newClDd.setDzbh(clcd.getDzbh());// 队长编号
			newClDd.setCdbh(clcd.getCdbh());// 车队编号
			newClDd.setZdbm(clCl.getZdbh());// 终端编号
			sjId = jsy.getSjh();//

			mapBody = new HashMap<String, Object>();
			mapBody.put("ck", clDd.getCk());// 乘客名称
			mapBody.put("cklxdh", clDd.getCklxdh());// 乘客联系电话
			mapBody.put("hcdz", clDd.getHcdz());// 候车地址
			mapBody.put("mdd", clDd.getMdd());// 目的地
			mapBody.put("yysj", clDd.getYysj());// 获取预约时间

		} else if (StringUtils.equals(entity.getSjSx(), "11")) {

			// 3-5、通过车牌号码查询临时车表，验证该车牌的正确性。
			SimpleCondition condition = new SimpleCondition(ClLsc.class);
			condition.eq(ClLsc.InnerColumn.cph, entity.getCph());
			List<ClLsc> lscList = clLscMapper.selectByExample(condition);
			RuntimeCheck.ifEmpty(lscList, "未找到该车牌记录，不能进行派单操作");
			ClLsc clLsc = lscList.get(0);
			RuntimeCheck.ifNull(clLsc, "未找到该车牌记录，不能进行派单操作");

			// 3-6、通过车牌号码查询临时车表，验证该车牌的正确性。
			newClDd.setCph(clLsc.getCph());// 车牌号
			// newClDd.setSj("");//司机
			// newClDd.setSjxm("");//司机姓名
			newClDd.setZws(clLsc.getZws());// 座位数
			newClDd.setClId(clLsc.getClId());// 车辆ID
			newClDd.setSjSx("11");// 司机属性 内部司机、外部司机
		} else {
			RuntimeCheck.ifNull(null, "司机属性入参错误，请核实");
		}

		newClDd.setDdzt("13");// 订单状态
		newClDd.setId(clDd.getId());

		int i = update(newClDd);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "派单操作数据库失败");
		}

		ClDdrz clDdrz = new ClDdrz();
		clDdrz.setId(genId());// 主键ID
		clDdrz.setDdId(clDd.getId());// 订单ID
		clDdrz.setCjsj(new Date());// 创建时间
		clDdrz.setCjr(userId);// 创建人
		clDdrz.setJgdm(clDd.getJgdm());// 机构代码
		clDdrz.setJgmc(clDd.getJgmc());// 机构名称
		clDdrz.setDdzt(newClDd.getDdzt());// 订单状态
		i = ddrzMapper.insertSelective(clDdrz);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "创建订单历史表失败");
			return ApiResponse.error();
		} else {
			if (StringUtils.isNotEmpty(sjId) && mapBody != null) {
				String title = "司机派单";// 标题
				String sendCode = sjId;// 接收方编号
				String message = JsonUtil.toJson(mapBody);// 接收方报文 mapBody
				String bizId = "BIZ_10";// 业务编号
				long type = 1;// 1、短信
				// TODO: 2018/5/9 插入系统消息表
				// todo 羊哥代码没有提交完 SysMessage 没有
				SysMessage sysMessage = new SysMessage();
				sysMessage.setMessage(message);//
				sysMessage.setType(type + "");//
				sysMessage.setTitle(title);
				sysMessage.setSendeeCode(sendCode);
				sysMessage.setBizId(bizId);
				sysMessageService.add(sysMessage);

			}
			return ApiResponse.success();
		}
	}

	/**
	 * 取消派单操作 请求方式为post 1、验证订单ID是否存在 2、验证该订单是否处于：已派单状态 3、将订单状态修改为待派单状态 4、写入日志表。
	 *
	 * @param entity
	 *            1、订单id id 必填
	 * @return 成功或失败提示
	 */
	public ApiResponse<String> updateCancelAssignOrder(ClDd entity) {
		SysYh user = getCurrentUser();
		String userId = user.getYhid();

		ClDd newClDd = new ClDd();

		// 1、查找该ID是否存在
		ClDd clDd = findById(entity.getId());
		RuntimeCheck.ifNull(clDd, "未找到订单记录");
		RuntimeCheck.ifFalse(clDd.getJgdm().indexOf(user.getJgdm()) == 0, "您不能对非本机构订单进行取消派单操作");
		// 2、验证当前状态必须是 11-订单确认状态
		String ddzt = clDd.getDdzt();
		RuntimeCheck.ifFalse(StringUtils.equals(ddzt, "13"), "订单没有处于已派单状态，不能进行取消派单操作");

		newClDd.setCph("");// 车牌号
		newClDd.setSj("");// 司机
		newClDd.setSjxm("");// 司机姓名
		// newClDd.setZws("");//座位数
		newClDd.setClId("");// 车辆ID
		newClDd.setSjSx("");// 司机属性 内部司机、外部司机
		newClDd.setZdbm("");// 终端编号
		newClDd.setDdzt("11");// 订单状态
		newClDd.setId(clDd.getId());
		int i = update(newClDd);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "操作数据库失败");
			return ApiResponse.fail("操作数据库失败");
		}

		ClDdrz clDdrz = new ClDdrz();
		clDdrz.setId(genId());// 主键ID
		clDdrz.setDdId(clDd.getId());// 订单ID
		clDdrz.setCjsj(new Date());// 创建时间
		clDdrz.setCjr(userId);// 创建人
		clDdrz.setJgdm(clDd.getJgdm());// 机构代码
		clDdrz.setJgmc(clDd.getJgmc());// 机构名称
		clDdrz.setDdzt(newClDd.getDdzt());// 订单状态
		i = ddrzMapper.insertSelective(clDdrz);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "创建订单历史表失败");
			return ApiResponse.error();
		} else {
			return ApiResponse.success();
		}

	}

	/**
	 * 订单编辑-订单确认 1、订单处于：司机确认(行程结束) 2、只有该队队长才能有限制
	 *
	 * ---------------20180506修改如下------------- 1、订单处理13（派单）、20（司机确认）后都可以修改
	 * 2、司机、队长、su(超级管理员)可以进行修改
	 *
	 * @param entity
	 * @return
	 */
	public ApiResponse<String> updateOrder(ClDd entity) {
		SysYh user = getCurrentUser();
		String userId = user.getYhid();

		// 1、查找该ID是否存在
		ClDd clDd = findById(entity.getId());
		RuntimeCheck.ifNull(clDd, "未找到订单记录");
		RuntimeCheck.ifFalse(clDd.getJgdm().indexOf(user.getJgdm()) == 0, "您不能对非本机构订单进行操作");
		// 2、验证当前状态必须是 11-订单确认状态
		// 订单状态 10-订单创建；11-订单确认(待派单)；12-订单驳回；13-已派单；20-司机确认(行程结束)；30-队长确认; 40-财务已收
		String ddzt = clDd.getDdzt();
		if (StringUtils.equals(ddzt, "13")) {
			// RuntimeCheck.ifFalse(StringUtils.equals(clDd.getSj(),userId)||StringUtils.equals(user.getLx(),"su"),"订单不属于本人，不能进行编辑操作");
		} else if (StringUtils.equals(ddzt, "20")) {
			// RuntimeCheck.ifFalse(StringUtils.equals(clDd.getDzbh(),userId)||StringUtils.equals(user.getLx(),"su"),"订单不属于本人，不能进行编辑操作");

			RuntimeCheck.ifNull(entity.getZj(), "订单总价不能为空");
			RuntimeCheck.ifNull(entity.getZj() > 0, "订单总价不能为空");
		} else {
			RuntimeCheck.ifTrue(true, "订单状态异常，不能进行订单编辑操作");
		}

		ClDd newClDd = new ClDd();
		newClDd.setId(clDd.getId());
		newClDd.setSj(userId);// 修改人
		newClDd.setXgsj(new Date());// 修改时间
		newClDd.setGlf(entity.getGlf());// 过路费
		newClDd.setGqf(entity.getGqf());//过桥费
		newClDd.setSy(entity.getSy());// 事由
		newClDd.setZj(entity.getZj());// 总价
		newClDd.setSc(entity.getSc());// 时长
		newClDd.setDj(entity.getDj());// 单价
		newClDd.setLc(entity.getLc());// 里程
		newClDd.setScf(entity.getScf());// 时长费
		newClDd.setLcf(entity.getLcf());// 里程费
		newClDd.setFkzt("00"); // 未付款
		newClDd.setYysj(entity.getYysj());
		newClDd.setSjqrsj(entity.getSjqrsj());

		int i = update(newClDd);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "修改订单失败");
			return ApiResponse.fail("操作数据库失败");
		} else {
			return ApiResponse.success();
		}
	}

	/**
	 * 订单确认 操作 1、订单处于：司机确认(行程结束) 2、只有该队队长才能有限制 3、修改订单状态为 队长确认 4、复制订单表到原始订单表中
	 *
	 * @param entity
	 *            订单ID 必填
	 * @return 成功与否
	 */
	public ApiResponse<String> updateAffirmOracle(ClDd entity) {
		SysYh user = getCurrentUser();
		String userId = user.getYhid();

		// 1、查找该ID是否存在
		ClDd clDd = findById(entity.getId());
		RuntimeCheck.ifNull(clDd, "未找到订单记录");
		RuntimeCheck.ifFalse(clDd.getJgdm().indexOf(user.getJgdm()) == 0, "您不能对非本机构订单进行操作");
		//// 2、验证当前状态必须是 11-订单确认状态
		// String ddzt=clDd.getDdzt();
		// RuntimeCheck.ifFalse(StringUtils.equals(ddzt,"20"),"订单没有处理司机
		//// 确认状态，不能进行队长确认操作");
		// RuntimeCheck.ifFalse(StringUtils.equals(clDd.getDzbh(),userId),"订单不属于本人，不能进行队长确认操作");

		RuntimeCheck.ifNull(clDd.getZj(), "订单总价不能为空");
		RuntimeCheck.ifNull(clDd.getZj() > 0, "订单总价不能为空");

		ClDd newClDd = new ClDd();
		newClDd.setId(clDd.getId());
		newClDd.setDdzt("30");// 订单状态
		int i = update(newClDd);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "操作数据库失败");
		}

		// 需要将订单表明细复制到原始单据表中。
		entityMapper.insertCopyOrder(clDd.getId(), userId);

		ClDdrz clDdrz = new ClDdrz();
		clDdrz.setId(genId());// 主键ID
		clDdrz.setDdId(clDd.getId());// 订单ID
		clDdrz.setCjsj(new Date());// 创建时间
		clDdrz.setCjr(userId);// 创建人
		clDdrz.setJgdm(clDd.getJgdm());// 机构代码
		clDdrz.setJgmc(clDd.getJgmc());// 机构名称
		clDdrz.setDdzt(newClDd.getDdzt());// 订单状态
		i = ddrzMapper.insertSelective(clDdrz);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "创建订单历史表失败");
			return ApiResponse.error();
		} else {
			return ApiResponse.success();
		}
	}

	public ApiResponse<List<Map<String, Object>>> collectingList(ClDd order) {
		String state = order.getDdzt();
		if (StringUtils.isEmpty(state))
			state = "30";
		RuntimeCheck.ifFalse(StringUtils.equals(state, "30") || StringUtils.equals(state, "40"), "订单状态错误");

		LimitedCondition condition = new LimitedCondition(ClDd.class);
		condition.eq(ClDd.InnerColumn.ddzt, state);
		if (StringUtils.isNotEmpty(order.getJgdm())){
			condition.eq(ClDd.InnerColumn.jgdm,order.getJgdm());
		}
		if (StringUtils.isNotEmpty(order.getCk())) {
			condition.like(ClDd.InnerColumn.ck, order.getCk());
		}
		if (StringUtils.isNotEmpty(order.getJgmc())) {
			condition.like(ClDd.InnerColumn.jgmc, order.getJgmc());
		}
		if (order.getStartTime() != null) {
			condition.gte(ClDd.InnerColumn.cjsj, order.getStartTime());
		}
		if (order.getEndTime() != null) {
			condition.lte(ClDd.InnerColumn.cjsj, order.getEndTime());
		}
		List<ClDd> orderList = findByCondition(condition);
		if (orderList.size() == 0) {
			return ApiResponse.success(new ArrayList<>());
		}

		// 订单分组
		Map<String, Map<String, Object>> orderGroupMap = new HashMap<>();
		for (ClDd o : orderList) {
			String orgCode = o.getJgdm();
			Map<String, Object> map = orderGroupMap.get(orgCode);
			if (map != null) {
				List<ClDd> orders = (List<ClDd>) map.get("orderList");
				Double oldAmount = (Double) map.get("amount");
				Double newAmount = MathUtil.add(oldAmount, o.getZj());
				map.put("amount", newAmount);
				orders.add(o);
			} else {
				List<ClDd> orders = new ArrayList<>();
				orders.add(o);
				map = new HashMap<>();
				map.put("orgCode", o.getJgdm());
				map.put("orgName", o.getJgmc());
				map.put("orderList", orders);
				map.put("amount", o.getZj());
				orderGroupMap.put(orgCode, map);
			}
		}
		List<Map<String, Object>> list = new ArrayList<>(orderGroupMap.size());
		for (Entry<String, Map<String, Object>> entry : orderGroupMap.entrySet()) {
			Map<String, Object> map = entry.getValue();
			List<ClDd> orders = (List<ClDd>) map.get("orderList");
			orders.sort(Comparator.comparing(ClDd::getCjsj));
			list.add(entry.getValue());
		}
		return ApiResponse.success(list);
	}

	public ApiResponse<List<Map<String, Object>>> paymentList(ClDd order) {
		String state = order.getFkzt();
		if (StringUtils.isEmpty(state))
			state = "00";
		RuntimeCheck.ifFalse(StringUtils.equals(state, "00") || StringUtils.equals(state, "10"), "订单状态错误");

		LimitedCondition condition = new LimitedCondition(ClDd.class);
		condition.eq(ClDd.InnerColumn.fkzt, state);
		condition.gte(ClDd.InnerColumn.ddzt,"20");
		if (StringUtils.isNotEmpty(order.getSjxm())) {
			condition.like(ClDd.InnerColumn.sjxm, order.getSjxm());
		}
		if (order.getStartTime() != null) {
			condition.gte(ClDd.InnerColumn.cjsj, order.getStartTime());
		}
		if (order.getEndTime() != null) {
			condition.lte(ClDd.InnerColumn.cjsj, order.getEndTime());
		}
		List<ClDd> orderList = findByCondition(condition);
		if (orderList.size() == 0) {
			return ApiResponse.success(new ArrayList<>());
		}

		// 订单分组
		Map<String, Map<String, Object>> orderGroupMap = new HashMap<>();
		for (ClDd o : orderList) {
			String sjxm = o.getSjxm();
			Map<String, Object> map = orderGroupMap.get(sjxm);
			if (map != null) {
				List<ClDd> orders = (List<ClDd>) map.get("orderList");
				Double oldAmount = (Double) map.get("amount");
				Double newAmount = MathUtil.add(oldAmount, o.getZj() == null ? 0 : o.getZj());
				map.put("amount", newAmount);
				orders.add(o);
			} else {
				List<ClDd> orders = new ArrayList<>();
				orders.add(o);
				map = new HashMap<>();
				map.put("orderId", o.getId());
				map.put("driverName", o.getSjxm());
				map.put("driverId", o.getSj());
				map.put("orderList", orders);
				map.put("amount", o.getZj() == null ? 0 : o.getZj());
				orderGroupMap.put(sjxm, map);
			}
		}
		List<Map<String, Object>> list = new ArrayList<>(orderGroupMap.size());
		for (Entry<String, Map<String, Object>> entry : orderGroupMap.entrySet()) {
			list.add(entry.getValue());
		}
		return ApiResponse.success(list);
	}

	@Override
	public ApiResponse<String> collectingConfirm(List<String> idList) {
		RuntimeCheck.ifEmpty(idList, "请选择订单");
		List<ClDd> orders = findIn(ClDd.InnerColumn.id, idList);
		RuntimeCheck.ifEmpty(orders, "请选择订单");
		for (ClDd order : orders) {
			RuntimeCheck.ifNull(order, "订单不存在");
			RuntimeCheck.ifFalse("30".equals(order.getDdzt()), "订单状态异常");
			order.setDdzt("40");
			entityMapper.updateByPrimaryKeySelective(order);
		}
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<String> paymentConfirm(List<String> idList) {
		RuntimeCheck.ifEmpty(idList, "请选择订单");
		List<ClDd> orders = findIn(ClDd.InnerColumn.id, idList);
		RuntimeCheck.ifEmpty(orders, "请选择订单");
		for (ClDd order : orders) {
			RuntimeCheck.ifNull(order, "订单不存在");
			RuntimeCheck.ifFalse("00".equals(order.getFkzt()), "订单状态异常");
			order.setFkzt("10");
			entityMapper.updateByPrimaryKeySelective(order);
		}
		return ApiResponse.success();
	}

	/**
	 * 收款管理
	 *
	 * @param entity
	 *            ddzt 订单状态 30：因收单据 40：已收单据 必填 ck 乘客姓名
	 * @return jgdm 机构ID jgmc 机构名称 ddList 订单列表
	 */
	// public ApiResponse<List<Map<String,Object>>> proceedsDetail(ClDd entity){
	//// 1、定义初始变量
	// ApiResponse<List<Map<String,Object>>> result = new ApiResponse<>();
	//
	// List<Map<String,Object>> rList = new ArrayList<>();
	//
	//// Map<String,Object> rMap = new HashMap<>();
	//// SysYh user = getCurrentUser();
	//// String firstJgdm=user.getJgdm();//原始机构ID
	// Map<String,Object> rMap = new HashMap<String,Object>();
	//// SysYh user = getCurrentUser();
	// String firstJgdm="";//原始机构ID
	//// Map<String,Object> rMap = new HashMap<>();
	//// SysYh user = getCurrentUser();
	//// String firstJgdm=user.getJgdm();//原始机构ID
	// String firstJgmc="";//原始机构名称
	// List<ClDd> firstDdList=new ArrayList<ClDd>();
	//
	//// 2、验证参数的有效性
	// String ddzt=entity.getDdzt();
	// if(!(StringUtils.equals(ddzt,"30")||StringUtils.equals(ddzt,"40"))){
	// RuntimeCheck.ifFalse(false,"入参错误");
	// }
	//// 3、查询订单信息
	// SimpleCondition condition = new SimpleCondition(ClDd.class);
	//
	// if(StringUtils.isNotBlank(entity.getCk())){
	// condition.like(ClDd.InnerColumn.ck,entity.getCk());
	// }
	// condition.eq(ClDd.InnerColumn.ddzt,ddzt);
	// condition.setOrderByClause(ClDd.InnerColumn.jgdm.desc());
	// List<ClDd> orgs = findByCondition(condition);
	//// 4、遍历订单LIST列表
	// if(orgs!= null&&orgs.size()>0){
	//// firstJgdm=StringUtils.trimToEmpty(orgs.get(0).getJgdm());//原始机构ID
	//// firstJgmc=StringUtils.trimToEmpty(orgs.get(0).getJgmc());
	// for(ClDd list:orgs){
	// String ddJgdm=StringUtils.trimToEmpty(list.getJgdm());
	// if(StringUtils.isNotBlank(ddJgdm)){
	// if(!StringUtils.equals(firstJgdm,ddJgdm)){
	//// 4-1 将上一个机构的信息存放到返回的List中
	// rMap.put("ddList",firstDdList);
	// rMap.put("jgdm",firstJgdm);
	// rMap.put("jgmc",firstJgmc);
	// rList.add(rMap);
	//
	//// 4-2、清除上一个机构的订单信息
	// rMap = new HashMap<String,Object>();
	// firstJgdm=ddJgdm;
	// firstJgmc=StringUtils.trimToEmpty(list.getJgmc());
	// firstDdList=new ArrayList<ClDd>();
	// }else{
	// firstDdList.add(list);
	// }
	// }
	// }
	//
	//
	// }
	//
	// result.setResult(rList);
	// return result;
	// }

	// /**
	// * 付款管理
	// * @param entity
	// * ddzt 订单状态 30：因收单据 40：已收单据 必填
	// * ck 乘客姓名
	// * @return
	// * sjid 司机ID
	// * sjxm 司机姓名
	// * ddList 订单列表
	// */
	// public ApiResponse<List<Map<String,Object>>> paymentDetail(ClDd entity){
	//// 1、定义初始变量
	// ApiResponse<List<Map<String,Object>>> result = new
	// ApiResponse<List<Map<String,Object>>>();
	//
	// List<Map<String,Object>> rList=new ArrayList<Map<String,Object>>();
	//
	// Map<String,Object> rMap = new HashMap<String,Object>();
	// String firstSj="";//原始驾驶员ID
	// String firstsjxm="";//原始驾驶员姓名
	// List<ClDd> firstDdList=new ArrayList<ClDd>();
	//
	//// 2、验证参数的有效性
	// String ddzt=entity.getDdzt();
	// if(!(StringUtils.equals(ddzt,"30")||StringUtils.equals(ddzt,"40"))){
	// RuntimeCheck.ifFalse(false,"入参错误");
	// }
	//// 3、查询订单信息
	// SimpleCondition condition = new SimpleCondition(ClDd.class);
	//
	// if(StringUtils.isNotBlank(entity.getCk())){
	// condition.like(ClDd.InnerColumn.ck,entity.getCk());
	// }
	// condition.eq(ClDd.InnerColumn.ddzt,ddzt);
	// condition.setOrderByClause(ClDd.InnerColumn.sj.desc());
	// List<ClDd> orgs = findByCondition(condition);
	//// 4、遍历订单LIST列表
	// if(orgs!= null){
	// for(ClDd list:orgs){
	// String ddSj=StringUtils.trimToEmpty(list.getSj());
	// if(StringUtils.isNotBlank(ddSj)){
	// if(!StringUtils.equals(firstSj,ddSj)){
	//// 4-1 将上一个机构的信息存放到返回的List中
	// rMap.put("ddList",firstDdList);
	// rMap.put("sjid",firstSj);
	// rMap.put("sjxm",firstsjxm);
	// rList.add(rMap);
	//
	//// 4-2、清除上一个机构的订单信息
	// rMap = new HashMap<String,Object>();
	// firstSj=ddSj;
	// firstsjxm=StringUtils.trimToEmpty(list.getSjxm());
	// firstDdList=new ArrayList<ClDd>();
	// }else{
	// firstDdList.add(list);
	// }
	// }
	// }
	// }
	// result.setResult(rList);
	// return result;
	// }
	/**
	 * 财务结算-订单编辑 1、订单处于：队长确认(队长确定价格)
	 *
	 * @return
	 */
	public ApiResponse<String> updateFinanceOrder(ClDd entity) {
		SysYh user = getCurrentUser();
		String userId = user.getYhid();

		// 1、查找该ID是否存在
		ClDd clDd = findById(entity.getId());
		RuntimeCheck.ifNull(clDd, "未找到订单记录");
		// 2、验证当前状态必须是 11-订单确认状态
		String ddzt = clDd.getDdzt();
		RuntimeCheck.ifFalse(StringUtils.equals(ddzt, "30")||StringUtils.equals(ddzt,"40") , "订单没有处理队长确认状态，不能进行编辑操作");

		RuntimeCheck.ifNull(entity.getZj(), "订单总价不能为空");

		entity.setSj(userId);// 修改人
		entity.setXgsj(new Date());// 修改时间
		// 3、修改订单
		int i = update(entity);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "创建订单历史表失败");
			return ApiResponse.fail("操作数据库失败");
		}

			// 原始单据
		 ClDdlsb initOrder = new ClDdlsb();
		 BeanUtils.copyProperties(entity,initOrder,"id");
		 initOrder.setId(genId());
		 initOrder.setDdId(entity.getId());
		 ddlsbMapper.insertSelective(initOrder);

		// 4、插入日志表
		ClDdrz clDdrz = new ClDdrz();
		clDdrz.setId(genId());// 主键ID
		clDdrz.setDdId(clDd.getId());// 订单ID
		clDdrz.setCjsj(new Date());// 创建时间
		clDdrz.setCjr(userId);// 创建人
		clDdrz.setJgdm(clDd.getJgdm());// 机构代码
		clDdrz.setJgmc(clDd.getJgmc());// 机构名称
		clDdrz.setDdzt(ddzt);// 订单状态
		i = ddrzMapper.insertSelective(clDdrz);
		if (i == 0) {
			RuntimeCheck.ifFalse(false, "创建订单历史表失败");
			return ApiResponse.error();
		} else {
			return ApiResponse.success();
		}
	}

	public ApiResponse<String> updateFinanceConfirm(String[] ids) {
		SysYh user = getCurrentUser();
		String userId = user.getYhid();
		RuntimeCheck.ifNull(ids, "入参错误");
		for (String id : ids) {
			// 1、查找该ID是否存在
			ClDd clDd = findById(id);
			RuntimeCheck.ifNull(clDd, "未找到订单记录");
			// 2、验证当前状态必须是 11-订单确认状态
			String ddzt = clDd.getDdzt();
			RuntimeCheck.ifFalse(StringUtils.equals(ddzt, "30"), "订单没有处理队长确认状态，不能进行财务确定操作");

			clDd.setSj(userId);// 修改人
			clDd.setXgsj(new Date());// 修改时间
			clDd.setDdzt("40");
			// 3、修改订单
			update(clDd);

			// 4、插入日志表
			ClDdrz clDdrz = new ClDdrz();
			clDdrz.setId(genId());// 主键ID
			clDdrz.setDdId(clDd.getId());// 订单ID
			clDdrz.setCjsj(new Date());// 创建时间
			clDdrz.setCjr(userId);// 创建人
			clDdrz.setJgdm(clDd.getJgdm());// 机构代码
			clDdrz.setJgmc(clDd.getJgmc());// 机构名称
			clDdrz.setDdzt(clDd.getDdzt());// 订单状态
			ddrzMapper.insertSelective(clDdrz);
		}
		return ApiResponse.success();
	}

	public ClDd orderMoneyFormula(ClDd order) {

		return order;
	}

	@Override
	public ApiResponse<List<Ddtongji>> ddtongji(DdTongjiTJ dd) {
		if (StringUtils.isEmpty(dd.getJgdm())) {
			SysYh currentUser = getCurrentUser();
			String jgdm = currentUser.getJgdm();
			dd.setJgdm(null);
			dd.setJgdmlike(jgdm);
		}
		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		if (StringUtils.isEmpty(dd.getKssj())) {
			String kssj = DateUtils.getToday() + " 00:00:00";
			dd.setKssj(kssj);
		}else{
			if(StringUtils.contains(dd.getKssj(),"T")){
				DateTime dateTime = formatter.parseDateTime(dd.getKssj().substring(0, 10));
				DateTime days = dateTime.plusDays(1);
				dd.setKssj(days.toString("yyyy-MM-dd") + " 00:00:00");
			}else{
				dd.setKssj(dd.getKssj() + " 00:00:00");
			}

		}
		if (StringUtils.isEmpty(dd.getJssj())) {
			String jssj = DateUtils.getToday() + " 23:59:59";
			dd.setJssj(jssj);
		}else{
			if(StringUtils.contains(dd.getJssj(),"T")){
				DateTime dateTime = formatter.parseDateTime(dd.getJssj().substring(0, 10));
				DateTime days = dateTime.plusDays(1);
				dd.setJssj(days.toString("yyyy-MM-dd") + " 23:59:59");
			}else {
				dd.setJssj(dd.getJssj() + " 23:59:59");
			}

		}

		ApiResponse<List<Ddtongji>> apiResponse = new ApiResponse<>();

		List<Ddtongji> ddlist = new ArrayList<>();

		// 获取条件下所有订单
		List<ClDd> ddTongji = entityMapper.DdTongji(dd);
		if (CollectionUtils.isEmpty(ddTongji)) {
			apiResponse.setCode(404);
			apiResponse.setMessage("未查询到订单");
			return apiResponse;
		}
		// 将订单按照机构分类
		Map<String, List<ClDd>> ddmp = ddTongji.stream().filter(s -> StringUtils.isNotEmpty(s.getJgmc()))
				.collect(Collectors.groupingBy(ClDd::getJgmc));
		// 获取每个机构下面的各种统计订单
		Iterator<Entry<String, List<ClDd>>> it = ddmp.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<ClDd>> next = it.next();
			List<ClDd> value = next.getValue();
			Ddtongji ddtongji = new Ddtongji();
			// 订单总数
			ddtongji.setDdzsCount(value.size());
			int yshCount = 0;
			int wshCount = 0;
			int yqxCount = 0;
			int dsjCount = 0;
			int ddzCount = 0;
			int cwCount = 0;
			for (ClDd clDd : value) {
				// 订单状态 10-订单创建；11-订单确认(待派单)；12-订单驳回；13-已派单；20-司机确认(行程结束)；30-队长确认; 40-财务已收
				if (StringUtils.equals(clDd.getDdzt(), "10")) {
					wshCount++;
				} else if (StringUtils.equals(clDd.getDdzt(), "11")) {
					yshCount++;
				} else if (StringUtils.equals(clDd.getDdzt(), "12")) {
					yqxCount++;
				} else if (StringUtils.equals(clDd.getDdzt(), "13")) {
					dsjCount++;
				} else if (StringUtils.equals(clDd.getDdzt(), "20")) {
					ddzCount++;
				} else if (StringUtils.equals(clDd.getDdzt(), "40")) {
					cwCount++;
				}
				continue;
			}
			ddtongji.setJgmc(next.getKey());
			ddtongji.setYshCount(yshCount);
			ddtongji.setWshCount(wshCount);
			ddtongji.setYqxCount(yqxCount);
			ddtongji.setDsjCount(dsjCount);
			ddtongji.setDdzCount(ddzCount);
			ddtongji.setCwCount(cwCount);
			ddlist.add(ddtongji);
		}

		apiResponse.setResult(ddlist);
		return apiResponse;
	}

	@Override
	public ApiResponse<List<Ddtongji>> chucheTj(DdTongjiTJ dd) {
		if (StringUtils.isEmpty(dd.getJgdm())) {
			SysYh currentUser = getCurrentUser();
			String jgdm = currentUser.getJgdm();
			dd.setJgdm(null);
			dd.setJgdmlike(jgdm);
		}
		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		if (StringUtils.isEmpty(dd.getKssj())) {
			String kssj = DateUtils.getToday() + " 00:00:00";
			dd.setKssj(kssj);
		}else{
			if(StringUtils.contains(dd.getKssj(),"T")){
				DateTime dateTime = formatter.parseDateTime(dd.getKssj().substring(0, 10));
				DateTime days = dateTime.plusDays(1);
				dd.setKssj(days.toString("yyyy-MM-dd") + " 00:00:00");
			}else{
				dd.setKssj(dd.getKssj() + " 00:00:00");
			}

		}
		if (StringUtils.isEmpty(dd.getJssj())) {
			String jssj = DateUtils.getToday() + " 23:59:59";
			dd.setJssj(jssj);
		}else{
			if(StringUtils.contains(dd.getJssj(),"T")){
				DateTime dateTime = formatter.parseDateTime(dd.getJssj().substring(0, 10));
				DateTime days = dateTime.plusDays(1);
				dd.setJssj(days.toString("yyyy-MM-dd") + " 23:59:59");
			}else {
				dd.setJssj(dd.getJssj() + " 23:59:59");
			}

		}

		ApiResponse<List<Ddtongji>> apiResponse = new ApiResponse<>();

		List<Ddtongji> ddlist = new ArrayList<>();

		List<ClDd> ddTongji = entityMapper.DdTongji(dd);
		if (CollectionUtils.isEmpty(ddTongji)) {
			apiResponse.setCode(404);
			apiResponse.setMessage("未查询到订单");
			return apiResponse;
		}
		// 将订单按照司机分类
		Map<String, List<ClDd>> ddmp = ddTongji.stream().filter(s -> StringUtils.isNotEmpty(s.getSjxm()))
				.collect(Collectors.groupingBy(ClDd::getSjxm));

		Iterator<Entry<String, List<ClDd>>> it = ddmp.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<ClDd>> next = it.next();
			List<ClDd> value = next.getValue();
			Ddtongji ddtongji = new Ddtongji();
			int ddzsCount = 0;

			for (ClDd clDd : value) {
				// 订单状态 10-订单创建；11-订单确认(待派单)；12-订单驳回；13-已派单；20-司机确认(行程结束)；30-队长确认; 40-财务已收
				Integer ddzt = Integer.parseInt(clDd.getDdzt());
				if (ddzt>=13) {
					ddzsCount++;
				}
				continue;
			}
			ddtongji.setSjname(next.getKey());
			ddtongji.setDdzsCount(ddzsCount);
			ddlist.add(ddtongji);
		}
		apiResponse.setResult(ddlist);
		return apiResponse;
	}

	@Override
	public ApiResponse<Map<String, Object>> getVeryDayOrder(String cllx) {

		List<String> li = new ArrayList<String>();
		if (StringUtils.equals(cllx, "2030")) {
			li.add("20");
			li.add("30");
		} else {
			li.add(cllx);
		}
		List<Map<String, Object>> list = entityMapper.selectVeryDayOrder(li);
		Map<String, Object> map = entityMapper.selectVeryDayOrderCount(li);
		map.put("list", list);
		return ApiResponse.success(map);
	}

	@Override
	public ApiResponse<String> driverConfirm(String id) {
		RuntimeCheck.ifBlank(id, "请选择订单");
		ClDd order = findById(id);
		RuntimeCheck.ifNull(order, "订单不存在");

		// 2、验证当前状态必须是 11-订单确认状态
		String ddzt = order.getDdzt();
		RuntimeCheck.ifFalse(StringUtils.equals(ddzt, "13"), "订单没有被派单，不能进行司机确认操作");

		// 计算里程费
		if (order.getSjqrsj() == null){
			order.setSjqrsj(new Date());
		}
		BigDecimal lcf = overWorkMoney(order);
		ClDd newClDd = new ClDd();
		newClDd.setLcf(lcf.doubleValue());
		newClDd.setId(order.getId());
		newClDd.setDdzt("20");// 订单状态
		if (order.getSjqrsj() == null){
			newClDd.setSjqrsj(new Date());
		}
		int i = update(newClDd);
		RuntimeCheck.ifTrue(i == 0, "操作数据库失败");

		order.setLcf(lcf.doubleValue());
		order.setDdzt("20");
		ddrzService.log(order);
		return ApiResponse.success();
	}

	@Override
	public ApiResponse<List<Ddtongji>> FukuanTj(DdTongjiTJ dd) {
		if (StringUtils.isEmpty(dd.getJgdm())) {
			SysYh currentUser = getCurrentUser();
			String jgdm = currentUser.getJgdm();
			dd.setJgdm(null);
			dd.setJgdmlike(jgdm);
		}
		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		if (StringUtils.isEmpty(dd.getKssj())) {
			String kssj = DateUtils.getToday() + " 00:00:00";
			dd.setKssj(kssj);
		}else{
			if(StringUtils.contains(dd.getKssj(),"T")){
				DateTime dateTime = formatter.parseDateTime(dd.getKssj().substring(0, 10));
				DateTime days = dateTime.plusDays(1);
				dd.setKssj(days.toString("yyyy-MM-dd") + " 00:00:00");
			}else{
				dd.setKssj(dd.getKssj() + " 00:00:00");
			}

		}
		if (StringUtils.isEmpty(dd.getJssj())) {
			String jssj = DateUtils.getToday() + " 23:59:59";
			dd.setJssj(jssj);
		}else{
			if(StringUtils.contains(dd.getJssj(),"T")){
				DateTime dateTime = formatter.parseDateTime(dd.getJssj().substring(0, 10));
				DateTime days = dateTime.plusDays(1);
				dd.setJssj(days.toString("yyyy-MM-dd") + " 23:59:59");
			}else {
				dd.setJssj(dd.getJssj() + " 23:59:59");
			}

		}
		ApiResponse<List<Ddtongji>> apiResponse = new ApiResponse<>();
		List<Ddtongji> ddlist = new ArrayList<>();
		List<ClDd> ddTongji = entityMapper.DdTongji(dd);
		if (CollectionUtils.isEmpty(ddTongji)) {
			apiResponse.setCode(404);
			apiResponse.setMessage("未查询到订单");
			return apiResponse;
		}

		Map<String, List<ClDd>> ddmp = ddTongji.stream().filter(s -> StringUtils.isNotEmpty(s.getSjxm()))
				.collect(Collectors.groupingBy(ClDd::getSjxm));
		Iterator<Entry<String, List<ClDd>>> it = ddmp.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<ClDd>> next = it.next();
			List<ClDd> value = next.getValue();
			Ddtongji ddtongji = new Ddtongji();
			double fkze = 0;

			for (ClDd clDd : value) {
				// 订单付款状态状态 10已付款 00未付款
				if (StringUtils.equals(clDd.getFkzt(), "10")) {
					fkze = clDd.getZj() + fkze;
				}

			}

			ddtongji.setSjname(next.getKey());
			ddtongji.setFkze(Double.toString(fkze));
			ddlist.add(ddtongji);

		}
		apiResponse.setResult(ddlist);

		return apiResponse;
	}

	@Override
	public ApiResponse<List<Ddtongji>> ShoukuanTj(DdTongjiTJ dd) {
		if (StringUtils.isEmpty(dd.getJgdm())) {
			SysYh currentUser = getCurrentUser();
			String jgdm = currentUser.getJgdm();
			dd.setJgdm(null);
			dd.setJgdmlike(jgdm);
		}
		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		if (StringUtils.isEmpty(dd.getKssj())) {
			String kssj = DateUtils.getToday() + " 00:00:00";
			dd.setKssj(kssj);
		}else{
			if(StringUtils.contains(dd.getKssj(),"T")){
				DateTime dateTime = formatter.parseDateTime(dd.getKssj().substring(0, 10));
				DateTime days = dateTime.plusDays(1);
				dd.setKssj(days.toString("yyyy-MM-dd") + " 00:00:00");
			}else{
				dd.setKssj(dd.getKssj() + " 00:00:00");
			}

		}
		if (StringUtils.isEmpty(dd.getJssj())) {
			String jssj = DateUtils.getToday() + " 23:59:59";
			dd.setJssj(jssj);
		}else{
			if(StringUtils.contains(dd.getJssj(),"T")){
				DateTime dateTime = formatter.parseDateTime(dd.getJssj().substring(0, 10));
				DateTime days = dateTime.plusDays(1);
				dd.setJssj(days.toString("yyyy-MM-dd") + " 23:59:59");
			}else {
				dd.setJssj(dd.getJssj() + " 23:59:59");
			}

		}

		ApiResponse<List<Ddtongji>> apiResponse = new ApiResponse<>();
		List<Ddtongji> ddlist = new ArrayList<>();
		List<ClDd> ddTongji = entityMapper.DdTongji(dd);
		if (CollectionUtils.isEmpty(ddTongji)) {
			apiResponse.setCode(404);
			apiResponse.setMessage("未查询到订单");
			return apiResponse;
		}

		Map<String, List<ClDd>> ddmp = ddTongji.stream().filter(s -> StringUtils.isNotEmpty(s.getJgmc()))
				.collect(Collectors.groupingBy(ClDd::getJgmc));
		// 获取每个机构下面的各种统计订单
		Iterator<Entry<String, List<ClDd>>> it = ddmp.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<ClDd>> next = it.next();
			List<ClDd> value = next.getValue();
			Ddtongji ddtongji = new Ddtongji();
			double fkze = 0;

			for (ClDd clDd : value) {
				// 订单付款状态状态 10已付款 00未付款
				if (StringUtils.equals(clDd.getFkzt(), "10")) {
					fkze = clDd.getZj() + fkze;
				}

			}
			ddtongji.setJgmc(next.getKey());
			ddtongji.setFkze(Double.toString(fkze));
			ddlist.add(ddtongji);
		}

		apiResponse.setResult(ddlist);
		return apiResponse;
	}
	private BigDecimal overWorkMoney(ClDd order){
		/*SimpleCondition condition = new SimpleCondition(SysHsgs.class);
		condition.eq(SysHsgs.InnerColumn.lx,"10");
		List<SysHsgs> hsgsList = hsgsMapper.selectByExample(condition);
		RuntimeCheck.ifTrue(hsgsList.size() == 0,"未找到核算公式");
		SysHsgs hsgs = hsgsList.get(0);
		BigDecimal price = hsgs.getJe();
		String nr = hsgs.getNr();
		long durationMs = 0;
		List<Map<String,Date>> dateList = splitDate(order.getYysj(),order.getSjqrsj());
		for (Map<String, Date> map : dateList) {
			Date startTime = map.get("startTime");
			String startTimeStr = DateUtils.getDateStr(startTime,"yyyy-MM-dd");
			SimpleCondition rlCond = new SimpleCondition(SysRlb.class);
			rlCond.eq(SysRlb.InnerColumn.rq,startTimeStr);
			List<SysRlb> rlbs = rlbMapper.selectByExample(rlCond);
			RuntimeCheck.ifEmpty(rlbs,"未找到日历");
			SysRlb rlb = rlbs.get(0);
			if (!"1".equals(rlb.getZt().trim()))continue;
			durationMs += getExtraTime(nr,map.get("startTime"),map.get("endTime"));
		}
		order.setJbsc(new BigDecimal(durationMs/(1000*60*60)).shortValue());
		entityMapper.updateByPrimaryKeySelective(order);*/
		BigDecimal amount = new BigDecimal(0);//price.multiply(new BigDecimal(durationMs)).divide(new BigDecimal(1000*60*60),2,BigDecimal.ROUND_HALF_UP);
		return amount;
	}


	private static List<Map<String,Date>> splitDate(Date startTime,Date endTime){
		int startDate = startTime.getDate();
		int endDate = endTime.getDate();
		List<Map<String,Date>> list = new ArrayList<>();
		if (startDate == endDate){
			Map<String,Date> map = new HashMap<>();
			map.put("startTime",startTime);
			map.put("endTime",endTime);
			list.add(map);
			return list;
		}
		for (int i = startDate; i <= endDate; i++){
			Map<String,Date> map = new HashMap<>();
			Date sTime = null;
			Date eTime = null;
			if (i == startDate){
				sTime = startTime;
			}else if (i == endDate){
				eTime = endTime;
			}
			if (sTime == null){
				sTime = new Date();
				sTime.setDate(i);
				sTime.setHours(0);
				sTime.setMinutes(0);
				sTime.setSeconds(0);
			}
			if (eTime == null){
				eTime = new Date();
				eTime.setDate(i);
				eTime.setHours(23);
				eTime.setMinutes(59);
				eTime.setSeconds(59);
			}
			map.put("startTime",sTime);
			map.put("endTime",eTime);
			list.add(map);
		}
		return list;
	}

	private static void testSplitDate(){
		Date startTime = new Date();
		Date endTime = new Date();
		startTime.setDate(12);
		List<Map<String,Date>> list = splitDate(startTime,endTime);
		for (Map<String, Date> map : list) {
			System.out.println("startTime:"+map.get("startTime").toLocaleString());
			System.out.println("endTime:"+map.get("endTime").toLocaleString());
			System.out.println("======================");
		}
	}
//	public static void main(String[] ars){
//		testSplitDate();
//        String workTime = "8:30-12:00,14:00-17:00";
//        Date orderStartTime = new Date();
//        Date orderEndTime = new Date();
//        orderStartTime.setHours(7);
//        orderStartTime.setMinutes(20);
//
//        orderEndTime.setHours(20);
//        orderEndTime.setMinutes(0);
//        long s = getExtraTime(workTime,orderStartTime,orderEndTime);
//        System.out.println("s="+s);
//        BigDecimal price = new BigDecimal(10);
//        BigDecimal amount = price.multiply(new BigDecimal(s)).divide(new BigDecimal(1000*60*60),2,BigDecimal.ROUND_HALF_UP);
//        System.out.println(amount);
//	}

	private static long getExtraTime(String workTimeStr,Date orderStartTime,Date orderEndTime){
		String[] times = workTimeStr.split(",");
		String time0 = times[0];
		Date midTime = new Date();
		String midEndTime = time0.split("-")[1];
		int midEndHour = Integer.parseInt(midEndTime.split(":")[0]);
		int midEndMinute = Integer.parseInt(midEndTime.split(":")[1]);
		midTime.setHours(midEndHour);
		midTime.setMinutes(midEndMinute);
		midTime.setSeconds(0);
		long duration = 0;
		if (orderEndTime.getTime() - midTime.getTime() > 0){
			duration += getPartExtraTime(times[0],orderStartTime,midTime,0);
			duration += getPartExtraTime(times[1],midTime,orderEndTime,1);
		}else{
			duration += getPartExtraTime(times[0],orderStartTime,midTime,0);
		}
		return duration;
	}

	private static long getPartExtraTime(String workTimeStr,Date orderStartTime,Date orderEndTime,int i){
		String[] workTimeArray = workTimeStr.split("-");
		String startWorkTime = workTimeArray[0];
		String endWorkTime = workTimeArray[1];

		String[] startWorkTimeArr = startWorkTime.split(":");
		int workStartHour = Integer.parseInt(startWorkTimeArr[0]);
		int workStartMinute = Integer.parseInt(startWorkTimeArr[1]);

		long duration = 0;

		Date workStartTime = new Date(orderStartTime.getTime());
		workStartTime.setHours(workStartHour);
		workStartTime.setMinutes(workStartMinute);
		workStartTime.setSeconds(0);
		long duration1 = workStartTime.getTime() - orderStartTime.getTime();
		if (duration1 > 0)duration += duration1;

		if (i == 1){
			String[] endWorkTimeArr = endWorkTime.split(":");
			int workEndHour = Integer.parseInt(endWorkTimeArr[0]);
			int workEndMinute = Integer.parseInt(endWorkTimeArr[1]);
			Date workEndTime = new Date(orderEndTime.getTime());
			workEndTime.setHours(workEndHour);
			workEndTime.setMinutes(workEndMinute);
			workEndTime.setSeconds(0);
			long duration2 = orderEndTime.getTime() - workEndTime.getTime();
			if (duration2 > 0){
				duration += duration2;
			}
		}
		return duration;
	}

	@Override
	public ApiResponse<DdTjTxReturn> ddzTj(DdTongjiTJ dd) {
		DdTjTxReturn ddTjTxReturn = new DdTjTxReturn();
		if (StringUtils.isEmpty(dd.getJgdm())) {
			SysYh currentUser = getCurrentUser();
			String jgdm = currentUser.getJgdm();
			dd.setJgdmlike(jgdm);
		}

		if (StringUtils.isEmpty(dd.getCxsj())) {
			String monday = getMONDAY();
			ddTjTxReturn.setStartTime(monday);
			dd.setCxsj(monday);
		}else {
			String convertWeekDate = convertWeekDate(dd.getCxsj());
			dd.setCxsj(convertWeekDate);
		}

		ApiResponse<DdTjTxReturn> apiResponse = new ApiResponse<>();


		List<String> dateList = new ArrayList<>();
		List<String> countList = new ArrayList<>();

		List<DdTjTx> weekTj = entityMapper.weekTj(dd);

		for (DdTjTx ddTjTx : weekTj) {

			dateList.add(ddTjTx.getToday());
			countList.add(ddTjTx.getVALUE());
		}

		ddTjTxReturn.setDate(dateList);
		ddTjTxReturn.setCount(countList);
		apiResponse.setResult(ddTjTxReturn);
		return apiResponse;
	}

	public static void main(String[] args) {

		String convertWeekDate = convertWeekDate("2018-06-02");
		System.out.println(convertWeekDate);
	}

	public String getMONDAY() {
		LocalDate with = LocalDate.now().with(DayOfWeek.MONDAY);
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return df.format(with);

	}

	private static String convertWeekDate(String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式

		Calendar cal = Calendar.getInstance();
		Date parse = null;
		try {
			parse = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		cal.setTime(parse);

		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了

		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天

		if (1 == dayWeek) {

			cal.add(Calendar.DAY_OF_MONTH, -1);

		}

		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天

		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

		return sdf.format(cal.getTime()); // 周一时间

	}

	@Override
	public ApiResponse<CcTjTx> chucheTTj(DdTongjiTJ dd) {

		if (StringUtils.isEmpty(dd.getJgdm())) {
			SysYh currentUser = getCurrentUser();
			String jgdm = currentUser.getJgdm();
			dd.setJgdmlike(jgdm);
		}
		if (StringUtils.isEmpty(dd.getKssj())) {
			String kssj = DateUtils.getToday() + " 00:00:00";
			dd.setKssj(kssj);
		}
		if (StringUtils.isEmpty(dd.getJssj())) {
			String jssj = DateUtils.getToday() + " 23:59:59";
			dd.setJssj(jssj);
		}

		ApiResponse<CcTjTx> apiResponse = new ApiResponse<>();

		    CcTjTx ccTjTx = new CcTjTx();
		    List<String> sjxm=new ArrayList<>();
		    List<Integer> count = new ArrayList<>();
		    Map<String, Integer> tjMap= new HashMap<>();
		List<ClDd> ddTongji = entityMapper.DdTongji(dd);

		int ypddCount = 0;
		int dpddCount=0;
		for (ClDd clDd : ddTongji) {
				// 订单状态 10-订单创建；11-订单确认(待派单)；12-订单驳回；13-已派单；20-司机确认(行程结束)；30-队长确认; 40-财务已收
				Integer ddzt = Integer.parseInt(clDd.getDdzt());
				if (ddzt>=13) {
					ypddCount++;
				}else {
					dpddCount++;
				}

		}
		tjMap.put("已派单", ypddCount);
		tjMap.put("待派单", dpddCount);
		ccTjTx.setTjMap(tjMap);

		// 将订单按照司机分类
		Map<String, List<ClDd>> ddmp = ddTongji.stream().filter(s -> StringUtils.isNotEmpty(s.getSjxm()))
				.collect(Collectors.groupingBy(ClDd::getSjxm));

		Iterator<Entry<String, List<ClDd>>> it = ddmp.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, List<ClDd>> next = it.next();
			List<ClDd> value = next.getValue();
			int ypdCount = 0;

			for (ClDd clDd : value) {
				// 订单状态 10-订单创建；11-订单确认(待派单)；12-订单驳回；13-已派单；20-司机确认(行程结束)；30-队长确认; 40-财务已收
				Integer ddzt = Integer.parseInt(clDd.getDdzt());
				if (ddzt>=13) {
					ypdCount++;
				}
				continue;

			}
			sjxm.add(next.getKey());
			count.add(ypdCount);
		}
		ccTjTx.setSjxm(sjxm);
		ccTjTx.setCount(count);
		apiResponse.setResult(ccTjTx);
		return apiResponse;
	}

}
