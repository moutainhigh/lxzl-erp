package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.erp.user.Role;
import com.lxzl.erp.common.domain.erp.user.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setUserName("kochiu8");
        user.setEmail("kochiu@lxzl.com.cn");
        user.setPassword("123456");
        List<Integer> roleList = new ArrayList<>();
        roleList.add(1);
        user.setRoleList(roleList);
        TestResult result = getJsonTestResult("/user/add",user);
    }

    @Test
    public void updateUser() throws Exception {
    }

    @Test
    public void updateUserPassword() throws Exception {
    }

    @Test
    public void login() throws Exception {
    }

    @Test
    public void getUserById() throws Exception {
    }

    @Test
    public void getCurrentUser() throws Exception {
    }

    @Test
    public void page() throws Exception {
    }

    @Test
    public void logout() throws Exception {
    }

}