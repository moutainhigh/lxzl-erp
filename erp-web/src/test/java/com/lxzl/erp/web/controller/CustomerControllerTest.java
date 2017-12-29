package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.*;
import com.lxzl.erp.common.domain.payment.ManualChargeParam;
import com.lxzl.erp.common.domain.payment.ManualDeductParam;
import com.lxzl.erp.common.util.JSONUtil;
import org.junit.Test;

import java.math.BigDecimal;

import com.lxzl.erp.TestResult;

public class CustomerControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addCustomerCompany() throws Exception {
        Customer customer = new Customer();
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName("清华四方");
        customerCompany.setConnectRealName("王二码子");
        customerCompany.setConnectPhone("13888889999");
        customerCompany.setAddress("企业信息详细地址测试");
        customer.setCustomerCompany(customerCompany);
        TestResult result = getJsonTestResult("/customer/addCompany", customer);
    }

    @Test
    public void addCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setRealName("叶良辰");
        customerPerson.setEmail("zhangsan@163.com");
        customerPerson.setPhone("13888886666");
        customerPerson.setAddress("个人信息详细地址测试");
        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/addPerson", customer);
    }

    @Test
    public void updateCustomerCompany() throws Exception {
        Customer customer = new Customer();
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName("深圳市腾讯科技股份有限公司update");
        customerCompany.setConnectRealName("张三啊");
        customer.setCustomerCompany(customerCompany);
        customer.setCustomerNo("C201711221037203271636");
        TestResult result = getJsonTestResult("/customer/updateCompany", customer);
    }

    @Test
    public void updateCustomerPerson() throws Exception {
        Customer customer = new Customer();
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setRealName("我是一个小小人");
        customer.setCustomerPerson(customerPerson);
        customer.setCustomerNo("C201711221149433561458");
        TestResult result = getJsonTestResult("/customer/updatePerson", customer);
    }

    @Test
    public void pageCustomerCompany() throws Exception {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
//        customerCompanyQueryParam.setCompanyName("百");
        TestResult result = getJsonTestResult("/customer/pageCustomerCompany", customerCompanyQueryParam);
    }

    @Test
    public void pageCustomerPerson() throws Exception {
        CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
        customerPersonQueryParam.setCustomerNo("C201711221429052021353");
        TestResult result = getJsonTestResult("/customer/pageCustomerPerson", customerPersonQueryParam);
    }

    @Test
    public void detailCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("CC201712261408524351576");
        TestResult result = getJsonTestResult("/customer/detailCustomerCompany", customer);
    }

    @Test
    public void detailCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("CP201711271744534451243");
        TestResult result = getJsonTestResult("/customer/detailCustomerPerson", customer);
    }

    @Test
    public void updateRisk() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("CC201711301106206721011");
        customerRiskManagement.setPaymentCycle(12);
        customerRiskManagement.setCreditAmount(new BigDecimal(80000d));
        customerRiskManagement.setDepositCycle(12);
        customerRiskManagement.setRemark("这是一个备注");
        TestResult result = getJsonTestResult("/customer/updateRisk", customerRiskManagement);
    }

    @Test
    public void updateRiskJSON() throws Exception {
        String jsonStr = "{\n" +
                "\t\"creditAmount\": \"10000\",\n" +
                "\t\"depositCycle\": \"12\",\n" +
                "\t\"paymentCycle\": \"12\",\n" +
                "\t\"payMode\": \"2\",\n" +
                "\t\"appleDepositCycle\": \"12\",\n" +
                "\t\"applePaymentCycle\": \"12\",\n" +
                "\t\"applePayMode\": \"1\",\n" +
                "\t\"newDepositCycle\": \"12\",\n" +
                "\t\"newPaymentCycle\": \"12\",\n" +
                "\t\"newPayMode\": \"2\",\n" +
                "\t\"remark\": \"这是一个高级用户\",\n" +
                "\t\"customerNo\": \"CC201712261408524351576\"\n" +
                "}\n";
        TestResult result = getJsonTestResult("/customer/updateRisk", JSONUtil.convertJSONToBean(jsonStr, CustomerRiskManagement.class));
    }

    @Test
    public void addCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerNo("CP01711221151519901204");
        customerConsignInfo.setConsigneeName("add测试联系人6677");
        customerConsignInfo.setConsigneePhone("13566253480");
        customerConsignInfo.setProvince(9);
        customerConsignInfo.setCity(90);
        customerConsignInfo.setDistrict(901);
        customerConsignInfo.setAddress("测试地址6677");
        customerConsignInfo.setRemark("测试增加");
        customerConsignInfo.setIsMain(0);

        TestResult result = getJsonTestResult("/customer/addCustomerConsignInfo", customerConsignInfo);
    }

    @Test
    public void updateCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(29);
        customerConsignInfo.setConsigneeName("update测试联系人26622");
        customerConsignInfo.setConsigneePhone("13566253478");
        customerConsignInfo.setProvince(29);
        customerConsignInfo.setCity(172); //武汉市
        customerConsignInfo.setDistrict(1685); //汉阳区
        customerConsignInfo.setAddress("修改后的测试地址222");
        customerConsignInfo.setRemark("update备注");
        customerConsignInfo.setIsMain(0);

        TestResult result = getJsonTestResult("/customer/updateCustomerConsignInfo", customerConsignInfo);
    }

    @Test
    public void deleteCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(18);
        customerConsignInfo.setIsMain(1);

        TestResult result = getJsonTestResult("/customer/deleteCustomerConsignInfo", customerConsignInfo);
    }

    @Test
    public void detailCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(19);


        TestResult result = getJsonTestResult("/customer/detailCustomerConsignInfo", customerConsignInfo);
    }


    @Test
    public void pageCustomerConsignInfo() throws Exception {
        CustomerConsignInfoQueryParam customerConsignInfoQueryParam = new CustomerConsignInfoQueryParam();
        customerConsignInfoQueryParam.setCustomerNo("C201711152010206581143");
//        customerConsignInfoQueryParam.setPageNo(1);
//        customerConsignInfoQueryParam.setPageSize(3);

        TestResult result = getJsonTestResult("/customer/pageCustomerConsignInfo", customerConsignInfoQueryParam);
    }

    @Test
    public void updateAddressIsMain() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(9);

        TestResult result = getJsonTestResult("/customer/updateAddressIsMain", customerConsignInfo);
    }

    @Test
    public void updateLastUseTime() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
