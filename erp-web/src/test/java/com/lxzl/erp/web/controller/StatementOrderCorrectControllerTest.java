package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectQueryParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/30
 * @Time : Created in 20:45
 */
public class StatementOrderCorrectControllerTest extends ERPUnTransactionalTest {
    @Test
    public void createStatementOrderCorrect() throws Exception {
        StatementOrderCorrect statementOrderCorrect = new StatementOrderCorrect();
        statementOrderCorrect.setStatementOrderId(1097);
        statementOrderCorrect.setStatementOrderItemId(460);
        statementOrderCorrect.setStatementCorrectOverdueAmount(new BigDecimal(1.8));
        statementOrderCorrect.setStatementCorrectReason("测试225");
        statementOrderCorrect.setRemark("测试225");
        TestResult testResult = getJsonTestResult("/correct/create", statementOrderCorrect);
    }

    @Test
    public void commitStatementOrderCorrect() throws Exception {
        StatementOrderCorrect statementOrderCorrect = new StatementOrderCorrect();
        statementOrderCorrect.setStatementCorrectNo("LXSOC-1097-20180210-00008");
//        statementOrderCorrect.setVerifyUserId(500030);
        statementOrderCorrect.setRemark("aaaaaaa");
        TestResult testResult = getJsonTestResult("/correct/commit", statementOrderCorrect);
    }

    @Test
    public void updateStatementOrderCorrect() throws Exception {
        StatementOrderCorrect statementOrderCorrect = new StatementOrderCorrect();
        statementOrderCorrect.setStatementCorrectNo("LXSOC-22-20180131-00005");
        statementOrderCorrect.setStatementCorrectAmount(new BigDecimal(11));
        statementOrderCorrect.setStatementCorrectReason("测试22");
        statementOrderCorrect.setRemark("测试22");
        TestResult testResult = getJsonTestResult("/correct/update", statementOrderCorrect);
    }

    @Test
    public void cancelStatementOrderCorrect() throws Exception {
        StatementOrderCorrect statementOrderCorrect = new StatementOrderCorrect();
        statementOrderCorrect.setStatementCorrectNo("LXSOC-22-20180131-00004");
        TestResult testResult = getJsonTestResult("/correct/cancel", statementOrderCorrect);
    }

    @Test
    public void queryStatementOrderCorrectDetailByNo() throws Exception {
        StatementOrderCorrect statementOrderCorrect = new StatementOrderCorrect();
        statementOrderCorrect.setStatementCorrectNo("LXSOC-22-20180131-00004");
        TestResult testResult = getJsonTestResult("/correct/query", statementOrderCorrect);
    }

    @Test
    public void pageStatementOrderCorrect() throws Exception {
        StatementOrderCorrectQueryParam statementOrderCorrectQueryParam = new StatementOrderCorrectQueryParam();
//        statementOrderCorrectQueryParam.setStatementCorrectNo("LXSOC-22-20180131-00002");
//        statementOrderCorrectQueryParam.setStatementOrderDetailId(79);
//        statementOrderCorrectQueryParam.setStatementOrderId(22);
//        statementOrderCorrectQueryParam.setStatementOrderCorrectStatus(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 10, 12);
        statementOrderCorrectQueryParam.setCreateStartTime(calendar.getTime());
        calendar.set(2018, 02, 01);
        statementOrderCorrectQueryParam.setCreateEndTime(calendar.getTime());
        TestResult testResult = getJsonTestResult("/correct/page", statementOrderCorrectQueryParam);
    }

}