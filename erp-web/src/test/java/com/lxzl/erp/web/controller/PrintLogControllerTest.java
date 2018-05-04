package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.printLog.pojo.PrintLog;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author : XiaoLuYu
 * @Date : Created in 2018/5/4
 * @Time : Created in 14:11
 */
public class PrintLogControllerTest extends ERPUnTransactionalTest{
    @Test
    public void updatePrintLog() throws Exception {
        PrintLog printLog =  new PrintLog();
        printLog.setReferNo("LXZL-01010100");
        printLog.setReferType(1);
        printLog.setRemark("测试备注1");
        getJsonTestResult("/print/updatePrintLog",printLog);
    }

    @Test
    public void queryPrintLog() throws Exception {
        PrintLog printLog =  new PrintLog();
        printLog.setReferNo("LXZL-01010100");
        printLog.setReferType(1);
        TestResult jsonTestResult = getJsonTestResult("/print/queryPrintLog", printLog);
    }

}