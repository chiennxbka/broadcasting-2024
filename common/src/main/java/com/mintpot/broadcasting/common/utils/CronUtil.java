package com.mintpot.broadcasting.common.utils;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
public class CronUtil {
    private final Date date;
    private final Calendar cal;
    private final String seconds = "0";
    private final String daysOfWeek = "?";

    private String minus;
    private String hours;
    private String daysOfMonth;
    private String months;
    private String years;

    public CronUtil(Date pDate) {
        this.date = pDate;
        cal = Calendar.getInstance();
        cal.setTime(pDate);
        this.generateCronExpression();
    }

    private void generateCronExpression() {
        this.hours = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        this.minus = String.valueOf(cal.get(Calendar.MINUTE));
        this.daysOfMonth = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        this.months = new SimpleDateFormat("MM").format(cal.getTime());
        this.years = String.valueOf(cal.get(Calendar.YEAR));
    }
}
