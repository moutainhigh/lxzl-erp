package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.user.UserQueryParam;
import com.lxzl.erp.common.domain.user.pojo.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UserControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setUserName("eddie");
        user.setEmail("liuke@lxzl.com.cn");
        user.setPassword("123456");
        List<Integer> roleList = new ArrayList<>();
        roleList.add(1);
        user.setRoleList(roleList);

        List<Integer> departmentIdList = new ArrayList<>();
        departmentIdList.add(400002);
        user.setDepartmentList(departmentIdList);
        TestResult result = getJsonTestResult("/user/add",user);
    }

    @Test
    public void updateUser() throws Exception {
        User user = new User();
        user.setUserId(500003);
        List<Integer> roleList = new ArrayList<>();
        roleList.add(1);
        user.setRoleList(roleList);

        List<Integer> departmentIdList = new ArrayList<>();
        departmentIdList.add(400002);
        user.setDepartmentList(departmentIdList);
        TestResult result = getJsonTestResult("/user/update",user);
    }

    @Test
    public void updateUserPassword() throws Exception {
    }

    @Test
    public void login() throws Exception {
    }

    @Test
    public void getUserById() throws Exception {
        UserQueryParam param = new UserQueryParam();
        param.setUserId(500003);
        TestResult result = getJsonTestResult("/user/getUserById",param);
    }

    @Test
    public void getCurrentUser() throws Exception {
    }

    @Test
    public void page() throws Exception {
        UserQueryParam userQueryParam = new UserQueryParam();
        TestResult result = getJsonTestResult("/user/page",userQueryParam);
    }

    @Test
    public void logout() throws Exception {
    }

}