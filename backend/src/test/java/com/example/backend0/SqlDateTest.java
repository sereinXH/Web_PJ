package com.example.backend0;

import com.example.backend0.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

/**
 * @ClassName SqlDateTest
 * @Description
 **/
public class SqlDateTest {
    @Test
    public void test(){
        Date date=new Date(System.currentTimeMillis());
        System.out.println(date);
        System.out.println(DateUtil.getSevenDaysAgo(date));
    }
}
