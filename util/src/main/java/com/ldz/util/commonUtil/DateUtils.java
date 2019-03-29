package com.ldz.util.commonUtil;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.time.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
	/**
	 * 返回格式：2007-08-14
	 * @return
	 */
	public static String getToday(){
		String time = "";
		time = getToday("yyyy-MM-dd");
		return time;
	}
	/**
	 * 
	 * @param format 根据指定的格式时间类型返回当前时间
	 * @return
	 */
	public static String getToday(String format){
		return getDateStr(Calendar.getInstance().getTime(),format);
	}
	
	/**
	 * 日期转字符
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateStr(Date date,String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static Date getDate(String date,String format) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return sf.parse(date);
	}
	
	/**
	 * @param millis
	 * @return
	 */
	public static Date parseMills(long millis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	
	public static Date getDateToday(String format) throws ParseException{
		String str = getDateStr(Calendar.getInstance().getTime(),format);
		return getDate(str,format);
	}
	
	public static long getTimes(){
		return new Date().getTime();
	}


	/**
	 * 根据所有传入的参数进行时间计算
	 * @param time 时间
	 * @param operator 运算符
	 * @param second 计算参数    秒
	 */
	public static String calculateTime(LocalDateTime time,String operator,int second){

		if(StringUtils.equals(operator,"+")){
			time = time.plusSeconds(second);
		}else if(StringUtils.equals(operator, "-")){
			time = time.minusSeconds(second);
		}
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return time.format(df);

	}

	public static long get_D_Plaus_1(Calendar c) {
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		return c.getTimeInMillis();
	}

	public static String plusYear(LocalDateTime time, int year){
		LocalDateTime localDateTime = time.plusYears(year);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return localDateTime.format(dateTimeFormatter);
	}

	public static void main(String[] args) throws ParseException {

		Date date = DateUtils.getDate("2018-11-23", "yyyy-MM-dd");
		LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		String s = plusYear(dateTime, 1);
		System.out.println(s);

	}




	/**
	 * 根据传入的开始时间和结束时间，获取时间段
	 * @param kssj
	 * @param jssj
	 * @return
	 */
	public static List<Date> createDateList(String kssj,String jssj) throws ParseException {
		List<Date> dates = new ArrayList<>();


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(kssj));

		for (long d = cal.getTimeInMillis(); d <= sdf.parse(jssj).getTime(); d = get_D_Plaus_1(cal)) {
			String format = sdf.format(d);
			Date parse = sdf.parse(format);
			dates.add(parse);
		}

		return dates;
	}



	public static String getNowTime() {
		return getToday("yyyy-MM-dd HH:mm:ss");
	}
	public static boolean checkTxnTime(String startTime, String endTime) {
		String nowDate = getDateStr(new Date(), "HH:mm:ss");
		if(nowDate.compareTo(startTime) >= 0 && nowDate.compareTo(endTime) <= 0){
			return true;
		}
		return false;
	}

}
