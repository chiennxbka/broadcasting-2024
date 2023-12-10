package com.mintpot.broadcasting.common.utils;

import com.mintpot.broadcasting.common.enums.ErrorCode;
import com.mintpot.broadcasting.common.exception.BusinessException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    public static Date atBusinessHour(Date date, long hour, long minute) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime timeOfDay = localDateTime.with(LocalTime.MIN).plusHours(hour).plusMinutes(minute);
        return localDateTimeToDate(timeOfDay);
    }

    public static long[] getTimeBusiness(Date time) {
        Calendar calendar = Calendar.getInstance();
        if (time != null) {
            calendar.setTime(time);
        } else {
            return new long[]{8, 0};
        }
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new long[]{hour, minute};
    }

    public static int compareHour(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(1970, Calendar.JANUARY, 1);
        Calendar c2 =  Calendar.getInstance();
        c2.setTime(d2);
        c2.set(1970, Calendar.JANUARY, 1);
        d1 = c1.getTime();
        d2 = c2.getTime();
        return d1.compareTo(d2);
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Long calculateDaysAgo(Date past) {
        if (past != null) {
            Date now = DateUtils.getSystemDate();
            return TimeUnit.MILLISECONDS.toDays(now.getTime() - DateUtils.atStartOfDay(past).getTime());
        }
        return null;
    }

    public static Date getSystemDate() {
        ZoneId zid = ZoneId.systemDefault();
        return Date.from(ZonedDateTime.now(zid).toInstant());
    }

    public static void validDateRange(Date startDate, Date endDate) {
        if (startDate != null && endDate != null && startDate.after(endDate))
            throw new BusinessException(ErrorCode.DATE_RANGE_INVALID);
    }

    public static Date getFirstDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static Date getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static Date getLastDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static Date getLastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
}
