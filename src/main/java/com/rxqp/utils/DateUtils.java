package com.rxqp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static String getCurrentTime() {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	public static String getCurrentTime(String format) {
		String returnStr = null;
		SimpleDateFormat f = new SimpleDateFormat(format);
		Date date = new Date();
		returnStr = f.format(date);
		return returnStr;
	}

	public static String dateToStr(Date date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String returnStr = f.format(date);
		return returnStr;
	}

	public static String dateToStr(Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		String returnStr = f.format(date);
		return returnStr;
	}

	public static Date strToDate(String str) {
		Date date = null;
		String formate = "yyyy-MM-dd";
		SimpleDateFormat f = new SimpleDateFormat(formate);
		try {
			date = f.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date strToDate(String str, String formate) {
		Date date = null;
		SimpleDateFormat f = new SimpleDateFormat(formate);
		try {
			date = f.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取相对今天的某一天，diff为1表示明天，-1表示昨天，以此类推
	 * 
	 * @param diff
	 * @return
	 */
	public static Date getSomeDayToToday(int diff) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, diff);
		return c.getTime();
	}

	public static int getDiffDayNum(Date beforDay, Date afterDay) {
		long diff = afterDay.getTime() - beforDay.getTime();
		int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
		return diffDays;
	}

	public static long getDiffTime(Date date1,Date date2){
		return date1.getTime() - date2.getTime();
	}

	public static void main(String[] args) {
		// System.out.println(dateToStr(getSomeDayToToday(0),
		// "yyyy-MM-dd 00:00:00"));
		// System.out.println(dateToStr(getSomeDayToToday(1),
		// "yyyy-MM-dd 00:00:00"));
		// System.out.println(getCurrentTime("yyyy-MM-dd HH:mm:ss"));
		// System.out.println(System.currentTimeMillis() / 1000);
		// System.out.println(strToDate("2016-08-17"));
		System.out.println(getDiffDayNum(strToDate("2017-01-01"),
				strToDate("2017-01-09")));
	}
}
