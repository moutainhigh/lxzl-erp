package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import org.junit.Test;

public class AreaControllerTest extends ERPUnTransactionalTest{
    @Test
    public void getAreaList() throws Exception {
        TestResult result = getJsonTestResult("/area/getAreaList",null);
    }
}