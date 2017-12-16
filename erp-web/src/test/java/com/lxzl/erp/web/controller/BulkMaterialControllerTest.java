package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.BulkMaterial;
import com.lxzl.erp.core.service.material.BulkMaterialService;
import com.lxzl.se.common.domain.Result;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.lxzl.erp.TestResult;

public class BulkMaterialControllerTest extends ERPUnTransactionalTest {

    @Autowired
    private BulkMaterialService bulkMaterialService;

    @Test
    public void dismantleBulkMaterial() throws Exception{
        BulkMaterial bulkMaterial = new BulkMaterial();
        bulkMaterial.setBulkMaterialId(477);

        TestResult result = getJsonTestResult("/bulkMaterial/dismantleBulkMaterial",bulkMaterial);
    }

    @Test
    public void installBulkMaterial() throws Exception{
        BulkMaterialQueryParam bulkMaterialQueryParam = new BulkMaterialQueryParam();
        bulkMaterialQueryParam.setBulkMaterialId(477);
        bulkMaterialQueryParam.setCurrentEquipmentNo("LX-EQUIPMENT-4000001-2017120110049");

        TestResult result = getJsonTestResult("/bulkMaterial/installBulkMaterial",bulkMaterialQueryParam);
    }



    @Test
    public void changeProductDismantleAndInstall() throws Exception{

        ServiceResult<String,Integer> ServiceResult= bulkMaterialService.changeProductDismantleAndInstall(476,478);
    }
}
