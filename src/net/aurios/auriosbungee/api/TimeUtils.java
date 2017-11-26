package net.aurios.auriosbungee.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	
	enum TimeUnit {
		UNKNOWN, SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR;
	}
	
	//Returns a time unit from a string. s -> second(s)...
	public TimeUnit timeUnit(String from) {
		if(from.startsWith("s")) return TimeUnit.SECOND;
		else if(from.startsWith("min")) return TimeUnit.MINUTE;
		else if(from.startsWith("h")) return TimeUnit.HOUR;
		else if(from.startsWith("w")) return TimeUnit.DAY;
		else if(from.startsWith("mo")) return TimeUnit.MONTH;
		else if(from.startsWith("y")) return TimeUnit.YEAR;
		else return TimeUnit.UNKNOWN;
	}
	
	@SuppressWarnings("deprecation")
	public Date addToDate(String date, TimeUnit timeUnit, int amount) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(date);
		if(timeUnit == TimeUnit.SECOND) d.setSeconds(d.getSeconds() + amount);
		else if(timeUnit == TimeUnit.MINUTE) d.setMinutes(d.getMinutes() + amount);
		else if(timeUnit == TimeUnit.HOUR) d.setHours(d.getHours() + amount);
		else if(timeUnit == TimeUnit.DAY) d.setHours(d.getHours() + (amount*24));
		else if(timeUnit == TimeUnit.WEEK) d.setHours(d.getHours() + (amount*24*7));
		else if(timeUnit == TimeUnit.MONTH) d.setMonth(d.getMonth() + amount);
		else if(timeUnit == TimeUnit.YEAR) d.setYear(d.getYear() + amount);
		
		return d;
	}

}
