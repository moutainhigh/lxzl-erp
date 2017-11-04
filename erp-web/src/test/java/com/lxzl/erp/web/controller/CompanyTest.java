package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
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

    @Test
    public void getDepartmentList() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        TestResult result = getJsonTestResult("/company/getDepartmentList",departmentQueryParam);
    }

    @Test
    public void getCompanyDepartmentTree() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        TestResult result = getJsonTestResult("/company/getCompanyDepartmentTree",departmentQueryParam);
    }
}