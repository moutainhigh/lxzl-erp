package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import org.junit.Test;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 15:30
 */
public class MaterialTest extends ERPUnTransactionalTest {

    @Test
    public void queryAllMaterial() throws Exception {
        MaterialQueryParam materialQueryParam = new MaterialQueryParam();
        materialQueryParam.setPageNo(1);
        materialQueryParam.setPageSize(15);
        TestResult result = getJsonTestResult("/material/queryAllMaterial", materialQueryParam);
    }

    @Test
    public void queryBulkMaterialByMaterialId() throws Exception {
        BulkMaterialQueryParam materialQueryParam = new BulkMaterialQueryParam();
        materialQueryParam.setPageNo(1);
        materialQueryParam.setPageSize(15);
        materialQueryParam.setMaterialId(2);
        TestResult result = getJsonTestResult("/material/queryBulkMaterialByMaterialId", materialQueryParam);
    }
}