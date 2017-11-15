package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CustomerType;
import com.lxzl.erp.common.domain.customer.CustomerCompanyQueryParam;
import com.lxzl.erp.common.domain.customer.CustomerPersonQueryParam;
import com.lxzl.erp.common.domain.customer.pojo.Customer;
import com.lxzl.erp.common.domain.customer.pojo.CustomerCompany;
import com.lxzl.erp.common.domain.customer.pojo.CustomerPerson;
import org.junit.Test;


public class CustomerControllerTest extends ERPUnTransactionalTest{
    @Test
    public void addCustomerCompany() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.CUSTOMER_TYPE_COMPANY);
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName("百度");
        customerCompany.setConnectRealName("小王");
        customer.setCustomerCompany(customerCompany);
        TestResult result = getJsonTestResult("/customer/add",customer);
    }
    @Test
    public void addCustomerPerson() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerType(CustomerType.CUSTOMER_TYPE_PERSON);
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setRealName("张三");
        customer.setCustomerPerson(customerPerson);
        TestResult result = getJsonTestResult("/customer/add",customer);
    }
    @Test
    public void updateCustomerCompany() throws Exception {
        Customer customer = new Customer();
        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName("深圳市腾讯科技股份有限公司");
        customerCompany.setConnectRealName("张三啊");
        customer.setCustomerCompany(customerCompany);
        customer.setCustomerNo("C201711151924067931067");
        TestResult result = getJsonTestResult("/customer/update",customer);
    }
    @Test
    public void updateCustomerPerson() throws Exception {
        Customer customer = new Customer();
        CustomerPerson customerPerson = new CustomerPerson();
        customerPerson.setRealName("我是一个人名");
        customer.setCustomerPerson(customerPerson);
        customer.setCustomerNo("C201711152009429071100");
        TestResult result = getJsonTestResult("/customer/update",customer);
    }
    @Test
    public void pageCustomerCompany() throws Exception {
        CustomerCompanyQueryParam customerCompanyQueryParam = new CustomerCompanyQueryParam();
        customerCompanyQueryParam.setCompanyName("百");
        TestResult result = getJsonTestResult("/customer/pageCustomerCompany",customerCompanyQueryParam);
    }
    @Test
    public void pageCustomerPerson() throws Exception {
        CustomerPersonQueryParam customerPersonQueryParam = new CustomerPersonQueryParam();
        TestResult result = getJsonTestResult("/customer/pageCustomerPerson",customerPersonQueryParam);
    }

}