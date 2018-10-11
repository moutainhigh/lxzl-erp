package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.user.pojo.UserSysDataPrivilege;
import org.junit.Test;

public class UserPrevControllerTest extends ERPUnTransactionalTest {

    @Test
    public void testAddPrev() throws Exception {
        UserSysDataPrivilege userSysDataPrivilege = new UserSysDataPrivilege();
        userSysDataPrivilege.setUserId(500008);
        userSysDataPrivilege.setRoleId(600007);
        userSysDataPrivilege.setPrivilegeType(1);

        TestResult testResult = getJsonTestResult("/userPrev/AddPrev", userSysDataPrivilege);
        System.err.println(testResult);
    }
    @Test
    public void BatchPrev() throws Exception {
        UserSysDataPrivilege userSysDataPrivilege = new UserSysDataPrivilege();
        userSysDataPrivilege.setUserId(500007);
        userSysDataPrivilege.setRoleId(600006);
        userSysDataPrivilege.setPrivilegeType(1);

        TestResult testResult = getJsonTestResult("/userPrev/BatchPrev", userSysDataPrivilege);
        System.err.println(testResult);
    }

}