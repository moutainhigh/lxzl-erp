package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.customer.pojo.CustomerRiskManagement;
import org.junit.Test;

import java.math.BigDecimal;


public class CustomerRiskManagementControllerTest extends ERPUnTransactionalTest{
    @Test
    public void add() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("C201711151924067931067");
        customerRiskManagement.setPaymentCycle(1);
        customerRiskManagement.setCreditAmount(new BigDecimal(0d));
        customerRiskManagement.setDepositCycle(0);
        TestResult result = getJsonTestResult("/customerRiskManagement/add",customerRiskManagement);
    }

    @Test
    public void update() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerRiskManagementId(1);
        customerRiskManagement.setPaymentCycle(2);
        TestResult result = getJsonTestResult("/customerRiskManagement/update",customerRiskManagement);
    }

    @Test
    public void detail() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerNo("C201711151924067931067");
        TestResult result = getJsonTestResult("/customerRiskManagement/detail",customerRiskManagement);
    }

    @Test
    public void delete() throws Exception {
        CustomerRiskManagement customerRiskManagement = new CustomerRiskManagement();
        customerRiskManagement.setCustomerRiskManagementId(1);
        TestResult result = getJsonTestResult("/customerRiskManagement/delete",customerRiskManagement);
    }

}