//        customerConsignInfo.setCustomerConsignInfoId(7);

        TestResult result = getJsonTestResult("/customer/updateLastUseTime", customerConsignInfo);
    }

    @Test
    public void queryAccount() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("500008");

        TestResult result = getJsonTestResult("/payment/queryAccount", customer);
    }

    @Test
    public void manualCharge() throws Exception {
        ManualChargeParam customer = new ManualChargeParam();
        customer.setBusinessCustomerNo("CC201712222002354621424");
        customer.setChargeAmount(new BigDecimal(11200.00));
        customer.setChargeRemark("测试手动加款1500元");
        TestResult result = getJsonTestResult("/payment/manualCharge", customer);
    }

    @Test
    public void manualDeduct() throws Exception {
        ManualDeductParam customer = new ManualDeductParam();
        customer.setBusinessCustomerNo("CC201712091546467081096");
        customer.setDeductAmount(new BigDecimal(1200));
        customer.setDeductRemark("测试手动扣款款1200元");
        TestResult result = getJsonTestResult("/payment/manualDeduct", customer);
    }

    @Test
    public void manualDeductJSON() throws Exception {
        String str = "{\n" +
                "\t\"businessCustomerNo\": \"CC201712261408524351576\",\n" +
                "\t\"deductAmount\": \"100\",\n" +
                "\t\"deductRemark\": \"备注\"\n" +
                "}";
        ManualDeductParam customer = JSONUtil.convertJSONToBean(str , ManualDeductParam.class);
        TestResult result = getJsonTestResult("/payment/manualDeduct", customer);
    }
}