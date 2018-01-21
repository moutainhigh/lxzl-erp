package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalNoLoginTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.user.UpdatePasswordParam;
import org.junit.Test;

/**
 * @author lk
 * @Description: TODO
 * @date 2018/1/21 14:45
 */
public class NoLoginTest extends ERPUnTransactionalNoLoginTest {
    @Test
    public void updatePasswordForNoLogin() throws Exception {
        UpdatePasswordParam updatePasswordParam = new UpdatePasswordParam();
        updatePasswordParam.setUserName("liuke");
        updatePasswordParam.setOldPassword("123456");
        updatePasswordParam.setNewPassword("Liuke123");
        TestResult testResult = getJsonTestResult("/user/updatePasswordForNoLogin",updatePasswordParam);
    }
}
