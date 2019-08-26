package com.ldz.biz.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ldz.biz.controller.ClSbyxsjjlCtrl;
import com.ldz.dao.biz.bean.GpsInfo;
import com.ldz.dao.biz.model.ClGpsLs;
import com.ldz.dao.biz.model.ClSbyxsjjl;
import com.ldz.dao.biz.model.ClXc;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.service.biz.impl.XcServiceImpl;
import com.ldz.service.biz.interfaces.*;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.sys.constant.Dict;
import com.ldz.sys.model.SysYh;
import com.ldz.sys.service.YhService;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import com.ldz.util.commonUtil.ExcelUtil;
import com.ldz.util.commonUtil.JwtUtil;
import com.ldz.util.exception.RuntimeCheck;
import com.ldz.util.redis.RedisTemplateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/*
 * 业务系统对外开放的接口
 *
 */

@RestController
@RequestMapping("/pub/")
public class MessageApi {
	@Autowired
	private GpsService gpsservice;
	@Autowired
	private SpkService spkservice;
	@Autowired
	private InstructionService intstruction;

	@Autowired
	private YhService yhService;
	@Autowired
	private StringRedisTemplate redisDao;
	@Autowired
	private GpsLsService gpsLsService;
	@Autowired
	private SbyxsjjlService sbyxsjjlService;
	@Autowired
	private XcService xcService;
	@Autowired
	private ZdglService zdglService;
	//事件 10急加速，20急刹车，30急转弯 ，40超速，50点火，60熄火,70不在电子围栏范围,80离线
	@GetMapping("/testExcel")
	public void testExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {





			List<Map<Integer, String>> data = new ArrayList<>();
			Map<Integer, String> map = new HashMap<>();
			map.put(0, "终端编号");
			map.put(1, "开始时间");
			map.put(2, "结束时间");
			map.put(3, "开始轨迹");
			map.put(4, "结束轨迹");
			map.put(5, "总里程");
			data.add(map);
		List<ClZdgl> zdgls = zdglService.findEq(ClZdgl.InnerColumn.zdLx, "30");
		Set<String> set = zdgls.stream().map(ClZdgl::getZdbh).collect(Collectors.toSet());
		SimpleCondition simpleCondition = new SimpleCondition(ClXc.class);
			simpleCondition.setOrderByClause(" xc_kssj desc ");
			simpleCondition.in(ClXc.InnerColumn.clZdbh, set);
		List<ClXc> clXcs = xcService.findByCondition(simpleCondition);
			for (ClXc clXc : clXcs) {
				Map<Integer, String> dataMap = new HashMap<>();
				dataMap.put(0, clXc.getClZdbh());
				dataMap.put(1, clXc.getXcKssj());
				dataMap.put(2, clXc.getXcJssj());
				dataMap.put(3, clXc.getStartAddress());
				dataMap.put(4,clXc.getEndAddress());
				dataMap.put(5, clXc.getXcLc());
				data.add(dataMap);
				System.out.println(data.size());
			}

		String fileName = java.net.URLEncoder.encode(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".xls", "UTF-8");
		response.setContentType("application/msexcel");
		request.setCharacterEncoding("UTF-8");
		response.setHeader("pragma", "no-cache");
		response.addHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1") + ".xls");
		OutputStream out = response.getOutputStream();
		ExcelUtil.createSheet(out,"行程统计",data);

	}



