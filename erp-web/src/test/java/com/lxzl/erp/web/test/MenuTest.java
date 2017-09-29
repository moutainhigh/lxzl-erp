package com.lxzl.erp.web.test;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Created by IntelliJ IDEA.
 * User: gaochao
 * Date: 2016/11/1.
 * Time: 15:09.
 */
public class MenuTest extends ERPUnTransactionalTest {

    @Test
    public void testMysql() throws Exception {
        MvcResult mvcResult = jsonRequest("/menu/getMenu");
        TestResult result = getJsonTestResult(mvcResult);
    }
}
