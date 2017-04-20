package com.flow.booktrade.service.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeUtil {

	public static ZonedDateTime getCurrentTime(){
		return ZonedDateTime.now();
	}
	
	/**
	 * Find difference between two ZonedDateTimes
	 * @param currentTime
	 * @param pastTime
	 * @param unit
	 * @return
	 */
	public static long getZonedDateTimeDifference(ZonedDateTime currentTime, ZonedDateTime pastTime, ChronoUnit unit){
		return unit.between(pastTime, currentTime);
	}
	
	/**
	 * Return formatted string of time passed
	 * @param currentTime
	 * @param pastTime
	 * @return
	 */
	public static String getZonedDateTimeDifferenceFormatString(ZonedDateTime currentTime, ZonedDateTime pastTime){
		String formatted = "";
		if(pastTime == null || currentTime == null){
			return "";
		}
		long day = ChronoUnit.DAYS.between(pastTime, currentTime);
		long hour = ChronoUnit.HOURS.between(pastTime, currentTime);
		long minute = ChronoUnit.MINUTES.between(pastTime, currentTime);
		if(day >= 1){
			formatted = day + " day";
			if(day > 1){
				formatted = formatted + "s";
			}
			return formatted;
		} else if(hour >= 1){
			formatted = hour + " hour";
			if(hour > 1){
				formatted = formatted + "s";
			}
			return formatted;
		} else {
			formatted = minute + " minute";
			if(minute > 1){
				formatted = formatted + "s";
			}
		}
		
		return formatted;
	}
	
	 /**
     * Creates an {@link Instant} from {@code java.util.Date} or it's subclasses. Null-safe.
     */
    public static Instant asInstant(Date date) {
        if (date == null)
            return null;
        else
            return Instant.ofEpochMilli(date.getTime());
    }

    /**
     * Calls {@link #asZonedDateTime(Date, ZoneId)} with the system default time zone.
     */
    public static ZonedDateTime asZonedDateTime(Date date) {
        return asZonedDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Creates {@link ZonedDateTime} from {@code java.util.Date} or it's subclasses. Null-safe.
     */
    public static ZonedDateTime asZonedDateTime(Date date, ZoneId zone) {
        if (date == null)
            return null;
        else
            return asInstant(date).atZone(zone);
    }
}
