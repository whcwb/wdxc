package com.ldz.service.biz.impl;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.ldz.dao.biz.mapper.ClZdglReportMapper;
import com.ldz.service.biz.interfaces.UBIService;
import com.ldz.service.biz.interfaces.XcService;
import com.ldz.service.biz.interfaces.ZdglService;
import com.ldz.sys.base.LimitedCondition;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.commonUtil.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ldz.dao.biz.model.ClXc;
import com.ldz.dao.biz.model.ClZdgl;
import com.ldz.dao.biz.model.ClZdglReport;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.sys.model.SysJg;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.exception.RuntimeCheck;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

@Service
public class UBIServiceImpl extends BaseServiceImpl<ClZdglReport, String> implements UBIService {
	@Autowired
	private ClZdglReportMapper entityMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ZdglService zdglService;
	@Autowired
	private XcService xcService;

	Logger errorLog = LoggerFactory.getLogger("error_info");


	@Override
	protected Mapper<ClZdglReport> getBaseMapper() {
		return entityMapper;
	}

	/**
	 * 获取接口调用者所属机构信息
	 * @return
	 */
	private SysJg getJgxx(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysJg jgxx = (SysJg)request.getAttribute("requestJgxx");
		RuntimeCheck.ifNull(jgxx, "无权调用接口！");

		return jgxx;
	}

	@Override
	public ApiResponse<String> bindDevice(ClZdgl data) {
		RuntimeCheck.ifBlank(data.getZdbh(), "终端编号不能为空！");
		SysJg jgxx = getJgxx();
		Example condition = new Example(ClZdgl.class);
		condition.and().andEqualTo(ClZdgl.InnerColumn.zdbh.name(), data.getZdbh()).andLike(ClZdgl.InnerColumn.jgdm.name(), jgxx.getJgdm()+"%");
		List<ClZdgl> exist = zdglService.findByCondition(condition);
		RuntimeCheck.ifEmpty(exist, "终端暂未入库！");
		ClZdgl updateData = exist.get(0);
		updateData.setBz(data.getBz());
		updateData.setXgr("UBI接口终端绑定");
		updateData.setXgsj(new Date());
		zdglService.update(updateData);
		ApiResponse<String> result = ApiResponse.success("终端绑定成功！");
		result.setResult(updateData.getZdLx());

		return result;
	}

