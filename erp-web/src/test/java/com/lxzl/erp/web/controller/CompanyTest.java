package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.domain.company.SubCompanyQueryParam;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.company.pojo.SubCompany;
import com.lxzl.erp.common.domain.user.DepartmentQueryParam;
import com.lxzl.erp.common.util.JSONUtil;
import com.lxzl.se.common.domain.Result;
import org.junit.Test;
import com.lxzl.erp.TestResult;
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
    public void getCompanyById() throws Exception {
        SubCompanyQueryParam subCompanyQueryParam = new SubCompanyQueryParam();
        subCompanyQueryParam.setSubCompanyId(1);
        TestResult result = getJsonTestResult("/company/getCompanyById",subCompanyQueryParam);
    }

    @Test
    public void getDepartmentList() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        TestResult result = getJsonTestResult("/company/getDepartmentList",departmentQueryParam);
    }

    @Test
    public void getCompanyDepartmentTree() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        departmentQueryParam.setSubCompanyId(1);
        TestResult result = getJsonTestResult("/company/getCompanyDepartmentTree",departmentQueryParam);
    }

    @Test
    public void getCompanyDepartmentUserTree() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        departmentQueryParam.setSubCompanyId(1);
        TestResult result = getJsonTestResult("/company/getCompanyDepartmentUserTree",departmentQueryParam);
    }

    @Test
    public void updateDepartment() throws Exception {
        String str = "{\"departmentId\":400049,\"departmentName\":\"财务一部\",\"departmentType\":\"300001\",\"parentDepartmentId\":\"400000\",\"subCompanyId\":\"2\"}";
        Department department = JSONUtil.convertJSONToBean(str, Department.class);
        TestResult result = getJsonTestResult("/company/updateDepartment",department);
    }

    @Test
    public void deleteDepartment() throws Exception {
        DepartmentQueryParam departmentQueryParam = new DepartmentQueryParam();
        departmentQueryParam.setDepartmentId(400039);
        TestResult result = getJsonTestResult("/company/deleteDepartment",departmentQueryParam);
    }
}