	@RequestMapping("gpsLsPager")
	public ApiResponse<List<ClGpsLs>> gpsLsPager(Page<ClGpsLs> pager){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		request.setAttribute("orgCode","100");
		ApiResponse<List<ClGpsLs>> result = new ApiResponse<>();
		LimitedCondition condition = new LimitedCondition(ClGpsLs.class);
		if (StringUtils.isNotBlank(request.getParameter("zdbhLike"))){
			condition.like("zdbh",request.getParameter("zdbhLike"));
		}
		String cjsjInRange = request.getParameter("cjsjInRange");
		if (StringUtils.isNotBlank(cjsjInRange) && StringUtils.isNotBlank(cjsjInRange.replaceAll(",","").trim())){
			String[] arrs = cjsjInRange.split(",");
			try {
				condition.gte("cjsj", DateUtils.getDate(arrs[0], "yyyy-MM-dd HH:mm:ss"));
				condition.lte("cjsj", DateUtils.getDate(arrs[1], "yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		PageInfo<ClGpsLs> resultPage = gpsLsService.findPage(pager, condition);
		result.setPage(resultPage);
		return result;
	}
	@RequestMapping("sjjlPager")
	public ApiResponse<List<ClSbyxsjjl>> sjjlPager(Page<ClSbyxsjjl> pager){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		request.setAttribute("orgCode","100");
		ApiResponse<List<ClSbyxsjjl>> result = new ApiResponse<>();
		LimitedCondition condition = new LimitedCondition(ClSbyxsjjl.class);
		if (StringUtils.isNotBlank(request.getParameter("zdbhLike"))){
			condition.like("zdbh",request.getParameter("zdbhLike"));
		}
		String cjsjInRange = request.getParameter("cjsjInRange");
		if (StringUtils.isNotBlank(cjsjInRange) && StringUtils.isNotBlank(cjsjInRange.replaceAll(",","").trim())){
			String[] arrs = cjsjInRange.split(",");
			try {
				condition.gte("cjsj", DateUtils.getDate(arrs[0], "yyyy-MM-dd HH:mm:ss"));
				condition.lte("cjsj", DateUtils.getDate(arrs[1], "yyyy-MM-dd HH:mm:ss"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		PageInfo<ClSbyxsjjl> resultPage = sbyxsjjlService.findPage(pager, condition);
		result.setPage(resultPage);
		return result;
	}

	/*
	 * 给tic-server提供gps存储接口
	 */
	@PostMapping("/gps/save")
	public ApiResponse<String> filterAndSave(@RequestBody GpsInfo entity) {
		return gpsservice.onReceiveGps(entity);
	}

	/*
	 * 给tic-server提供云视屏存储接口
	 */
	@PostMapping("/spk/save")
	public ApiResponse<String> saveSpk(@RequestBody GpsInfo entity) {

		return spkservice.saveSpk(entity);
	}

	@PostMapping("/intstruction/send")
	public ApiResponse<String> sendinstruction( GpsInfo info) {

       return  intstruction.sendinstruction(info);
	}

	@PostMapping("/batchUpdate")
	public ApiResponse<String> batchUpdate(HttpServletRequest request, GpsInfo info, ClZdgl zdgl) {
		String userid = request.getHeader("userid");
		String token = request.getHeader("token");
		if (token == null)
			token = request.getParameter("token");
		if (userid == null)
			userid = request.getParameter("userid");

		if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(token)){
			return ApiResponse.fail("请您登录后再操作");
		}
		// 验证用户状态
		SysYh user = yhService.findById(userid);
		if (!Dict.UserStatus.VALID.getCode().equals(user.getZt())){
			return ApiResponse.fail("该用户已锁定，请联系管理人员");
		}
		SysYh userInfo =null;
		try {
			// 验证访问者是否合法
			String userId = JwtUtil.getClaimAsString(token, "userId");
			if (!userid.equals(userId)){
				return ApiResponse.fail("登录失败");
			}
			String value = redisDao.boundValueOps(userid).get();
			if (StringUtils.isEmpty(value) || !value.equals(token)){
				return ApiResponse.fail("未找到该用户的缓存信息");
			}
			request.setAttribute("userInfo", user);
			request.setAttribute("orgCode", user.getJgdm());
			String userInfoJson = redisDao.boundValueOps(userid + "-userInfo").get();
			ObjectMapper mapper = new ObjectMapper();
			userInfo = mapper.readValue(userInfoJson, SysYh.class);

		} catch (Exception e) {
			return ApiResponse.fail("用户验证失败");
		}
		RuntimeCheck.ifNull(userInfo,"用户登录验证失败");

		return  intstruction.batchUpdate(info,zdgl,userInfo);
	}

	/*@GetMapping("/test")
	public String test(){
		SimpleCondition s = new SimpleCondition(ClGpsLs.class);
		Example.Criteria criteria = s.createCriteria();
		criteria.andCondition("CJSJ >= to_date('2018-07-27 07:45:16','yyyy-MM-dd HH:mi:ss') and CJSJ <= to_date('2018-07-27 08:00:00','yyyy-MM-dd HH:mi:ss') and zdbh = '865923030038639' ");
		s.and(criteria);

		s.setOrderByClause(" CJSJ desc ");
		List<ClGpsLs> gpsLs = gpsLsService.findByCondition(s);
		List<PointListBean> listBeans = new ArrayList<>();
		gpsLs.stream().forEach(clGpsLs -> {
			PointListBean pointListBean = new PointListBean();
			pointListBean.setCoord_type_input("bd09ll");
			pointListBean.setDirection(clGpsLs.getFxj().doubleValue());
			pointListBean.setLatitude(clGpsLs.getBdwd().doubleValue());
			pointListBean.setLongitude(clGpsLs.getBdjd().doubleValue());
			pointListBean.setLoc_time(clGpsLs.getCjsj().getTime() / 1000);
			pointListBean.setSpeed(Double.parseDouble(clGpsLs.getYxsd()));
			listBeans.add(pointListBean);
		});

		return GuiJIApi.trackPoint(listBeans);
	}*/

	/*@GetMapping("test")
	public void s(){
		System.out.println("------");
		redisTemplateUtil.boundValueOps("1").set("123",10,TimeUnit.SECONDS);
		System.out.println("++++++");
		System.out.println(redisTemplateUtil.boundValueOps("1").get());
	}

	@GetMapping("tt")
	public void a(){
		System.out.println("========");
		System.out.println(redisTemplateUtil.boundValueOps("1").get());
		redisTemplateUtil.delete("1");
		System.out.println("|||||||||||||||||||||");
	}*/


}
