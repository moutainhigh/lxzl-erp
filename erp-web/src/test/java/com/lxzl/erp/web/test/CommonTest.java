package com.lxzl.erp.web.test;

import com.lxzl.erp.common.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-09 14:03
 */
public class CommonTest {
    public static void main(String[] args) {
        Calendar date1 = Calendar.getInstance();
        date1.setTime(new Date());
        date1.set(Calendar.MONTH, 10);
        date1.set(Calendar.DAY_OF_MONTH, 13);
        Calendar date2 = Calendar.getInstance();
        date2.setTime(new Date());
        int space = DateUtil.getMonthSpace(date1.getTime(), date2.getTime());
    }
}
