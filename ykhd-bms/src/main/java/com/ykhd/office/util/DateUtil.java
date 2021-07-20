package com.ykhd.office.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理相关
 */
public final class DateUtil {

	private static SimpleDateFormat year_format = new SimpleDateFormat("yyyy");
	private static SimpleDateFormat year_month_format = new SimpleDateFormat("yyyy-MM");
	private static SimpleDateFormat short_date_format = new SimpleDateFormat("yyyy-MM-dd");
	private static Calendar weekCalendar;
	static {
		weekCalendar = Calendar.getInstance();
		weekCalendar.setFirstDayOfWeek(Calendar.MONDAY);
	}
	
	/**
	 * yyyy-MM-dd --> date
	 */
	public static Date yyyyMMdd2date(String str) {
		try {
			return short_date_format.parse(str);
		} catch (ParseException e) {
			throw new IllegalArgumentException("日期格式错误：" + str);
		}
	}
	
	/**
	 * date --> yyyy-MM-dd
	 */
	public static String date2yyyyMMdd(Date date) {
		return short_date_format.format(date);
	}
	
	/**
	 * date --> yyyy
	 */
	public static String date2yyyy(Date date) {
		return year_format.format(date);
	}
	
	/**
	 * date --> yyyy-MM@W （月内周，从周一算新的一周）
	 */
	public static String date2yearMonthWeek(Date date) {
		weekCalendar.setTime(date);
		return year_month_format.format(date) + "@" + weekCalendar.get(Calendar.WEEK_OF_MONTH);
	}
	
	/**
	 * 两个日期之间相差的天数
	 */
	public static int intervalDays(Date begin, Date end) {
		int days = (int) ((end.getTime() - begin.getTime()) / 86400000);
		return days;
	}
	
	/**
	 * 日期查询条件下一天
	 */
	public static Date nextDay(String yyyyMMdd) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(yyyyMMdd2date(yyyyMMdd));
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}
	
	/**
	 * 日期查询条件下一天
	 */
	public static Date nextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}
	
	/**
	 * N个月前的日期
	 */
	public static Date monthsAgo(Date date, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -num);
		return calendar.getTime();
	}

	/**
	 * 本月第一天 yyyy-MM-dd
	 */
	public static String firstDayOfMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return date2yyyyMMdd(calendar.getTime());
	}



	
}