	@Override
	public ApiResponse<Map<String, Object>> totalBase(String zdbhs) {
		SortedMap<String, Object> totalDataMap = new ConcurrentSkipListMap<String, Object>();
		SysJg jgxx = getJgxx();
		try{
			//实时查询今日里程数和驾驶时长
			String sql = "select m.* from cl_xc m join cl_zdgl c " +
						//根据终端类型和激活状态，当前时间查询数据
						 " on m.CL_ZDBH=c.zdbh where c.jgdm like '"+jgxx.getJgdm()+"%' and c.jhzt='20' and m.XC_KSSJ>='"+DateTime.now().toString("yyyy-MM-dd")+" 00:00:00'";
			String tmpValue = "";
			if (StringUtils.isNotBlank(zdbhs)){
				sql += " and m.cl_zdbh in (";
				String[] arrs = zdbhs.split(",");

				for (int i=0;i <arrs.length; i++){
					tmpValue += "'"+arrs[i]+"',";
				}
				tmpValue = tmpValue.substring(0, tmpValue.length() - 1);
				sql += tmpValue + " )";
			}
			sql += " order by m.CL_ZDBH,m.XC_JSSJ";
			List<ClXc> currentXcList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ClXc>(ClXc.class));

			DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
			//今日行程和里程总数
			String currentMinutesKey = "currentTimes";
			String currentMilesKey = "currentMiles";
			if (!currentXcList.isEmpty()){
				for (int i=0; i<currentXcList.size(); i++){
					ClXc xcItem = currentXcList.get(i);
					//统计驾驶时长
					DateTime begin = DateTime.parse(xcItem.getXcKssj(), dtf);
					DateTime end = DateTime.parse(xcItem.getXcJssj(), dtf);
					Integer minute = Minutes.minutesBetween(begin, end).getMinutes();
					if (totalDataMap.containsKey(currentMinutesKey)){
						minute += (Integer)totalDataMap.get(currentMinutesKey);
					}
					//统计行驶里程。里程是附加在开始和结束GPS点字段内
					String[] xslcArray = xcItem.getXcStartEnd().split(",");
					Integer miles = 0;
					if (xslcArray.length == 3){
						miles = (int)Float.parseFloat(xslcArray[2]);
					}
					if (totalDataMap.containsKey(currentMilesKey)){
						miles += (Integer)totalDataMap.get(currentMilesKey);
					}
					//今日行驶时长
					totalDataMap.put(currentMinutesKey, minute);
					//今日行驶里程
					totalDataMap.put(currentMilesKey, miles);
				}
			}else{
				//今日行驶时长
				totalDataMap.put(currentMinutesKey, 0);
				//今日行驶里程
				totalDataMap.put(currentMilesKey, 0);
			}
			//最近7天新增数据
			sql = "select r_tjrq as tjrq, sum(r_jszsc) as times,sum(r_lczs) as miles from cl_zdgl_report m where m.jgdm like '"+jgxx.getJgdm()+"%' and m.r_tjrq>='"+DateTime.now().plusDays(-7).toString("yyyy-MM-dd")+"' and m.r_tjrq<'"+DateTime.now().toString("yyyy-MM-dd")+"'";
			if (StringUtils.isNotBlank(tmpValue)){
				sql += " and m.r_zdbh in (" + tmpValue + " )";
			}
			sql += " group by r_tjrq";
			List weekData = jdbcTemplate.queryForList(sql);
			totalDataMap.put("week", weekData);
			//本月行程和里程总数
			sql = "select sum(r_jszsc) as times,sum(r_lczs) as miles from cl_zdgl_report m where m.jgdm like '"+jgxx.getJgdm()+"%' and m.r_tjrq>='"+DateTime.now().dayOfMonth().withMinimumValue().toString("yyyy-MM-dd")+"' and m.r_tjrq<='"+DateTime.now().dayOfMonth().withMaximumValue().toString("yyyy-MM-dd")+"'";
			if (StringUtils.isNotBlank(tmpValue)){
				sql += " and m.r_zdbh in (" + tmpValue + " )";
			}
			Map<String, Object> monthData = jdbcTemplate.queryForMap(sql);
			//统计本月手工把今日数据也累加上去
			totalDataMap.put("monthTimes", (monthData.get("times") == null ? 0 : (int)Float.parseFloat(monthData.get("times").toString())) + (Integer)totalDataMap.get(currentMinutesKey));
			totalDataMap.put("monthMiles", (monthData.get("miles") == null ? 0 : (int)Float.parseFloat(monthData.get("miles").toString())) + (Integer)totalDataMap.get(currentMilesKey));
			//本年行程和里程总数
			sql = "select sum(r_jszsc) as times,sum(r_lczs) as miles from cl_zdgl_report m where m.jgdm like '"+jgxx.getJgdm()+"%' and m.r_tjrq>='"+DateTime.now().toString("yyyy")+"-01-01' and m.r_tjrq<='"+DateTime.now().toString("yyyy")+"-12-31'";
			if (StringUtils.isNotBlank(tmpValue)){
				sql += " and m.r_zdbh in (" + tmpValue + " )";
			}
			Map<String, Object> yearData = jdbcTemplate.queryForMap(sql);
			//统计本年手工把今日数据也累加上去
			totalDataMap.put("yearTimes", (yearData.get("times") == null ? 0 : (int)Float.parseFloat(yearData.get("times").toString())) + (Integer)totalDataMap.get(currentMinutesKey));
			totalDataMap.put("yearMiles", (yearData.get("miles") == null ? 0 : (int)Float.parseFloat(yearData.get("miles").toString())) + (Integer)totalDataMap.get(currentMilesKey));
		}catch(Exception e){
			totalDataMap.put("currentTimes", 0);
			totalDataMap.put("currentMiles", 0);
			totalDataMap.put("monthTimes", 0);
			totalDataMap.put("monthMiles", 0);
			totalDataMap.put("yearTimes", 0);
			totalDataMap.put("yearMiles", 0);
			errorLog.error("统计数据发生异常", e);
		}


