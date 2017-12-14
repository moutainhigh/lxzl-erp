package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
import com.lxzl.se.common.domain.Result;
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
        Result result = getJsonTestResult("/supplier/getSupplier", supplierQueryParam);
    }

    @Test
    public void add() throws Exception{
        Supplier supplier = new Supplier();
        supplier.setSupplierName("超级大的供应商");
        Result result = getJsonTestResult("/supplier/add", supplier);
    }

    @Test
    public void update() throws Exception{
        Supplier supplier = new Supplier();
        supplier.setSupplierNo("S201711220926507931454");
        supplier.setSupplierName("超级大大的供应商");
        Result result = getJsonTestResult("/supplier/update", supplier);
    }
}
