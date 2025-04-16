package com.fu.basedemo;

import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

public class CalendarTests {
    @Test
    public void test(){
        Date birthday = new Date(123 -65, Calendar.JANUARY,10);
        Calendar c = Calendar.getInstance();
        //oldAge获取到的是年龄
        c.set(c.get(Calendar.YEAR) - 65,Calendar.DECEMBER,31,23,59,59);
        Date limitBirthday = c.getTime();
        System.out.println(birthday);
        System.out.println(limitBirthday);
        System.out.println(birthday.getTime());
        System.out.println(limitBirthday.getTime());
        System.out.println(birthday.getTime() <= limitBirthday.getTime());
    }
}
