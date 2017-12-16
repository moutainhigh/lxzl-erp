package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import org.junit.Test;
/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 12:41
 */
public class DdlTest extends ERPUnTransactionalTest {


    @Test
    public void listDdlConfig() throws Exception {
        TestResult result = getJsonTestResult("/ddl/listDdlConfig", null);
    }

    @Test
    public void getDdlRoute() throws Exception {
        TestResult result = getJsonTestResult("/ddl/getDdlRoute?table=erp_sub_company&columnValue=sub_company_name", null);
    }
}
