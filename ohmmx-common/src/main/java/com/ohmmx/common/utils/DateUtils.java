package com.ohmmx.common.utils;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

public final class DateUtils extends DateFormatter {
	/** Date Format String: "yyyy-MM" */
	public static final String PATTERN_yyyy_MM = "yyyy-MM";
	/** Date Format String: "yyyyMM" */
	public static final String PATTERN_yyyyMM = "yyyyMM";

	public static String yyyy_MM(final Date date) {
		return format(PATTERN_yyyy_MM, date);
	}

	public static String yyyy_MM(final Calendar calendar) {
		return format(PATTERN_yyyy_MM, calendar);
	}

	public static String yyyy_MM() {
		return format(PATTERN_yyyy_MM, now());
	}

	public static Date yyyy_MM(final String date) {
		return parse(PATTERN_yyyy_MM, date);
	}

	public static String yyyyMM(final Date date) {
		return format(PATTERN_yyyyMM, date);
	}

	public static String yyyyMM(final Calendar calendar) {
		return format(PATTERN_yyyyMM, calendar);
	}

	public static String yyyyMM() {
		return format(PATTERN_yyyyMM, now());
	}

	public static Date yyyyMM(final String date) {
		return parse(PATTERN_yyyyMM, date);
	}

	public static boolean sameMonth(final Date from) {
		return sameMonth(from, now());
	}

	public static boolean sameMonth(final Date from, final Date to) {
		return startOfMonth(from).equals(startOfMonth(to));
	}

	public static String previousMonth(final String month) {
		Date monthDate = yyyy_MM(month);
		Date previousMonthDate = previousMonth(monthDate);
		return yyyy_MM(previousMonthDate);
	}

	/**
	 * 判断第一个时间是否在第二个时间之前
	 */
	public static boolean isBefore(Date source, Date target) {
		DateTime dtSource = new DateTime(source);
		DateTime dtTarget = new DateTime(target);
		return dtSource.isBefore(dtTarget);
	}

	/**
	 * 判断第一个时间是否在第二个时间之后
	 */
	public static boolean isAfter(Date source, Date target) {
		DateTime dtSource = new DateTime(source);
		DateTime dtTarget = new DateTime(target);
		return dtSource.isAfter(dtTarget);
	}

	/**
	 * 第一个时间在第二个时间之后或者同一天
	 */
	public static boolean isNotBefore(Date source, Date target) {
		return isAfter(source, target) || DateFormatter.sameDay(source, target);
	}

	/**
	 * 第一个时间在第二个时间之前或者同一天
	 */
	public static boolean isNotAfter(Date source, Date target) {
		return isBefore(source, target) || DateFormatter.sameDay(source, target);
	}

	/**
	 * 日期是当月的第几天
	 */
	public static int dayOfMonth(Date day) {
		DateTime dt = new DateTime(day);
		return dt.dayOfMonth().get();
	}
}
