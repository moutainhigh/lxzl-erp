package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-14 11:16
 */
public class SupplierTest extends ERPUnTransactionalTest {

    @Test
    public void getSupplier() throws Exception{
        SupplierQueryParam supplierQueryParam = new SupplierQueryParam();
        TestResult result = getJsonTestResult("/supplier/getSupplier", supplierQueryParam);
    }
}
