package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.StatementOrderPayType;
import com.lxzl.erp.common.domain.k3.pojo.OrderStatementDateSplit;
import com.lxzl.erp.common.domain.k3.pojo.returnOrder.K3ReturnOrder;
import com.lxzl.erp.common.domain.order.pojo.Order;
import com.lxzl.erp.common.domain.reletorder.pojo.ReletOrder;
import com.lxzl.erp.common.domain.statement.StatementOrderMonthQueryParam;
import com.lxzl.erp.common.domain.statement.StatementOrderPayParam;
import com.lxzl.erp.common.domain.statement.StatementOrderQueryParam;
import com.lxzl.erp.common.util.FastJsonUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        param.setOrderNo("LXO-20180228-731827-00096");
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
        param.setReturnOrderNo("LXRO-731827-20180228-00038");
        TestResult testResult = getJsonTestResult("/statementOrder/createReturnOrderStatement", param);
    }

    @Test
    public void createK3ReturnOrderStatement() throws Exception {
        K3ReturnOrder param = new K3ReturnOrder();
        param.setReturnOrderNo("LXK3RO20180302142236358");
        TestResult testResult = getJsonTestResult("/statementOrder/createK3ReturnOrderStatement", param);
    }

    @Test
    public void pay() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        param.setStatementOrderNo("LXSO-704755-20180306-02494");
        param.setStatementOrderPayType(StatementOrderPayType.PAY_TYPE_BALANCE);
        TestResult testResult = getJsonTestResult("/statementOrder/pay", param);
    }

    @Test
    public void weixinPay() throws Exception {
        StatementOrderPayParam param = new StatementOrderPayParam();
        param.setStatementOrderNo("LXSO-704987-20180322-03517");
        param.setOpenId("o_ORluFbHAHEKaa_PCRo1bky4R6U");
        TestResult testResult = getJsonTestResult("/statementOrder/weixinPay", param);
    }

    @Test
    public void page() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
//        param.setOrderNo("LXO-20180305-0755-00009");//LXO-20180305-010-00001
//        param.setStatementOrderCustomerName("收货进行时五号");
        param.setSubCompanyId(3);
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
        param.setStatementOrderNo("LXSO-701088-20180601-07728");
        TestResult testResult = getJsonTestResult("/statementOrder/detail", param);
    }

    @Test
    public void detailByOrderId() throws Exception {
        StatementOrderQueryParam param = new StatementOrderQueryParam();
        param.setOrderNo("LXO-20180227-731826-00095");
        TestResult testResult = getJsonTestResult("/statementOrder/detailByOrderId", param);
    }

    @Test
    public void queryStatementOrderCheckParam() throws Exception {
//        String str = "2018-1-15";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date rentStartTime = sdf.parse(str);
        StatementOrderMonthQueryParam param = new StatementOrderMonthQueryParam();
        param.setPageNo(1);
        param.setPageSize(10);
        param.setOwnerName("来春");
//        param.setSubCompanyId(2);
//        param.setStatementOrderCustomerName("庄凯麟勿动");
//        param.setStatementOrderCustomerNo("LXCC-1000-20180131-13764");
//        param.setMonthTime(rentStartTime);
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
    @Test
    public void reCreateOrderStatement() throws Exception {
        Order param = new Order();
        param.setOrderNo("LXO-20180620-027-00115");
        TestResult testResult = getJsonTestResult("/statementOrder/reCreateOrderStatement", param);
    }

    @Test
    public void reCreateOrderStatementSplit() throws Exception {
        OrderStatementDateSplit param = new OrderStatementDateSplit();
        param.setOrderNo("LXSE2018012256");
        param.setBeforeStatementDate(31);
        param.setAfterStatementDate(20);
        param.setChangeType(0);
        Calendar calendar=Calendar.getInstance();
        calendar.set(2018,4,4);
        param.setStatementDateChangeTime(calendar.getTime());
        TestResult testResult = getJsonTestResult("/statementOrder/reCreateOrderStatementSplit", param);
    }

    @Test
    public void reCreateReletOrderStatement() throws Exception {
        ReletOrder param = new ReletOrder();
        param.setReletOrderNo("LXR-20180620-027-00108");
        TestResult testResult = getJsonTestResult("/statementOrder/reCreateReletOrderStatement", param);
    }

    @Test
    public void reStatementK3ReturnOrderRentOnly() throws Exception {
        K3ReturnOrder param = new K3ReturnOrder();
        param.setReturnOrderNo("LXK3RO20180626172311506");
        TestResult testResult = getJsonTestResult("/statementOrder/reStatementK3ReturnOrderRentOnly", param);
    }

    @Test
    public void exportQueryStatementOrderCheckParam() throws Exception {
        String str = "2018-05";
        String str1 = "2018-05";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date rentStartTime = sdf.parse(str);
        Date rentStartTime1 = sdf.parse(str1);
        StatementOrderMonthQueryParam param = new StatementOrderMonthQueryParam();
//        param.setPageNo(1);
//        param.setPageSize(10);
//        param.setOwnerName("来春");
//        param.setSubCompanyId(2);
//        param.setStatementOrderCustomerName("庄凯麟勿动");
        param.setStatementOrderCustomerNo("LXCC-020-20180212-00132");
        param.setStatementOrderStartTime(rentStartTime1);
        param.setStatementOrderEndTime(rentStartTime);
//        param.setMonthTime(rentStartTime);
        TestResult testResult = getJsonTestResult("/statementOrder/exportQueryStatementOrderCheckParam", param);
    }

}
