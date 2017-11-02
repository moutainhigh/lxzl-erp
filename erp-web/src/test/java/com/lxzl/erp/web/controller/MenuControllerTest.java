package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import org.junit.Test;

public class MenuControllerTest extends ERPUnTransactionalTest {
    @Test
    public void getMenu() throws Exception {
        TestResult result = getJsonTestResult("/menu/getMenu",null);
    }

    @Test
    public void getHomeMenu() throws Exception {
    }

    @Test
    public void getMenuByCode() throws Exception {
    }

    @Test
    public void getMenuTest() throws Exception {
    }

}