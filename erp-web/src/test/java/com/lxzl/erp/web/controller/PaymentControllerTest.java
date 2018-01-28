package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.payment.ChargeRecordParam;
import com.lxzl.erp.common.domain.payment.WeixinPayParam;
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
        WeixinPayParam weixinPayParam = new WeixinPayParam();

        weixinPayParam.setBusinessCustomerNo("LXCC-1000-20180124-13746");
        weixinPayParam.setOpenId("o_ORluM1fFEVm9LMePBFvyBzbdr8");
        weixinPayParam.setAmount(new BigDecimal(0.01));
        weixinPayParam.setBusinessOrderRemark("test");
        TestResult testResult = getJsonTestResult("/payment/wechatCharge", weixinPayParam);
    }

    @Test
    public void queryChargeRecordPage() throws Exception {
        ChargeRecordParam param = new ChargeRecordParam();
        param.setPageNo(1);
        param.setPageSize(5);
        param.setBusinessCustomerNo("CC201712161455119301332");
        TestResult testResult = getJsonTestResult("/payment/queryChargeRecordPage", param);
    }
}
