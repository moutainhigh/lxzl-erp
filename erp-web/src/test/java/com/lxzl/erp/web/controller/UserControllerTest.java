package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.user.LoginParam;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.Role;
import com.lxzl.erp.common.domain.user.pojo.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
public class UserControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setUserName("eddie1");
        user.setEmail("liuke@lxzl.com.cn");
        user.setPassword("123456");
        List<Role> roleList = new ArrayList<>();
        Role role = new Role();
        role.setRoleId(600002);
        roleList.add(role);
        user.setRoleList(roleList);

        TestResult testResult = getJsonTestResult("/user/add",user);
    }

    @Test
    public void updateUser() throws Exception {
        User user = new User();
        user.setUserId(500004);
        List<Role> roleList = new ArrayList<>();
        Role role = new Role();
        role.setRoleId(600002);
        roleList.add(role);
        user.setRoleList(roleList);
        TestResult testResult = getJsonTestResult("/user/update",user);
    }

    @Test
    public void updateUserPassword() throws Exception {
    }

    @Test
    public void login() throws Exception {
        LoginParam loginParam = new LoginParam();
        loginParam.setUserName("eddie");
        loginParam.setPassword("eddie");
        TestResult testResult = getJsonTestResult("/user/login",loginParam);
    }

    @Test
    public void getUserById() throws Exception {
        UserQueryParam param = new UserQueryParam();
        param.setUserId(500003);
        TestResult testResult = getJsonTestResult("/user/getUserById",param);
    }

    @Test
    public void getCurrentUser() throws Exception {
    }

    @Test
    public void page() throws Exception {
        UserQueryParam userQueryParam = new UserQueryParam();
//        userQueryParam.setRoleId(600001);
        userQueryParam.setDepartmentId(400007);
        TestResult testResult = getJsonTestResult("/user/page",userQueryParam);
    }

    @Test
    public void logout() throws Exception {
    }

}