		return ApiResponse.success(totalDataMap);
	}

	@Override
	public ApiResponse<List<Map<String, Object>>> totalMiles(String rangedate, String zdbhs) {
		ApiResponse<List<Map<String, Object>>> result = new ApiResponse<>();
		if (StringUtils.isBlank(zdbhs)){
			result.setMessage("查询终端编号不能为空!");
			result.setCode(ApiResponse.FAILED);

			return result;
		}
		SysJg jgxx = getJgxx();
		//如果没有传开始和结束时间，默认查询3天内的数据
		if (StringUtils.isBlank(rangedate)){
			rangedate = DateTime.now().plusDays(-3).toString("yyyy-MM-dd") + "," + DateTime.now().toString("yyyy-MM-dd");
		}
		String tmpValue = "";
		if (StringUtils.isNotBlank(zdbhs)){
			String[] arrs = zdbhs.split(",");

			for (int i=0;i <arrs.length; i++){
				tmpValue += "'"+arrs[i]+"',";
			}
			tmpValue = tmpValue.substring(0, tmpValue.length() - 1);
		}
		String sql = "select r_zdbh as zdbh, sum(r_lczs) as miles from cl_zdgl_report m where m.jgdm like '"+jgxx.getJgdm()+"%' and m.r_zdbh in ("+tmpValue+") and m.r_tjrq>='"+rangedate.split(",")[0]+"' and m.r_tjrq<='"+rangedate.split(",")[1]+"' GROUP BY r_zdbh";
		result.setResult(jdbcTemplate.queryForList(sql));
		return result;
	}

	@Override
	public ApiResponse<List<Map<String, Object>>> totalAvgmiles(String rangedate, String zdbhs) {
		ApiResponse<List<Map<String, Object>>> result = new ApiResponse<>();
		SysJg jgxx = getJgxx();
		//如果没有传日期类型，默认为：周
		if (StringUtils.isBlank(rangedate)){
			rangedate = DateTime.now().plusDays(-7).toString("yyyy-MM-dd") + "," + DateTime.now().toString("yyyy-MM-dd");
		}
		String tmpValue = "";
		if (StringUtils.isNotBlank(zdbhs)){
			String[] arrs = zdbhs.split(",");

			for (int i=0;i <arrs.length; i++){
				tmpValue += "'"+arrs[i]+"',";
			}
			tmpValue = tmpValue.substring(0, tmpValue.length() - 1);
		}
		String sql = "select r_zdbh as zdbh, avg(r_lczs) as avgmiles from cl_zdgl_report m where m.jgdm like '"+jgxx.getJgdm()+"%' and m.r_tjrq>='"+rangedate.split(",")[0]+"' and m.r_tjrq<='"+rangedate.split(",")[1]+"'";
		if (StringUtils.isNotBlank(tmpValue)){
			sql += " and m.r_zdbh in ("+tmpValue+")";
		}
		sql += " GROUP BY r_zdbh";
		result.setResult(jdbcTemplate.queryForList(sql));
		return result;
	}

	@Override
	public ApiResponse<Map<String, Integer>> analysis(String zdbhs) {
		SortedMap<String, Integer> totalDataMap = new ConcurrentSkipListMap<String, Integer>();
		SysJg jgxx = getJgxx();
		try{
			//实时查询设备总数和在线数量
			String sql = "select zdbh,zxzt from cl_zdgl where jgdm like '"+jgxx.getJgdm()+"%' and jhzt='20'";
			String tmpValue = "";
			if (StringUtils.isNotBlank(zdbhs)){
				sql += " and m.cl_zdbh in (";
				String[] arrs = zdbhs.split(",");

				for (int i=0;i <arrs.length; i++){
					tmpValue += "'"+arrs[i]+"',";
				}
				tmpValue = tmpValue.substring(0, tmpValue.length() - 1);
				sql += tmpValue + " )";
			}
			List<ClZdgl> itemList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ClZdgl>(ClZdgl.class));

			//设备总数和在线数量
			String allKey = "allZdNum";
			String onlineKey = "onlineZdNum";
			if (!itemList.isEmpty()){
				//设备总数
				totalDataMap.put(allKey, itemList.size());
				Integer onlineNum = new Integer(0);
				for (int i=0; i<itemList.size(); i++){
					ClZdgl item = itemList.get(i);

					if ("00".equals(item.getZxzt()) || "10".equals(item.getZxzt())){
						onlineNum++;
					}
				}
				//在线数量
				totalDataMap.put(onlineKey, onlineNum);
			}else{
				//设备总数
				totalDataMap.put(allKey, 0);
				//在线数量
				totalDataMap.put(onlineKey, 0);
			}

			//总里程,总时长
			sql = "select sum(r_jszsc) as times,sum(r_lczs) as miles from cl_zdgl_report m where m.jgdm like '"+jgxx.getJgdm()+"%'";
			if (StringUtils.isNotBlank(tmpValue)){
				sql += " and m.r_zdbh in (" + tmpValue + " )";
			}
			Map<String, Object> data = jdbcTemplate.queryForMap(sql);
			//统计总里程,总时长
			totalDataMap.put("times", (data.get("times") == null ? 0 : (int)Float.parseFloat(data.get("times").toString())));
			totalDataMap.put("miles", (data.get("miles") == null ? 0 : (int)Float.parseFloat(data.get("miles").toString())));
			//用户出行时间比例，用户驾驶速度占比，用户驾驶行为数据[平均行程，急转弯、加速、减速、刹车，超速]
			//用户驾驶速度占比，划分几个速度区间来统计，只统计7天内数据，GPS历史表数据太多
			//统计速度区间[0-25],[25-50],[50-75],[75-100],[100以上]
			sql = "SELECT sum( CASE WHEN m.yxsd > 0 AND m.yxsd <= 25 THEN 1 ELSE 0 END ) AS speed25," +
				  "sum( CASE WHEN m.yxsd > 25 AND m.yxsd <= 50 THEN 1 ELSE 0 END ) AS speed50, " +
				  "sum( CASE WHEN m.yxsd > 50 AND m.yxsd <= 75 THEN 1 ELSE 0 END ) AS speed75, " +
				  "sum( CASE WHEN m.yxsd > 75 AND m.yxsd <= 100 THEN 1 ELSE 0 END ) AS speed100, " +
				  "sum( CASE WHEN m.yxsd > 100 THEN 1 ELSE 0 END ) AS speed100s " +
				  " from cl_gps_ls m JOIN cl_zdgl c ON m.zDBH = c.zdbh " +
				  " WHERE c.jgdm like '"+jgxx.getJgdm()+"%' and c.jhzt='20' and m.cjsj>='"+DateTime.now().plusDays(-7).toString("yyyy-MM-dd")+" 00:00:00'";

			//统计速度值
			totalDataMap.put("speed25", (data.get("speed25") == null ? 0 : Integer.parseInt(data.get("speed25").toString())));
			totalDataMap.put("speed50", (data.get("speed50") == null ? 0 : Integer.parseInt(data.get("speed50").toString())));
			totalDataMap.put("speed75", (data.get("speed75") == null ? 0 : Integer.parseInt(data.get("speed75").toString())));
			totalDataMap.put("speed100", (data.get("speed100") == null ? 0 : Integer.parseInt(data.get("speed100").toString())));
			totalDataMap.put("speed100s", (data.get("speed100s") == null ? 0 : Integer.parseInt(data.get("speed100s").toString())));
		}catch(Exception e){
			totalDataMap.put("allZdNum", 0);
			totalDataMap.put("onlineZdNum", 0);
			totalDataMap.put("times", 0);
			totalDataMap.put("miles", 0);
			totalDataMap.put("speed25", 0);
			totalDataMap.put("speed50", 0);
			totalDataMap.put("speed75", 0);
			totalDataMap.put("speed100", 0);
			totalDataMap.put("speed100s", 0);
			errorLog.error("统计数据发生异常", e);
		}

		return ApiResponse.success(totalDataMap);
	}

    @Override
    public ApiResponse<List<Map<String, Object>>> getRecentXc(String deviceId, Page<ClXc> page) {
		RuntimeCheck.ifBlank(deviceId, "请输入终端编号");
		SimpleCondition condition = new SimpleCondition(ClXc.class);
		condition.setOrderByClause( " xc_kssj desc");
		condition.eq(ClXc.InnerColumn.clZdbh,deviceId);
		page.setPageNum(1);
		page.setPageSize(3);
		PageInfo<ClXc> pageInfo = xcService.findPage(page, condition);
		if(CollectionUtils.isEmpty(pageInfo.getList())){
			return ApiResponse.success(new ArrayList<>());
		}
		List<ClXc> xcList = pageInfo.getList();
		List<Map<String,Object>> list = new ArrayList<>(pageInfo.getList().size());
		if (xcList.size() == 0){
			return ApiResponse.success(list);
		}
		for (ClXc xc : xcList) {
			if (StringUtils.isEmpty(xc.getXcStartEnd())){
				continue;
			}
			String[] startAndEndPoint = xc.getXcStartEnd().split(",");
			String startPoint = startAndEndPoint[0].replace("-",",");
			String endPoint = startAndEndPoint[1].replace("-",",");
			String distance = "0";
			if(startAndEndPoint.length >= 3){
				distance = startAndEndPoint[2];
			}
			Map<String,Object> map = new HashMap<>();
			map.put("jsjps",endPoint);
			map.put("ksjps",startPoint);
			map.put("kssj",xc.getXcKssj());
			map.put("jssj",xc.getXcJssj());
			map.put("distance",distance);
			map.put("startAddress",filterAddress(xc.getStartAddress()));
			map.put("endAddress",filterAddress(xc.getEndAddress()));
			long sc = 0 ;
			try {
				Date startDate = DateUtils.getDate(xc.getXcKssj(),"yyyy-MM-dd HH:mm:ss");
				Date endDate = DateUtils.getDate(xc.getXcJssj(),"yyyy-MM-dd HH:mm:ss");
				sc = endDate.getTime() - startDate.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			map.put("sc",sc);
			list.add(map);
		}
		return ApiResponse.success(list);
    }
	private static String filterAddress(String s){
		if (StringUtils.isBlank(s)) return null;
		String[] key = {"区","县","市","省"};
		for (String s1 : key) {
			int index = s.lastIndexOf(s1);
			if (index >= 0 && index != s.length() -1){
				s = s.substring(index+1);
			}
		}
		return s;
	}

}
