package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.se.common.domain.Result;
import org.junit.Test;

public class MenuControllerTest extends ERPUnTransactionalTest {
    @Test
    public void getMenu() throws Exception {
        Result result = getJsonTestResult("/menu/getMenu",null);
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