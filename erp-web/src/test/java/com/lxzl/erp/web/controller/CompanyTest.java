package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.se.common.domain.Result;
import org.junit.Test;

public class CompanyTest extends ERPUnTransactionalTest {
    @Test
    public void addSubCompany() throws Exception {
        SubCompany subCompany = new SubCompany();
        subCompany.setSubCompanyName("深圳分公司");
        Result result = getJsonTestResult("/company/addSubCompany",subCompany);
    }

    @Test
    public void pageSubCompany() throws Exception {
        SubCompanyQueryParam subCompanyQueryParam = new SubCompanyQueryParam();
        Result result = getJsonTestResult("/company/pageSubCompany",subCompanyQueryParam);
    }

    @Test
    public void getCompanyById() throws Exception {
        SubCompanyQueryParam subCompanyQueryParam = new SubCompanyQueryParam();
        subCompanyQueryParam.setSubCompanyId(1);
        Result result = getJsonTestResult("/company/getCompanyById",subCompanyQueryParam);
    }

    @Test
    public void getDepartmentList() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        Result result = getJsonTestResult("/company/getDepartmentList",departmentQueryParam);
    }

    @Test
    public void getCompanyDepartmentTree() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        departmentQueryParam.setSubCompanyId(1);
        Result result = getJsonTestResult("/company/getCompanyDepartmentTree",departmentQueryParam);
    }

    @Test
    public void getCompanyDepartmentUserTree() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        departmentQueryParam.setSubCompanyId(1);
        Result result = getJsonTestResult("/company/getCompanyDepartmentUserTree",departmentQueryParam);
    }

    @Test
    public void deleteDepartment() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        departmentQueryParam.setDepartmentId(400039);
        Result result = getJsonTestResult("/company/deleteDepartment",departmentQueryParam);
    }
}