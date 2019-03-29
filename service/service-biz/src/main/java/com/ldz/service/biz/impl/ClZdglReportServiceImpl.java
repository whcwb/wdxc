package com.ldz.service.biz.impl;

import com.ldz.dao.biz.mapper.ClZdglReportMapper;
import com.ldz.dao.biz.model.ClZdglReport;
import com.ldz.service.biz.interfaces.ClZdglReportService;
import com.ldz.sys.base.BaseServiceImpl;
import com.ldz.util.bean.ApiResponse;
import com.ldz.util.bean.SimpleCondition;
import com.ldz.util.exception.RuntimeCheck;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.Mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ClZdglReportServiceImpl extends BaseServiceImpl<ClZdglReport, String> implements ClZdglReportService {

	@Autowired
	private ClZdglReportMapper baseMapper;

	@Override
	protected Mapper<ClZdglReport> getBaseMapper() {
		return baseMapper;
	}

	@Override
	public void saveBatch(List<ClZdglReport> list) {
		list.forEach(clZdglReport -> {
			clZdglReport.setId(genId());
		});
		baseMapper.insertList(list);
	}

    @Override
    public ApiResponse<List<List<String>>> getWeekMile(String deviceId) {

		ApiResponse<List<List<String>>> re = new ApiResponse<>();
		List<List<String>> result = new ArrayList<>();

		RuntimeCheck.ifBlank(deviceId, "请选择终端编号");
		// 先查看当天的日期 ， 找出本周的日期时间

		DateTime time = DateTime.now();
		DateTime.Property week = time.dayOfWeek();
		int weekValue = week.get();


		// 只需要找到星期一的日期 和 星期天的日期即可
		String sun = time.plusDays(7 - weekValue).toString("yyyy-MM-dd") ;
		String mon = time.minusDays(weekValue - 1 ).toString("yyyy-MM-dd");

		List<String> da = new ArrayList<>();
		for(int i = 1; i <= 7;i++){
			da.add(time.minusDays(weekValue - i ).toString("yyyy-MM-dd"));
		}

		// 查询 周一到周日的里程
		SimpleCondition condition = new SimpleCondition(ClZdglReport.class);
		condition.eq(ClZdglReport.InnerColumn.rZdbh, deviceId);
		condition.gte(ClZdglReport.InnerColumn.rTjrq , mon);
		condition.lte(ClZdglReport.InnerColumn.rTjrq, sun);
		List<ClZdglReport> reports = findByCondition(condition);
		Map<String, String> collect ;
		String totalMile = "0";
		if(CollectionUtils.isEmpty(reports)){
			collect = new HashMap<>();
		}else {
			collect = reports.stream().collect(Collectors.toMap(ClZdglReport::getrTjrq, p -> p.getrLczs()));
			if(reports.size() > 1 ){
				totalMile = 	reports.stream().map(ClZdglReport::getrLczs).reduce((s, s2) -> Double.parseDouble(s) + Double.parseDouble(s2) + "").get();
			}else{
				totalMile = reports.get(0).getrLczs();
			}
		}
		re.setMessage(totalMile);

		for (String d : da) {
			List<String> m = new ArrayList<>();
			m.add(d);
			if(collect.containsKey(d)){
				m.add(collect.get(d));
			}else {
				m.add("0");
			}
			result.add(m);
		}
		re.setResult(result);
		return re;
    }

	@Override
	public ApiResponse<List<List<String>>> getMonthMile(String deviceId) {
		ApiResponse<List<List<String>>> re = new ApiResponse<>();
		List<List<String>> result = new ArrayList<>();
		RuntimeCheck.ifBlank(deviceId, "请选择终端");
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setGroupingUsed(false);
		format.setMaximumFractionDigits(1);

		// 查询当前年的月份
		DateTime time = DateTime.now();
		int ofMonth = time.getMonthOfYear();
		List<String> da = new ArrayList<>();
		for(int i = 1; i<=12; i++){
			da.add(time.minusMonths(ofMonth - i).toString("yyyy-MM"));
		}

		int year = time.getYear();
		String start = year + "-01-01";
		String end = year + "-12-31";
		SimpleCondition condition = new SimpleCondition(ClZdglReport.class);
		condition.eq(ClZdglReport.InnerColumn.rZdbh , deviceId);
		condition.gte(ClZdglReport.InnerColumn.rTjrq, start);
		condition.lte(ClZdglReport.InnerColumn.rTjrq, end);
		List<ClZdglReport> reports = findByCondition(condition);
		String totalMile = "0";
		if(CollectionUtils.isEmpty(reports)){
			re.setMessage(totalMile);
			for (String d : da) {
				List<String> m = new ArrayList<>();
				m.add(d);
				m.add("0");
				result.add(m);
			}
			re.setResult(result);
			return re;
		}
		Map<String, List<ClZdglReport>> collect = reports.stream().collect(Collectors.groupingBy(clZdglReport -> clZdglReport.getrTjrq().substring(0, 7)));
		totalMile = reports.stream().map(ClZdglReport::getrLczs).reduce((s, s2) -> Double.parseDouble(s) + Double.parseDouble(s2) + "").get();


		for (String d : da) {
			List<String> m = new ArrayList<>();
			m.add(d);
			if(collect.containsKey(d)){
				String s1 = collect.get(d).stream().map(ClZdglReport::getrLczs).reduce((s, s2) -> Double.parseDouble(s) + Double.parseDouble(s2) + "").get();
				m.add(format.format(Double.parseDouble(s1)));
			}else {
				m.add("0");
			}
			result.add(m);
		}
		re.setMessage(format.format(Double.parseDouble(totalMile)));
		re.setResult(result);
		return re;
	}

	@Override
	public ApiResponse<List<List<String>>> getDayMile(String deviceId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

		ApiResponse<List<List<String>>> re = new ApiResponse<>();
		List<List<String>> result = new ArrayList<>();
		RuntimeCheck.ifBlank(deviceId, "请选择终端");

		DateTime now = DateTime.now();
		SimpleCondition condition = new SimpleCondition(ClZdglReport.class);
		condition.eq(ClZdglReport.InnerColumn.rZdbh ,deviceId);
		condition.eq(ClZdglReport.InnerColumn.rTjrq, now.toString("yyyy-MM-dd"));
		List<ClZdglReport> reports = findByCondition(condition);
		String totalMile = "0";
		if(CollectionUtils.isEmpty(reports)){
			re.setMessage(totalMile);
			for(int i = 0 ; i< 24; i++ ){
				List<String> m = new ArrayList<>();
				m.add(i+"");
				m.add("0");
				result.add(m);
			}
			re.setResult(result);
			return re;
		}

		ClZdglReport clZdglReport = reports.get(0);
		Class<? extends ClZdglReport> aClass = clZdglReport.getClass();

		for(int i = 0 ; i< 24; i++ ){
			Method method = aClass.getMethod("getLc"+i);
			String invoke = (String) method.invoke(clZdglReport);
			List<String> m = new ArrayList<>();
			m.add(i + "");
			m.add(invoke);
			totalMile = Double.parseDouble(totalMile) + Double.parseDouble(invoke) + "";
			result.add(m);
		}
		re.setMessage(totalMile);
		re.setResult(result);
		return re;
	}


	public static void main(String[] args) {


		DateTime time = DateTime.now();
		int ofMonth = time.getMonthOfYear();
		System.out.println(ofMonth);
		/*List<String> a = new ArrayList<>();
		a.add("11");
		String s1 = a.stream().reduce((s, s2) -> Double.parseDouble(s) + Double.parseDouble(s2) + "").get();
		System.out.println(s1);
		DateTime time = DateTime.now();
		System.out.println(time.getYear());*/
		/*DateTime.Property week = time.dayOfWeek();
		int weekValue = week.get();


		// 只需要找到星期一的日期 和 星期天的日期即可
		String sun = time.plusDays(7 - weekValue).toString("yyyy-MM-dd");
		String fri = time.minusDays(weekValue-1).toString("yyyy-MM-dd");
		System.out.println("星期天 "+ sun);
		System.out.println("星期一 " + fri);*/

	}


}