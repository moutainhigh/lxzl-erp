package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.supplier.SupplierQueryParam;
import com.lxzl.erp.common.domain.supplier.pojo.Supplier;
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
        TestResult testResult = getJsonTestResult("/supplier/getSupplier", supplierQueryParam);
    }

    @Test
    public void add() throws Exception{
        Supplier supplier = new Supplier();
        supplier.setSupplierNo("LX20180112");
        supplier.setSupplierName("庄凯麟的大盘商12");
        supplier.setBeneficiaryName("庄凯麟");
        supplier.setBeneficiaryAccount("62222212344321987654");
        supplier.setBeneficiaryBankName("工商银行深圳车公庙支行");
        TestResult testResult = getJsonTestResult("/supplier/add", supplier);
    }

    @Test
    public void update() throws Exception{
        Supplier supplier = new Supplier();
        supplier.setSupplierId(16);
        supplier.setSupplierNo("LX20180109");
        supplier.setSupplierName("庄凯麟的大盘商9");
        TestResult testResult = getJsonTestResult("/supplier/update", supplier);
    }

    @Test
    public void getSupplierByNo() throws Exception{
        Supplier supplier = new Supplier();
        supplier.setSupplierNo("LX20180108");
        TestResult testResult = getJsonTestResult("/supplier/getSupplierByNo", supplier);
    }

}
