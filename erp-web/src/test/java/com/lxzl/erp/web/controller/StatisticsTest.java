package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.TimeDimensionType;
import com.lxzl.erp.common.domain.statistics.*;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-27 20:08
 */
public class StatisticsTest extends ERPUnTransactionalTest {
    @Test
    public void queryIndexInfo() throws Exception {
        TestResult testResult = getJsonTestResult("/statistics/queryIndexInfo", null);
    }
    @Test
    public void queryIncome() throws Exception {
        StatisticsIncomePageParam statisticsIncomePageParam = new StatisticsIncomePageParam();
        statisticsIncomePageParam.setPageNo(1);
        statisticsIncomePageParam.setPageSize(100);
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
        Date start = sdf.parse( " 2008-07-10 19:20:00 " );
        Date end = sdf.parse( " 2028-07-10 19:20:00 " );
        statisticsIncomePageParam.setStartTime(start);
        statisticsIncomePageParam.setEndTime(end);
        statisticsIncomePageParam.setSubCompanyId(1);
        statisticsIncomePageParam.setRentLengthType(2);
        statisticsIncomePageParam.setCustomerName("公司");
        statisticsIncomePageParam.setSalesmanName("ad");
        TestResult testResult = getJsonTestResult("/statistics/queryIncome", statisticsIncomePageParam);
    }
    @Test
    public void queryIncomeJson() throws Exception {
        String json = "{\"pageNo\":1,\"pageSize\":15,\"customerName\":\"\",\"salesmanName\":\"\",\"rentLengthType\":\"\",\"startTime\":\"1517068800000\",\"endTime\":\"1519660799999\"}";
        StatisticsIncomePageParam statisticsIncomePageParam = JSON.parseObject(json,StatisticsIncomePageParam.class);

        TestResult testResult = getJsonTestResult("/statistics/queryIncome", statisticsIncomePageParam);
    }
    @Test
    public void queryUnReceivable() throws Exception {
        UnReceivablePageParam unReceivablePageParam = new UnReceivablePageParam();
        TestResult testResult = getJsonTestResult("/statistics/queryUnReceivable", unReceivablePageParam);
    }

    @Test
    public void queryStatisticsUnReceivable() throws Exception {
        StatisticsUnReceivablePageParam statisticsUnReceivablePageParam = new StatisticsUnReceivablePageParam();
//        statisticsUnReceivablePageParam.setSubCompanyId(1);
        TestResult testResult = getJsonTestResult("/statistics/queryStatisticsUnReceivable", statisticsUnReceivablePageParam);

    }

    @Test
    public void queryStatisticsUnReceivableJson() throws Exception {
        String json = "{\"pageNo\":1,\"pageSize\":15,\"salesmanName\":\"\",\"subCompanyId\":\"7\"}";
        StatisticsUnReceivablePageParam statisticsUnReceivablePageParam = JSON.parseObject(json,StatisticsUnReceivablePageParam.class);
        TestResult testResult = getJsonTestResult("/statistics/queryStatisticsUnReceivable", statisticsUnReceivablePageParam);
    }

    @Test
    public void queryStatisticsUnReceivableForSubCompany() throws Exception {
        TestResult testResult = getJsonTestResult("/statistics/queryStatisticsUnReceivableForSubCompany", null);
    }

    @Test
    public void queryLongRent() throws Exception {
        HomeRentParam homeRentParam = new HomeRentParam();
        homeRentParam.setStartTime(getFistByMonth());
        homeRentParam.setEndTime(getEndByMonth());
        TestResult testResult = getJsonTestResult("/statistics/queryLongRent", homeRentParam);
    }

    @Test
    public void queryShortRent() throws Exception {
        HomeRentParam homeRentParam = new HomeRentParam();
        homeRentParam.setStartTime(getFistByMonth());
        homeRentParam.setEndTime(getEndByMonth());
        TestResult testResult = getJsonTestResult("/statistics/queryShortRent", homeRentParam);
    }
    @Test
    public void queryLongRentByTime() throws Exception {
        HomeRentByTimeParam homeRentByTimeParam = new HomeRentByTimeParam();
        homeRentByTimeParam.setTimeDimensionType(TimeDimensionType.TIME_DIMENSION_TYPE_MONTH);
        TestResult testResult = getJsonTestResult("/statistics/queryLongRentByTime", homeRentByTimeParam);
    }

    @Test
    public void queryShortRentByTime() throws Exception {
        HomeRentByTimeParam homeRentByTimeParam = new HomeRentByTimeParam();
        homeRentByTimeParam.setTimeDimensionType(TimeDimensionType.TIME_DIMENSION_TYPE_YEAR);
        TestResult testResult = getJsonTestResult("/statistics/queryShortRentByTime", homeRentByTimeParam);
    }
    private Date getFistByMonth(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    private Date getEndByMonth(){
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND,59);
        ca.set(Calendar.MILLISECOND, 999);
        return ca.getTime();
    }
}
