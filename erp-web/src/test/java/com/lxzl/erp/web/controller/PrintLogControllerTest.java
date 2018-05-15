package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.printLog.PrintLogPageParam;
import com.lxzl.erp.common.domain.printLog.pojo.PrintLog;
import org.junit.Test;

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
        getJsonTestResult("/print/savePrintLog",printLog);
    }

    @Test
    public void pagePrintLog() throws Exception {
        PrintLogPageParam printLogPageParam =  new PrintLogPageParam();
        printLogPageParam.setReferNo("LXZL-01010100");
        printLogPageParam.setReferType(1);
        TestResult result = getJsonTestResult("/print/pagePrintLog", printLogPageParam);
    }


    @Test
    public void queryPrintLogCount() throws Exception {
        PrintLogPageParam printLogPageParam =  new PrintLogPageParam();
        printLogPageParam.setReferNo("LXZL-01010100");
        printLogPageParam.setReferType(1);
        TestResult result = getJsonTestResult("/print/queryPrintLogCount", printLogPageParam);
    }

}