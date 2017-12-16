package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.returnOrder.RentMaterialCanProcessPageParam;
import com.lxzl.erp.common.domain.returnOrder.RentProductSkuPageParam;
import org.junit.Test;

public class CustomerOrderControllerTest extends ERPUnTransactionalTest{
    @Test
    public void pageRentProductSku() throws Exception {
        RentProductSkuPageParam rentProductSkuPageParam = new RentProductSkuPageParam();
        rentProductSkuPageParam.setCustomerNo("C201711152010206581143");
        TestResult testResult = getJsonTestResult("/customerOrder/pageRentProduct",rentProductSkuPageParam);
    }

    @Test
    public void pageRentMaterialCanReturn() throws Exception {
        RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam = new RentMaterialCanProcessPageParam();
        rentMaterialCanProcessPageParam.setCustomerNo("CP201712060843154191841");
        TestResult testResult = getJsonTestResult("/customerOrder/pageRentMaterialCanReturn",rentMaterialCanProcessPageParam);
    }

    @Test
    public void pageRentMaterialCanChange() throws Exception {
        RentMaterialCanProcessPageParam rentMaterialCanProcessPageParam = new RentMaterialCanProcessPageParam();
        rentMaterialCanProcessPageParam.setCustomerNo("C201711152010206581143");
        TestResult testResult = getJsonTestResult("/customerOrder/pageRentMaterialCanReturn",rentMaterialCanProcessPageParam);
    }

}