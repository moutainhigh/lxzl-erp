package com.lxzl.erp.common.util;

import com.lxzl.erp.common.constant.ErrorCode;
import com.lxzl.se.common.exception.BusinessException;


import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2017/1/23.
 * Time: 15:27.
 */
public class DateUtil {

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION);
        }
    }

    /**
     * 判断两个日期是否是同一天
     */
    public static boolean isSameDay(Date smdate, Date bdate) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            return smdate.equals(bdate);
        }catch (ParseException e){
            throw new BusinessException(ErrorCode.BUSINESS_EXCEPTION);
        }
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
    public static boolean isLeapYesr(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        if(year%4==0&&year%100!=0||year%400==0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取下月指定日期当天开始时间
     */
    public static Date getNextMonthDayStartTime(Date currentTime, int day) {
        currentTime = currentTime == null ? new Date() : currentTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.MONTH, 2);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        // 本月最后一天小于输入的
        if (calendar.get(Calendar.DATE) < day) {
            day = calendar.get(Calendar.DATE);
        }
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取该月有多少天
     */
    public static int getActualMaximum(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 该日期距离月底的天数
     */
    public static int betweenActualMaximumDays(Date currentTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int now = calendar.get(Calendar.DAY_OF_MONTH);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return actualMaximum - now;
    }

    /**
     * 该月1日距离day的天数
     */
    public static int betweenAppointDays(Date currentTime, int day) {

        currentTime = currentTime == null ? new Date() : currentTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        // 本月最后一天小于输入的
        if (calendar.getActualMaximum(Calendar.DATE) < day) {
            day = getActualMaximum(calendar.getTime());
        }
        return day;
    }

    /**
     * 两个日期相差月数
     *
     * @param date1 开始日期
     * @param date2 结束日期
     * @return 相差天数
     */
    public static int getMonthSpace(Date date1, Date date2) {
        int result = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        result = year * 12 + result;
        int days = c2.get(Calendar.DAY_OF_MONTH) - c1.get(Calendar.DAY_OF_MONTH);
        if (days < 0) {
            result--;
        }
        return result;
    }

    /**
     * 获取当前月的偏移月
     * @param offset 0-当月，1-下月，-1 上月，依次类推
     * @return
     */
    public static Date getMonthByCurrentOffset(Integer offset) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, offset);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        String day = format.format(cale.getTime());
        Date date = null;
        try {
            date = format.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取某月的偏移月
     * @param month 基准月
     * @param offset 0-当月，1-下月，-1 上月，依次类推
     * @return
     */
    public static Date getMonthByOffset(Date month , Integer offset) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.setTime(month);
        cale.add(Calendar.MONTH, offset);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        String day = format.format(cale.getTime());
        Date date = null;
        try {
            date = format.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 获取当前日期的偏移日
     * @param offset 0-当天，1-明天，-1 昨天，依次类推
     * @return
     */
    public static Date getDayByCurrentOffset(Integer offset) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.DAY_OF_MONTH, offset);
        String day = format.format(cale.getTime());
        Date date = null;
        try {
            date = format.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取某日期的偏移日
     * @param baseDate 基准日期
     * @param offset 0-当天，1-明天，-1 昨天，依次类推
     * @return
     */
    public static Date getDayByOffset(Date baseDate , Integer offset) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale.setTime(baseDate);
        cale.add(Calendar.DAY_OF_MONTH, offset);
        String day = format.format(cale.getTime());
        Date date = null;
        try {
            date = format.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取当前日期当年目前所有月
     * 如当前日期为2018-05-06，本接口返回的日期列表为2018-01-01，2018-02-01，2018-01-01，2018-023-01，2018-04-01，2018-05-01
     * @return Date
     */
    public static List<Date> getCurrentYearPassedMonth(){
        Date yearFirstDay = getCurrYearFirst();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(yearFirstDay);
        List<Date> dateList = new ArrayList<>();
        dateList.add(calendar.getTime());
        while(true){
            calendar.add(Calendar.MONTH,1);
            if(calendar.getTimeInMillis()>System.currentTimeMillis()){
                break;
            }
            dateList.add(calendar.getTime());
        }
        return dateList;
    }

    /**
     * 获取当前日期当年目前未到的所有月
     * 如当前日期为2018-05-06，本接口返回的日期列表为2018-06-01，2018-07-01，2018-08-01，2018-09-01，2018-10-01，2018-11-01，2018-12-01
     * @return Date
     */
    public static List<Date> getCurrentYearNoPassedMonth(){
        List<Date> dateList = new ArrayList<>();
        Date currentMonth = getMonthByCurrentOffset(0);
        Calendar thisMonth = Calendar.getInstance();
        thisMonth.setTime(currentMonth);
        Date nextMonth = getMonthByCurrentOffset(1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nextMonth);
        while (thisMonth.get(Calendar.YEAR)==calendar.get(Calendar.YEAR)){
            dateList.add(calendar.getTime());
            calendar.add(Calendar.MONTH,1);
        }
        return dateList;
    }
    /**
     * 获取当前日期当月目前所有天
     * @return Date
     */
    public static List<Date> getCurrentMonthPassedDay(){
        Date monthFirstDay = getMonthByCurrentOffset(0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthFirstDay);
        List<Date> dateList = new ArrayList<>();
        dateList.add(calendar.getTime());
        while(true){
            calendar.add(Calendar.DAY_OF_MONTH,1);
            if(calendar.getTimeInMillis()>System.currentTimeMillis()){
                break;
            }
            dateList.add(calendar.getTime());
        }
        return dateList;
    }
    /**
     * 获取当前日期当月目前未到的所有天
     * @return Date
     */
    public static List<Date> getCurrentMonthNoPassedDay(){

        List<Date> dateList = new ArrayList<>();
        Date currentDay = getDayByCurrentOffset(0);
        Calendar thisDay = Calendar.getInstance();
        thisDay.setTime(currentDay);
        Calendar nextDay = Calendar.getInstance();
        Date next = getDayByCurrentOffset(1);
        nextDay.setTime(next);
        while (thisDay.get(Calendar.MONTH)==nextDay.get(Calendar.MONTH)){
            dateList.add(nextDay.getTime());
            nextDay.add(Calendar.DAY_OF_MONTH,1);
        }
        return dateList;
    }

    /**
     * 获取给定日期月份第一天凌晨 Tue May 01 00:00:00 CST 2018
     * @param date
     * @return
     */
    public static Date getStartMonthDate(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.set(Calendar.HOUR_OF_DAY ,0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTime();
    }

    /**
     * 获取给定日期月份第一天凌晨 Thu May 31 23:59:59 CST 2018
     * @param date
     * @return
     */
    public static Date getEndMonthDate(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY ,23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTime();
    }

    /**
     * 获取当前月最后一天,开始时间
     *
     *
     * @param date
     * @return
     */
    public static  Date getEndByMonth(Date date) {
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 00);
        ca.set(Calendar.MINUTE, 00);
        ca.set(Calendar.SECOND, 00);
        ca.set(Calendar.MILLISECOND, 00);
        return ca.getTime();
    }

    /**
     * 获取月份
     *
     * @param date  时间
     * @return 月份
     * @throws ParseException
     */
    public static Integer getMounth(Date date) {
        if(date == null){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH)+1;
    }

    /**
     * 获取年份
     *
     * @param date  时间
     * @return 年份
     * @throws ParseException
     */
    public static Integer getYear(Date date) {
        if(date == null){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取月份和剩余天数
     *
     * @param startDay  开始时间
     * @param endDay  结束时间
     * @return 月份和剩余天数
     * @throws ParseException
     */
    public static String  getMonthAndDays(Date startDay,Date endDay) throws Exception {
        List<Date> monthList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        Date minDate = sdf.parse(sdf.format(startDay));
        min.setTime(minDate);
        Date newMinDate = min.getTime();

        Date maxDate = sdf.parse(sdf.format(endDay));
        max.setTime(maxDate);
        max.add(Calendar.DATE,1);

        Calendar curr = min;
        while (curr.before(max)) {
            monthList.add(curr.getTime());
            curr.add(Calendar.MONTH, 1);
        }

        Date newMaxDate = max.getTime();
        int monthLength = DateUtil.getMonthSpace(newMinDate, newMaxDate);
        int allDay = DateUtil.daysBetween(newMinDate, newMaxDate);
        int allMonthDays = 0;  //所有月的天数
        for (int i = 0; i < monthLength; i++) {
            Date date = monthList.get(i);
            allMonthDays = DateUtil.getActualMaximum(date) + allMonthDays;
        }

        return monthLength+"月"+(allDay - allMonthDays )+"天";
    }

    /**
     * <p>
     * 计算两个日期之间相隔的月天
     * </p>
     * <pre>
     *     所需参数示例及其说明
     *     参数名称 : 示例值 : 说明 : 是否必须
     * </pre>
     * @author daiqi
     * @date 2018/7/6 11:16
     * @param start
     * @param end
     * @return int[]  下标为0的为月数 下标为1的为天数
     */
    public static int[] getDiff(Date start, Date end) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH);
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        endCalendar.add(Calendar.DATE, 1);
        int endYear = endCalendar.get(Calendar.YEAR);
        int endMonthValue = endCalendar.get(Calendar.MONTH);
        int endDayOfMonth = endCalendar.get(Calendar.DAY_OF_MONTH);

        int yearDiff = endYear - startYear;     // yearDiff >= 0
        int monthDiff = endMonthValue - startMonth;

        int dayDiff = endDayOfMonth - startDay;

        if (dayDiff < 0) {
            Calendar endMinusOneCalendar = Calendar.getInstance();   // end 的上一个月
            endMinusOneCalendar.setTime(endCalendar.getTime());
            endMinusOneCalendar.add(Calendar.MONTH, -1);
            int monthDays = endMinusOneCalendar.getActualMaximum(Calendar.DATE);  // 该月的天数

            dayDiff += monthDays;  // 用上一个月的天数补上这个月差掉的日子

            if (monthDiff > 0) {   // eg. start is 2018-04-03, end is 2018-08-01
                monthDiff--;

            } else {  // eg. start is 2018-04-03, end is 2019-02-01
                monthDiff += 11;
                yearDiff--;

            }
        }
        int[] diff = new int[2];

        diff[0] = yearDiff * 12 + monthDiff;
        diff[1] = dayDiff;
        return diff;
    }
    /**
     * 判断当前日期是星期几
     */
    public static int dayForWeek(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 获取指定日期所在月份开始的时间戳
     *
     * @param date 指定日期
     * @return
     */
    public static Long getMonthBegin(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c.getTimeInMillis();
    }

    /**
     * 获取指定日期所在月份结束的时间戳
     *
     * @param date 指定日期
     * @return
     */
    public static Long getMonthEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        c.set(Calendar.MINUTE, 59);
        //将秒至59
        c.set(Calendar.SECOND, 59);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 999);
        // 获取本月最后一天的时间戳
        return c.getTimeInMillis();
    }

    /**
     * 获取两个时间之间的所有月份
     */
    public static List<Date> getMonthBetween(Date minDate, Date maxDate) {
        ArrayList<Date> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            String formatDate = sdf.format(curr.getTime());
            ParsePosition pos = new ParsePosition(0);
            result.add(sdf.parse(formatDate, pos));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    public static  Date getMonthAndDay(Date date,Integer month,Integer day){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        ca.set(Calendar.DAY_OF_YEAR,month);
        ca.set(Calendar.DAY_OF_MONTH, day);
        ca.set(Calendar.HOUR_OF_DAY ,00);
        ca.set(Calendar.MINUTE, 00);
        ca.set(Calendar.SECOND, 00);
        return ca.getTime();
    }

    public static void main(String[] args) {
    }

}
