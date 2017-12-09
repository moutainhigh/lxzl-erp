package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.basic.BrandQueryParam;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-12-09 13:21
 */
public class BasicTest extends ERPUnTransactionalTest {

    @Test
    public void testQueryBrand() throws Exception {
        BrandQueryParam param = new BrandQueryParam();
        TestResult result = getJsonTestResult("/basic/queryAllBrand", param);
    }
}
