package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.company.pojo.Department;
import com.lxzl.erp.common.domain.user.pojo.*;
import com.lxzl.erp.common.domain.user.RoleQueryParam;
import com.lxzl.erp.common.domain.user.UserRoleQueryParam;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
    public void updateRoleDepartmentData() throws Exception {

        RoleDepartmentData roleDepartmentData = new RoleDepartmentData();
        roleDepartmentData.setRoleId(1);
        List<Department> departmentList = new ArrayList<>();
        Department department = new Department();
        department.setDepartmentId(2);
        departmentList.add(department);
        roleDepartmentData.setDepartmentList(departmentList);
        TestResult result = getJsonTestResult("/userRole/updateRoleDepartmentData",roleDepartmentData);
    }
    @Test
    public void getRoleDepartmentDataListByRoleId() throws Exception {

        Role role = new Role();
        role.setRoleId(1);
        TestResult result = getJsonTestResult("/userRole/getRoleDepartmentDataListByRoleId",role);
    }

    @Test
    public void updateRoleUserData() throws Exception {

        RoleUserData roleUserData = new RoleUserData();
        roleUserData.setActiveUserId(500001);
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUserId(500002);
//        userList.add(user);
        roleUserData.setPassiveUserList(userList);
        TestResult result = getJsonTestResult("/userRole/updateRoleUserData",roleUserData);
    }
    @Test
    public void getRoleUserDataListByRoleId() throws Exception {

        User user = new User();
        user.setUserId(500001);
        TestResult result = getJsonTestResult("/userRole/getRoleUserDataListByRoleId",user);
    }
    @Test
    public void saveRoleMenu() throws Exception {
    }

    @Test
    public void getRoleMenuList() throws Exception {
    }

    @Test
    public void rebuildFinalRoleUserData() throws Exception {
        RoleUserData roleUserData = new RoleUserData();
        roleUserData.setActiveUserId(500001);
        TestResult result = getJsonTestResult("/userRole/rebuildFinalRoleUserData",roleUserData);
    }
    @Test
    public void getFinalRoleUserData() throws Exception {
        RoleUserData roleUserData = new RoleUserData();
        roleUserData.setActiveUserId(500001);
        TestResult result = getJsonTestResult("/userRole/getFinalRoleUserData",roleUserData);
    }
}