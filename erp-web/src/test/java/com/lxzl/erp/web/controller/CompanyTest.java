package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import org.junit.Test;

public class CompanyTest extends ERPUnTransactionalTest {
    @Test
    public void addSubCompany() throws Exception {
        SubCompany subCompany = new SubCompany();
        subCompany.setSubCompanyName("深圳分公司");
        TestResult result = getJsonTestResult("/company/addSubCompany",subCompany);
    }
    @Test
    public void pageSubCompany() throws Exception {
        SubCompanyQueryParam subCompanyQueryParam = new SubCompanyQueryParam();
        TestResult result = getJsonTestResult("/company/pageSubCompany",subCompanyQueryParam);
    }
}