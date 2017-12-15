package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerConsignInfoQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.*;
import com.lxzl.erp.common.domain.payment.account.pojo.ManualChargeParam;
import com.lxzl.se.common.domain.Result;
import org.junit.Test;

import java.math.BigDecimal;


public class CustomerControllerTest extends ERPUnTransactionalTest{
    @Test
    public void addCustomerCompany() throws Exception {
        Customer customer = new Customer();
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName("清华四方");
        customerCompany.setConnectRealName("王二码子");
        customerCompany.setConnectPhone("13888889999");
        customerCompany.setAddress("企业信息详细地址测试");
        customer.setCustomerCompany(customerCompany);
        Result result = getJsonTestResult("/customer/addCompany",customer);
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
        Result result = getJsonTestResult("/customer/addPerson",customer);
    }
    @Test
    public void updateCustomerCompany() throws Exception {
        Customer customer = new Customer();
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName("深圳市腾讯科技股份有限公司update");
        customerCompany.setConnectRealName("张三啊");
        customer.setCustomerCompany(customerCompany);
        customer.setCustomerNo("C201711221037203271636");
        Result result = getJsonTestResult("/customer/updateCompany",customer);
    }
    @Test
    public void updateCustomerPerson() throws Exception {
        Customer customer = new Customer();
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setRealName("我是一个小小人");
        customer.setCustomerPerson(customerPerson);
        customer.setCustomerNo("C201711221149433561458");
        Result result = getJsonTestResult("/customer/updatePerson",customer);
    }
    @Test
    public void pageCustomerCompany() throws Exception {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
//        customerCompanyQueryParam.setCompanyName("百");
        Result result = getJsonTestResult("/customer/pageCustomerCompany",customerCompanyQueryParam);
    }
    @Test
    public void pageCustomerPerson() throws Exception {
        CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
        customerPersonQueryParam.setCustomerNo("C201711221429052021353");
        Result result = getJsonTestResult("/customer/pageCustomerPerson",customerPersonQueryParam);
    }

    @Test
    public void detailCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("CC201711221418540821486");
        Result result = getJsonTestResult("/customer/detailCustomerCompany",customer);
    }
    @Test
    public void detailCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("C201711152010206581143");
        Result result = getJsonTestResult("/customer/detailCustomerPerson",customer);
    }
    @Test
    public void updateRisk() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("CC201711301106206721011");
        customerRiskManagement.setPaymentCycle(12);
        customerRiskManagement.setCreditAmount(new BigDecimal(80000d));
        customerRiskManagement.setDepositCycle(12);
        customerRiskManagement.setRemark("这是一个备注");
        Result result = getJsonTestResult("/customer/updateRisk",customerRiskManagement);
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

        Result result = getJsonTestResult("/customer/addCustomerConsignInfo",customerConsignInfo);
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

        Result result = getJsonTestResult("/customer/updateCustomerConsignInfo",customerConsignInfo);
    }

    @Test
    public void deleteCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(18);
        customerConsignInfo.setIsMain(1);

        Result result = getJsonTestResult("/customer/deleteCustomerConsignInfo",customerConsignInfo);
    }

    @Test
    public void detailCustomerConsignInfo() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(19);


        Result result = getJsonTestResult("/customer/detailCustomerConsignInfo",customerConsignInfo);
    }


    @Test
    public void pageCustomerConsignInfo() throws Exception {
        CustomerConsignInfoQueryParam customerConsignInfoQueryParam = new CustomerConsignInfoQueryParam();
        customerConsignInfoQueryParam.setCustomerNo("C201711152010206581143");
//        customerConsignInfoQueryParam.setPageNo(1);
//        customerConsignInfoQueryParam.setPageSize(3);

        Result result = getJsonTestResult("/customer/pageCustomerConsignInfo",customerConsignInfoQueryParam);
    }

    @Test
    public void updateAddressIsMain() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
        customerConsignInfo.setCustomerConsignInfoId(9);

        Result result = getJsonTestResult("/customer/updateAddressIsMain",customerConsignInfo);
    }

    @Test
    public void updateLastUseTime() throws Exception {
        CustomerConsignInfo customerConsignInfo = new CustomerConsignInfo();
//        customerConsignInfo.setCustomerConsignInfoId(7);

        Result result = getJsonTestResult("/customer/updateLastUseTime",customerConsignInfo);
    }

    @Test
    public void queryAccount() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("500008");

        Result result = getJsonTestResult("/payment/queryAccount",customer);
    }

    @Test
    public void manualCharge() throws Exception {
        ManualChargeParam customer = new ManualChargeParam();
        customer.setBusinessCustomerNo("500008");
        customer.setChargeAmount(new BigDecimal(100.50));

        Result result = getJsonTestResult("/payment/manualCharge",customer);
    }
}