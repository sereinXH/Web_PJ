package com.example.backend0.util;

import java.sql.Date;
import java.util.Calendar;

/**
 * @ClassName DateUtil
 * @Description
 **/
public class DateUtil {
    public static Date getSevenDaysAgo(Date givenDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(givenDate);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        return new Date(calendar.getTimeInMillis());
    }
}
