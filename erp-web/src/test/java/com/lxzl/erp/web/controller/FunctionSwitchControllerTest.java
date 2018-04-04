package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.functionSwitch.SwitchQueryParam;
import com.lxzl.erp.common.domain.functionSwitch.pojo.Switch;
import org.junit.Test;

import java.text.SimpleDateFormat;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/4/4
 * @Time : Created in 17:11
 */
public class FunctionSwitchControllerTest extends ERPUnTransactionalTest{
    @Test
    public void add() throws Exception {

        Switch functionSwitch = new Switch();
        functionSwitch.setInterfaceUrl("functionSwitch/page/");
        TestResult jsonTestResult = getJsonTestResult("/functionSwitch/add",functionSwitch);
    }

    @Test
    public void update() throws Exception {
        Switch functionSwitch = new Switch();
        functionSwitch.setSwitchId(1);
        functionSwitch.setInterfaceUrl("/switch/add11");
        functionSwitch.setIsOpen(1);
        TestResult jsonTestResult = getJsonTestResult("/functionSwitch/update",functionSwitch);

    }

    @Test
    public void page() throws Exception {
        SwitchQueryParam switchQueryParam = new SwitchQueryParam();
//        switchQueryParam.setIsOpen(1);
//        switchQueryParam.setInterfaceUrl("/switch");
        switchQueryParam.setCreateEndTime(new SimpleDateFormat("yyyy-MM-dd").parse("2018-5-4"));
        switchQueryParam.setCreateStartTime(new SimpleDateFormat("yyyy-MM-dd").parse("2018-3-5"));
        TestResult jsonTestResult = getJsonTestResult("/functionSwitch/page",switchQueryParam);

    }

    @Test
    public void delete() throws Exception {
        Switch functionSwitch = new Switch();
//        functionSwitch.setSwitchId(1);
        TestResult jsonTestResult = getJsonTestResult("/functionSwitch/delete",functionSwitch);

    }

}