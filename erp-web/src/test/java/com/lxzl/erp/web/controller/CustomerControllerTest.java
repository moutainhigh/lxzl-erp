package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import org.junit.Test;

import java.math.BigDecimal;


public class CustomerControllerTest extends ERPUnTransactionalTest{
    @Test
    public void addCustomerCompany() throws Exception {
        Customer customer = new Customer();
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName("清华同方");
        customerCompany.setConnectRealName("李四");
        customer.setCustomerCompany(customerCompany);
        TestResult result = getJsonTestResult("/customer/addCompany",customer);
    }
    @Test
    public void addCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setRealName("张三");
        customerPerson.setEmail("zhangsan@163.com");
        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/addPerson",customer);
    }
    @Test
    public void updateCustomerCompany() throws Exception {
        Customer customer = new Customer();
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName("深圳市腾讯科技股份有限公司");
        customerCompany.setConnectRealName("张三啊");
        customer.setCustomerCompany(customerCompany);
        customer.setCustomerNo("C201711221037203271636");
        TestResult result = getJsonTestResult("/customer/updateCompany",customer);
    }
    @Test
    public void updateCustomerPerson() throws Exception {
        Customer customer = new Customer();
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setRealName("我是一个人名");
        customer.setCustomerPerson(customerPerson);
        customer.setCustomerNo("C201711221149433561458");
        TestResult result = getJsonTestResult("/customer/updatePerson",customer);
    }
    @Test
    public void pageCustomerCompany() throws Exception {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
//        customerCompanyQueryParam.setCompanyName("百");
        TestResult result = getJsonTestResult("/customer/pageCustomerCompany",customerCompanyQueryParam);
    }
    @Test
    public void pageCustomerPerson() throws Exception {
        CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
        customerPersonQueryParam.setCustomerNo("C201711221429052021353");
        TestResult result = getJsonTestResult("/customer/pageCustomerPerson",customerPersonQueryParam);
    }

    @Test
    public void detailCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("CC201711221418540821486");
        TestResult result = getJsonTestResult("/customer/detailCustomerCompany",customer);
    }
    @Test
    public void detailCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerNo("C201711152010206581143");
        TestResult result = getJsonTestResult("/customer/detailCustomerPerson",customer);
    }
    @Test
    public void updateRisk() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("CP201712060843154191841");
        customerRiskManagement.setPaymentCycle(12);
        customerRiskManagement.setCreditAmount(new BigDecimal(10000000d));
        customerRiskManagement.setDepositCycle(12);
        customerRiskManagement.setRemark("这是一个优质客户");
        TestResult result = getJsonTestResult("/customer/updateRisk",customerRiskManagement);
    }

}