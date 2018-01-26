package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.statistics.StatisticsIncomePageParam;
import com.lxzl.erp.common.util.DateUtil;
import org.junit.Test;

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
        statisticsIncomePageParam.setStartTime(getFistByMonth());
        statisticsIncomePageParam.setEndTime(getEndByMonth());
        TestResult testResult = getJsonTestResult("/statistics/queryIncome", statisticsIncomePageParam);
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
