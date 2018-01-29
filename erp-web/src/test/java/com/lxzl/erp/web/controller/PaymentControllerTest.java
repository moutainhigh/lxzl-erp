package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.payment.WeixinChargeParam;
import org.junit.Test;

import java.math.BigDecimal;


/**
 * @Author : kai
 * @Date : Created in 2018/1/27
 * @Time : Created in 10:19
 */
public class PaymentControllerTest extends ERPUnTransactionalTest {

    @Test
    public void weixinPay() throws Exception {
        WeixinChargeParam param = new WeixinChargeParam();

        param.setCustomerNo("LXCC-1000-20180124-13746");
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
}
