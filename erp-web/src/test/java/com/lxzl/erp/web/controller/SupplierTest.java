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
        supplierQueryParam.setSupplierName("供货商 ");
        TestResult testResult = getJsonTestResult("/supplier/getSupplier", supplierQueryParam);
    }

    @Test
    public void add() throws Exception{
        Supplier supplier = new Supplier();
        supplier.setSupplierName("庄凯麟的大盘商55");
        supplier.setProvince(19);
        supplier.setCity(202);
        supplier.setDistrict(1956);
        supplier.setBeneficiaryName("庄凯麟");
        supplier.setBeneficiaryAccount("6225-5555");
        supplier.setBeneficiaryBankName("工商银行深圳车公庙支行");
        supplier.setSupplierCode("LX_-1234");
        TestResult testResult = getJsonTestResult("/supplier/add", supplier);
    }

    @Test
    public void update() throws Exception{
        Supplier supplier = new Supplier();
        supplier.setSupplierNo("LXS075500026");
        supplier.setSupplierName("庄凯麟的大盘商22");
        supplier.setBeneficiaryName("庄凯麟");
        supplier.setBeneficiaryAccount("6222-1234-5678");
        supplier.setBeneficiaryBankName("工商银行深圳车公庙支行");
        supplier.setSupplierCode("LX123456789");
        TestResult testResult = getJsonTestResult("/supplier/update", supplier);
    }

    @Test
    public void getSupplierByNo() throws Exception{
        Supplier supplier = new Supplier();
        supplier.setSupplierNo("LX20180108");
        TestResult testResult = getJsonTestResult("/supplier/getSupplierByNo", supplier);
    }

}
