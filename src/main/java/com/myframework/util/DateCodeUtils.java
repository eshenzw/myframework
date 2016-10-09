package com.myframework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateCodeUtils
{
	private static final Logger logger = LoggerFactory.getLogger(DateCodeUtils.class);
	// 今日
	public static final int TODAY = 1;
	// 昨日
	public static final int YESTERDAY = 2;
	// 本周
	public static final int THIS_WEEK = 3;
	// 上周
	public static final int LAST_WEEK = 4;
	// 本月
	public static final int THIS_MONTH = 5;
	// 上月
	public static final int LAST_MONTH = 6;
	// 最近七天
	public static final int LAST_SEVEN_DAYS = 7;
	// 最近30天
	public static final int LAST_THIRTY_DAYS = 8;
	// 今年
	public static final int THIS_YEAR = 9;
	// 去年
	public static final int LAST_YEAR = 10;

	/**
	 * 通过日期编码获取日期查询Map
	 * 
	 * @param dateCode
	 * @return
	 */
	public static Map<String, Object> getQueryDateMapByDateCode(int dateCode)
	{

		int dateCodeInt = dateCode;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		Calendar todayCal = Calendar.getInstance();
		switch (dateCodeInt)
		{
			case TODAY:
				queryMap.put("beginDate", DateUtil.date2String("yyyy-MM-dd", new Date()));
				queryMap.put("endDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				logger.debug("---------------今天-----------------");
				break;
			case YESTERDAY:
				todayCal.add(Calendar.DAY_OF_YEAR, -1);
				queryMap.put("endDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				queryMap.put("beginDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				logger.debug("---------------昨天-----------------");
				break;
			case LAST_SEVEN_DAYS:
				queryMap.put("endDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				todayCal.add(Calendar.DAY_OF_YEAR, -6);
				queryMap.put("beginDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				logger.debug("---------------最近七天-----------------");
				break;
			case LAST_THIRTY_DAYS:
				todayCal.add(Calendar.DAY_OF_YEAR, -29);
				queryMap.put("beginDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				queryMap.put("endDate", DateUtil.date2String("yyyy-MM-dd", new Date()));
				logger.debug("---------------最近30天-----------------");
				break;
			case THIS_WEEK:
				queryMap.put("beginDate", getFirstDayOfThisWeek());
				queryMap.put("endDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				logger.debug("---------------本周-----------------");
				break;
			case LAST_WEEK:
				queryMap.put("beginDate", getFirstDayOfLastWeek());
				queryMap.put("endDate", getLastDayOfLastWeek());
				logger.debug("---------------上周-----------------");
				break;
			case THIS_MONTH:
				queryMap.put("beginDate", getFirstDayOfThisMonth());
				queryMap.put("endDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				logger.debug("---------------本月-----------------");
				break;
			case LAST_MONTH:
				queryMap.put("beginDate", getFirstDayOfLastMonth());
				queryMap.put("endDate", getLastDayOfLastMonth());
				logger.debug("---------------上月-----------------");
				break;
			case THIS_YEAR:
				queryMap.put("beginDate", getFirstDayOfThisYear());
				queryMap.put("endDate", DateUtil.date2String("yyyy-MM-dd", todayCal.getTime()));
				logger.debug("---------------今年-----------------");
				break;
			case LAST_YEAR:
				queryMap.put("beginDate", getFirstDayOfLastYear());
				queryMap.put("endDate", getLastDayOfLastYear());
				logger.debug("---------------去年-----------------");
				break;
			default:
				break;
		}
		return queryMap;
	}

	/**
	 * 去年的最后一天
	 * 
	 * @return
	 */
	private static String getLastDayOfLastYear()
	{
		// 获取去年的最后一天
		Calendar calYear = Calendar.getInstance();
		calYear.add(Calendar.YEAR, -1);
		calYear.set(Calendar.MONTH, Calendar.DECEMBER);
		calYear.set(Calendar.DAY_OF_MONTH, 31);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_month = sdf.format(calYear.getTime());
		return last_month;
	}

	/**
	 * 获取去年的第一天
	 * 
	 * @return
	 */
	private static String getFirstDayOfLastYear()
	{
		// 获取去年的第一天
		Calendar calYear = Calendar.getInstance();
		calYear.add(Calendar.YEAR, -1);
		calYear.set(Calendar.DAY_OF_YEAR, calYear.getMinimum(Calendar.DAY_OF_YEAR));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_month = sdf.format(calYear.getTime());
		return last_month;
	}

	// 获取今年的第一天
	private static String getFirstDayOfThisYear()
	{
		// 获取今年的第一天
		Calendar calYear = Calendar.getInstance();
		calYear.set(Calendar.DAY_OF_YEAR, calYear.getMinimum(Calendar.DAY_OF_YEAR));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_month = sdf.format(calYear.getTime());
		return last_month;
	}

	public static String getFirstDayOfThisWeek()
	{
		// 获取本周周一的日期
		Calendar calWeek = Calendar.getInstance();
		// 先往前减一天 避免“周日”去到下周一的bug
		calWeek.add(Calendar.DAY_OF_WEEK, -1);
		calWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_week = sdf.format(calWeek.getTime());
		return last_week;
	}

	public static String getFirstDayOfLastWeek()
	{
		// 获取上周周一的日期
		Calendar calWeek = Calendar.getInstance();
		calWeek.add(Calendar.DATE, -7);
		calWeek.add(Calendar.DAY_OF_WEEK, -1);
		calWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_week = sdf.format(calWeek.getTime());
		return last_week;
	}

	public static String getLastDayOfLastWeek()
	{
		// 获取上周周日的日期
		Calendar calWeek = Calendar.getInstance();
		// calWeek.add(Calendar.DATE, -1);
		calWeek.add(Calendar.DAY_OF_WEEK, -1);
		calWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_week = sdf.format(calWeek.getTime());
		return last_week;
	}

	public static String getFirstDayOfThisMonth()
	{
		// 获取本月第一天的日期
		Calendar calMonth = Calendar.getInstance();
		calMonth.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_month = sdf.format(calMonth.getTime());
		return last_month;
	}

	public static String getFirstDayOfLastMonth()
	{
		// 获取上月第一天的日期
		Calendar calMonth = Calendar.getInstance();
		calMonth.add(Calendar.MONTH, -1);
		calMonth.set(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_month = sdf.format(calMonth.getTime());
		return last_month;
	}

	public static String getLastDayOfLastMonth()
	{
		// 获取上月最后一天的日期
		Calendar calMonth = Calendar.getInstance();
		calMonth.add(Calendar.MONTH, -1);
		calMonth.set(Calendar.DAY_OF_MONTH, calMonth.getMaximum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String last_month = sdf.format(calMonth.getTime());
		return last_month;
	}

	public static String getFirstDayOfLastNthWeek(int nth)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取上周周一的日期
		Calendar calWeek = Calendar.getInstance();
		calWeek.add(Calendar.DATE, -7 * nth);
		calWeek.add(Calendar.DAY_OF_WEEK, -1);
		calWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		String last_week = sdf.format(calWeek.getTime());
		return last_week;
	}

	/**
	 * nth代表上几周: 0代表本周最后一天， 1代表上周最后一天
	 * 
	 * @param nth
	 * @return
	 */
	public static String getLastDayOfLastNthWeek(int nth)
	{
		nth--;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取上周周日的日期
		Calendar calWeek = Calendar.getInstance();
		calWeek.add(Calendar.DATE, -7 * nth);
		calWeek.add(Calendar.DAY_OF_WEEK, -1);
		calWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String last_week = sdf.format(calWeek.getTime());
		return last_week;
	}

	public String getThisDate()
	{
		Calendar calWeek = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String this_date = sdf.format(calWeek.getTime());
		return this_date;
	}

	// 当前时间往前倒31天
	public List<String> get31Date()
	{
		List<String> list = new ArrayList<String>();
		for (int i = 30; i > -1; i--)
		{
			Calendar calWeek = Calendar.getInstance();
			calWeek.add(Calendar.DATE, -i);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			String day = sdf.format(calWeek.getTime()).toString();
			list.add(day);
		}
		return list;
	}

	public String getDateByNum(int num)
	{
		Calendar calWeek = Calendar.getInstance();
		calWeek.add(Calendar.DATE, num);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(calWeek.getTime());
		return date;
	}

	public static void main(String[] args)
	{
		Map<String, Object> map = getQueryDateMapByDateCode(LAST_YEAR);
		System.out.println(map.get("beginDate") + "--" + map.get("endDate"));
	}

	/**
	 * 填充0，到没有数据返回的日期中
	 * 
	 * @param dateStrMap
	 *            //查询参数
	 * @param resultMap
	 *            //结果
	 */
	public static void fill0ToResultStatMap(Map<String, Object> dateStrMap, Map<String, Object> resultMap)
	{

	}

	/**
	 * @throws ParseException
	 * 
	 */
	public static void addOneDayToEndDate(Map<String, Object> dateStrMap)
	{
		Date endDate;
		try
		{
			endDate = DateUtil.string2Date("yyyy-MM-dd", dateStrMap.get("endDate").toString());
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.add(Calendar.DATE, 1);
		dateStrMap.put("endDate", DateUtil.date2String("yyyy-MM-dd", cal.getTime()));
	}

}