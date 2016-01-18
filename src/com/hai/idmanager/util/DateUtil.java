package com.hai.idmanager.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
	
	public static String getDateByYMD(long timeMillis){
		String dateStr = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dateStr = sdf.format(new Date(timeMillis));
		
		return dateStr;
	}
	
	/**
	 * 根据传入的格式生成日期
	 * @param timeMillis
	 * @param dateFormat	比如"yyyy-MM-dd HH:mm"
	 * @return
	 */
	public static String getDateByFormat(long timeMillis, String dateFormat){
		String dateStr = null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		dateStr = sdf.format(new Date(timeMillis));
		
		return dateStr;
	}
	
	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public static int getCurrentMonthDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeekDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	public static int getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

}
