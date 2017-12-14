package com.lxzl.erp.web.test;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.se.common.domain.Result;
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
        MvcResult mvcResult = jsonTestRequest("/menu/getMenu");
        Result result = getJsonTestResult(mvcResult);
    }
}
