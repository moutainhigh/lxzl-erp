package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.basic.BrandQueryParam;
import org.junit.Test;

public class BasicControllerTest extends ERPUnTransactionalTest{

    @Test
    public void queryAllBrand() throws Exception {
        BrandQueryParam brandQueryParam = new BrandQueryParam();
        TestResult result = getJsonTestResult("/basic/queryAllBrand",brandQueryParam);
    }

}