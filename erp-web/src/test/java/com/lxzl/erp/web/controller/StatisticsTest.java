package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-27 20:08
 */
public class StatisticsTest extends ERPUnTransactionalTest {
    @Test
    public void queryIndexInfo() throws Exception {
        TestResult testResult = getJsonTestResult("/statistics/queryIndexInfo", null);
    }
}
