package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.erp.user.Role;
import com.lxzl.erp.common.domain.erp.user.RoleQueryParam;
import com.lxzl.erp.common.domain.erp.user.UserRole;
import com.lxzl.erp.common.domain.erp.user.UserRoleQueryParam;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserRoleControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addRole() throws Exception {
        Role role = new Role();
        role.setRoleName("测试");
        role.setRoleDesc("测试详情");
        role.setIsSuperAdmin(CommonConstant.COMMON_CONSTANT_NO);
        role.setRemark("测试备注");
        TestResult result = getJsonTestResult("/userRole/add",role);
    }

    @Test
    public void updateRole() throws Exception {
        Role role = new Role();
        role.setRoleId(2);
        role.setRoleName("测试修改");
        TestResult result = getJsonTestResult("/userRole/update",role);
    }

    @Test
    public void deleteRole() throws Exception {
        Role role = new Role();
        role.setRoleId(3);
        TestResult result = getJsonTestResult("/userRole/delete",role);
    }

    @Test
    public void page() throws Exception {
        RoleQueryParam roleQueryParam  = new RoleQueryParam();
        roleQueryParam.setPageNo(1);
        roleQueryParam.setPageSize(10);
        TestResult result = getJsonTestResult("/userRole/page",roleQueryParam);
    }

    @Test
    public void saveUserRole() throws Exception {
        UserRole userRole = new UserRole();
        userRole.setUserId(1);

        List<Role> roleList = new ArrayList<>();
//        Role role = new Role();
//        role.setRoleId(4);
//        roleList.add(role);

        userRole.setRoleList(roleList);

        TestResult result = getJsonTestResult("/userRole/saveUserRole",userRole);

    }

    @Test
    public void getUserRoleList() throws Exception {

        UserRoleQueryParam userRoleQueryParam = new UserRoleQueryParam();
        userRoleQueryParam.setUserId(1);
        userRoleQueryParam.setPageNo(1);
        userRoleQueryParam.setPageSize(10);
        TestResult result = getJsonTestResult("/userRole/getUserRoleList",userRoleQueryParam);
    }

    @Test
    public void saveRoleMenu() throws Exception {
    }

    @Test
    public void getRoleMenuList() throws Exception {
    }

}