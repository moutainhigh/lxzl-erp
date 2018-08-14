package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.erpInterface.customer.InterfaceCustomerAccountLogParam;
import com.lxzl.erp.common.domain.payment.ChargeRecordPageParam;
import com.lxzl.erp.common.domain.payment.CustomerAccountLogParam;
import com.lxzl.erp.common.domain.payment.ChargeParam;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @Author : kai
 * @Date : Created in 2018/1/27
 * @Time : Created in 10:19
 */
public class PaymentControllerTest extends ERPUnTransactionalTest {
    @Test
    public void constantlyExportQueryChargeRecord1() throws Exception {
//        AddOnlineHistoryBankSlipQueryParam param =  new AddOnlineHistoryBankSlipQueryParam();
//        param.setChargeOrderNo("CO201807030901537631101");
//        param.s
//        TestResult testResult = getJsonTestResult("/payment/constantlyExportQueryChargeRecord", param);
    }

    @Test
    public void exportQueryChargeRecordParamPage() throws Exception {
        ChargeRecordPageParam param =  new ChargeRecordPageParam();
        TestResult testResult = getJsonTestResult("/payment/exportHistoryChargeRecord", param);
    }


    @Test
    public void constantlyExportQueryChargeRecord() throws Exception {
        ChargeRecordPageParam chargeRecordPageParam =  new ChargeRecordPageParam();

//        List<ChargeRecord> list = new ArrayList<>();
//        ChargeRecord chargeRecord = new ChargeRecord();
//        chargeRecord.setCustomerId(12);
//        chargeRecord.setBusinessOrderNo("oo-asdas");
//        chargeRecord.setChargeTime(new Date());
//        chargeRecord.setChargeAmountReal(new BigDecimal(20));
//        chargeRecord.setChargeStatus(CommonConstant.DATA_STATUS_ENABLE);
//        chargeRecord.setChargeName("测试用户");
//        chargeRecord.setChargeDescription("充值详情");
//        chargeRecord.setOpenId("12312qweq1231");
//        chargeRecord.setBusinessCustomerNo("cc-asdbaksjdbkajsbdajs");
//        chargeRecord.setThirdPartyPayOrderId("取2131231");
//        chargeRecord.setSubCompanyId(1);
//        chargeRecord.setSubCompanyName("深圳分公司");
//        chargeRecord.setRemark("这是备注");
//        list.add(chargeRecord);
//        chargeRecordPageParam.setChargeRecordList(list);
        ChargeRecordPageParam param = new ChargeRecordPageParam();
        param.setChargeOrderNo("1111111");
        TestResult testResult = getJsonTestResult("/payment/constantlyExportQueryChargeRecord", param);
    }

    @Test
    public void weixinPay() throws Exception {
        ChargeParam param = new ChargeParam();

        param.setCustomerNo("LXCC-027-20180726-00111");
        param.setOpenId("o_ORluM1fFEVm9LMePBFvyBzbdr8");
        param.setAmount(new BigDecimal(0.01));
        TestResult testResult = getJsonTestResult("/payment/wechatCharge", param);
    }

    @Test
    public void queryChargeRecordPage() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("LXCC-1000-20180124-13746");
        TestResult testResult = getJsonTestResult("/payment/queryChargeRecordPage", customer);
    }

    @Test
    public void queryChargeRecordParamPage() throws Exception {


        ChargeRecordPageParam param = new ChargeRecordPageParam();
//        param.setPageNo(1);
//        param.setPageSize(10);
        param.setBusinessCustomerNo("LXCC-027-20180726-00111");
//        param.setSubCompanyId(8);
//        param.setCustomerName("续租唧");
//        param.setChargeType(2);
//        param.setChargeBodyId("2");
//        param.setChargeStatus(20);
//        param.setQueryStartTime(new Date("1514908800000"));
//        param.setQueryEndTime(new Date("1517500799999"));
        TestResult testResult = getJsonTestResult("/payment/queryChargeRecordParamPage", param);
    }


    @Test
    public void queryChargeRecordParamPageJson() throws Exception {
        String json = "{\n" +
                "    \"businessAppId\":\"201712140001\",\n" +
                "    \"businessAppSecret\":\"JN2hrJcxZjn6y4XvtdzqXOdy5ouol0FeQA2ZUfuayhcj4sUlqQMezAICJEM36sdg\",\n" +
                "    \"pageNo\":1,\n" +
                "    \"pageSize\":10,\n" +
//                "    \"subCompanyId\":2,\n" +
//                "    \"customerName\":\"rwearwe\",\n" +
//                "    \"chargeType\":5\n" +
                "}";
        ChargeRecordPageParam param = JSON.parseObject(json,ChargeRecordPageParam.class);

        TestResult testResult = getJsonTestResult("/payment/queryChargeRecordParamPage", param);
    }


    @Test
    public void queryCustomerAccountLogPage() throws Exception {
        CustomerAccountLogParam param = new CustomerAccountLogParam();
        param.setBusinessCustomerNo("LXCC-027-20180723-00088");
        param.setPageNo(1);
        param.setPageSize(15);
//        param.setCustomerAccountLogType(5);
        TestResult testResult = getJsonTestResult("/payment/queryCustomerAccountLogPage", param);
    }

    @Test
    public void weixinQueryCustomerAccountLogPage() throws Exception {
        InterfaceCustomerAccountLogParam param = new InterfaceCustomerAccountLogParam();
        param.setCustomerNo("LXCC-1000-20180213-00128");
        param.setPageNo(1);
        param.setPageSize(10);
//        param.setCustomerAccountLogType(5);
        TestResult testResult = getJsonTestResult("/payment/weixinQueryCustomerAccountLogPage", param);
    }

    @Test
    public void alipayCharge()  throws Exception {
        ChargeParam param = new ChargeParam();
        param.setCustomerNo("LXCC-027-20180726-00111");
        param.setOpenId("o_ORluM1fFEVm9LMePBFvyBzbdr8");
        param.setAmount(new BigDecimal(0.01));
        TestResult testResult = getJsonTestResult("/payment/wechatCharge", param);
    }
}
