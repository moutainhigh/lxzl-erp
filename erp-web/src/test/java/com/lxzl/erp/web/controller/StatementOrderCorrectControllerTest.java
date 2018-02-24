package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.StatementOrderCorrectQueryParam;
import com.lxzl.erp.common.domain.statementOrderCorrect.pojo.StatementOrderCorrect;
import com.lxzl.erp.common.util.FastJsonUtil;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/1/30
 * @Time : Created in 20:45
 */
public class StatementOrderCorrectControllerTest extends ERPUnTransactionalTest {
    @Test
    public void createStatementOrderCorrect() throws Exception {
        StatementOrderCorrect statementOrderCorrect = new StatementOrderCorrect();
        statementOrderCorrect.setStatementOrderId(38);
        statementOrderCorrect.setStatementOrderReferId(38);
        statementOrderCorrect.setStatementOrderItemId(461);
//        statementOrderCorrect.setStatementCorrectRentAmount(new BigDecimal(15));
        statementOrderCorrect.setStatementCorrectAmount(new BigDecimal(0));
//        statementOrderCorrect.setStatementCorrectOverdueAmount(new BigDecimal(0.82));
        statementOrderCorrect.setStatementCorrectReason("测试225");
        statementOrderCorrect.setRemark("测试225");
        TestResult testResult = getJsonTestResult("/correct/create", statementOrderCorrect);
    }
    @Test
    public void createStatementOrderCorrectJSON() throws Exception {
        String str = "{\n" +
                "\t\"statementOrderId\": 24,\n" +
                "\t\"statementOrderReferId\": 3000183,\n" +
                "\t\"statementOrderItemId\": 446,\n" +
                "\t\"statementCorrectAmount\": \"1000\",\n" +
                "\t\"statementCorrectRentAmount\": \"\",\n" +
                "\t\"statementCorrectRentDepositAmount\": \"\",\n" +
                "\t\"statementCorrectDepositAmount\": \"\",\n" +
                "\t\"statementCorrectOtherAmount\": \"\",\n" +
                "\t\"statementCorrectOverdueAmount\": \"\",\n" +
                "\t\"statementCorrectReason\": \"\",\n" +
                "\t\"remark\": \"\"\n" +
                "}";
        StatementOrderCorrect statementOrderCorrect = FastJsonUtil.toBean(str,StatementOrderCorrect.class);
        TestResult testResult = getJsonTestResult("/correct/create", statementOrderCorrect);
    }

    @Test
    public void commitStatementOrderCorrect() throws Exception {
        StatementOrderCorrectParam statementOrderCorrectParam = new StatementOrderCorrectParam();
        statementOrderCorrectParam.setStatementCorrectNo("LXSOC-38-20180211-00003");
        statementOrderCorrectParam.setVerifyUserId(500030);
        statementOrderCorrectParam.setRemark("aaaaaaa");
        TestResult testResult = getJsonTestResult("/correct/commit", statementOrderCorrectParam);
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
        statementOrderCorrect.setStatementCorrectNo("LXSOC-1085-20180210-00005");
        TestResult testResult = getJsonTestResult("/correct/cancel", statementOrderCorrect);
    }

    @Test
    public void queryStatementOrderCorrectDetailByNo() throws Exception {
        StatementOrderCorrect statementOrderCorrect = new StatementOrderCorrect();
        statementOrderCorrect.setStatementCorrectNo("LXSOC-24-20180223-00007");
        TestResult testResult = getJsonTestResult("/correct/query", statementOrderCorrect);
    }

    @Test
    public void pageStatementOrderCorrect() throws Exception {
        StatementOrderCorrectQueryParam statementOrderCorrectQueryParam = new StatementOrderCorrectQueryParam();
        statementOrderCorrectQueryParam.setStatementCorrectNo("LXSOC-24-20180223-00007");
//        statementOrderCorrectQueryParam.setStatementOrderDetailId(79);
//        statementOrderCorrectQueryParam.setStatementOrderId(22);
//        statementOrderCorrectQueryParam.setStatementOrderCorrectStatus(0);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2017, 10, 12);
//        statementOrderCorrectQueryParam.setCreateStartTime(calendar.getTime());
//        calendar.set(2018, 02, 23);
//        statementOrderCorrectQueryParam.setCreateEndTime(calendar.getTime());
        TestResult testResult = getJsonTestResult("/correct/page", statementOrderCorrectQueryParam);
    }

}