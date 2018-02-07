package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.StatementOrderPayType;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderPayParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.util.FastJsonUtil;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-12 8:52
 */
public class StatementOrderControllerTest extends ERPUnTransactionalTest {

    @Test
    public void createNew() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setOrderNo("LXO-20180123-701359-00081");
        TestResult testResult = getJsonTestResult("/statementOrder/createNew", param);
    }

    @Test
    public void createChangeOrderStatement() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setChangeOrderNo("CO201712181114261141769");
        TestResult testResult = getJsonTestResult("/statementOrder/createChangeOrderStatement", param);
    }

    @Test
    public void createReturnOrderStatement() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setReturnOrderNo("LXRO-731475-20180129-00047");
        TestResult testResult = getJsonTestResult("/statementOrder/createReturnOrderStatement", param);
    }

    @Test
    public void pay() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        param.setStatementOrderNo("LXSO-731458-20180124-00044");
        param.setStatementOrderPayType(StatementOrderPayType.PAY_TYPE_BALANCE);
        TestResult testResult = getJsonTestResult("/statementOrder/pay", param);
    }

    @Test
    public void weixinPay() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        param.setStatementOrderNo("LXSO-731457-20180101-00037");
        param.setOpenId("o_ORluFbHAHEKaa_PCRo1bky4R6U");
        TestResult testResult = getJsonTestResult("/statementOrder/weixinPay", param);
    }

    @Test
    public void page() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setOwnerName("皮");
        TestResult testResult = getJsonTestResult("/statementOrder/page", param);
    }

    @Test
    public void pageJSON() throws Exception {
        String jsonStr = "{\"pageNo\":1,\"pageSize\":15,\"statementOrderNo\":\"\",\"statementOrderCustomerName\":\"\",\"statementExpectPayStartTime\":\"\",\"statementExpectPayEndTime\":\"\",\"createTimePicker\":\"\"}";
        StatementOrderQueryParam param = FastJsonUtil.toBean(jsonStr, StatementOrderQueryParam.class);

        TestResult testResult = getJsonTestResult("/statementOrder/page", param);
    }

    @Test
    public void detail() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        param.setStatementOrderNo("LXSO-731490-20180101-00087");
        TestResult testResult = getJsonTestResult("/statementOrder/detail", param);
    }

    @Test
    public void detailByOrderId() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setOrderNo("LXO2018010570133800034");
        TestResult testResult = getJsonTestResult("/statementOrder/detailByOrderId", param);
    }

    @Test
    public void queryStatementOrderCheckParam() throws Exception {
        String str = "2018-1-15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rentStartTime = sdf.parse(str);
        StatementOrderMonthQueryParam param = new StatementOrderMonthQueryParam();
        param.setPageNo(1);
        param.setPageSize(10);
//        param.setStatementOrderCustomerName("庄凯麟勿动");
//        param.setStatementOrderCustomerNo("LXCC-1000-20180131-13764");
        param.setMonthTime(rentStartTime);
        TestResult testResult = getJsonTestResult("/statementOrder/queryStatementOrderCheckParam", param);
    }

    @Test
    public void queryStatementOrderMonthDetail() throws Exception {
        String str = "2018-01-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rentStartTime = sdf.parse(str);
        StatementOrderMonthQueryParam param = new StatementOrderMonthQueryParam();
        param.setStatementOrderCustomerNo("LXCC-1000-20180202-00010");
        param.setMonthTime(rentStartTime);
        TestResult testResult = getJsonTestResult("/statementOrder/queryStatementOrderMonthDetail", param);
    }

    @Test
    public void batchPay() throws Exception {
        List<StatementOrderPayParam> param = new ArrayList<>();

        StatementOrderPayParam pay1 = new StatementOrderPayParam();
        pay1.setStatementOrderNo("LXSO-731490-20180701-00082");
        pay1.setStatementOrderPayType(StatementOrderPayType.PAY_TYPE_BALANCE);

        StatementOrderPayParam pay2 = new StatementOrderPayParam();
        pay2.setStatementOrderNo("LXSO-731490-20180401-00081");
        pay2.setStatementOrderPayType(StatementOrderPayType.PAY_TYPE_BALANCE);

        StatementOrderPayParam pay3 = new StatementOrderPayParam();
        pay3.setStatementOrderNo("LXSO-731490-20180103-00086");
        pay3.setStatementOrderPayType(StatementOrderPayType.PAY_TYPE_BALANCE);

        param.add(pay1);
        param.add(pay2);
        param.add(pay3);

        TestResult testResult = getJsonTestResult("/statementOrder/batchPay", param);
    }
}
