package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.material.BulkMaterialQueryParam;
import com.lxzl.erp.common.domain.material.MaterialModelQueryParam;
import com.lxzl.erp.common.domain.material.MaterialQueryParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.material.pojo.MaterialImg;
import com.lxzl.erp.common.domain.material.pojo.MaterialModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: ${DESCRIPTION}
 *
 * @author gaochao
 * @date 2017-11-13 15:30
 */
public class MaterialTest extends ERPUnTransactionalTest {

    @Test
    public void addMaterial() throws Exception {
        Material material = new Material();
        material.setMaterialName("固态/128G SSD");
        material.setMaterialType(8);
        material.setMaterialCapacityValue(128.0);
        material.setIsRent(CommonConstant.YES);
        material.setMaterialPrice(new BigDecimal(5000.0));
        material.setDayRentPrice(new BigDecimal(5000.0));
        material.setMonthRentPrice(new BigDecimal(5000.0));
        material.setTimeRentPrice(new BigDecimal(5000.0));
        material.setMaterialDesc("测试备注");

        List<MaterialImg> materialImgList = new ArrayList<>();
        MaterialImg materialImg = new MaterialImg();
        materialImg.setMaterialImgId(1);
        materialImgList.add(materialImg);
        material.setMaterialImgList(materialImgList);
        TestResult result = getJsonTestResult("/material/add", material);
    }

    @Test
    public void updateMaterial() throws Exception {
        Material material = new Material();
        material.setMaterialNo("M201711201356145971009");
        material.setMaterialDesc("M201711201356145971009");
        TestResult result = getJsonTestResult("/material/update", material);
    }

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

    @Test
    public void addMaterialModel() throws Exception {
        MaterialModel materialModel = new MaterialModel();
        materialModel.setMaterialModelId(11);
        materialModel.setMaterialType(2);
        materialModel.setModelName("水冷机箱");
        TestResult result = getJsonTestResult("/material/addModel", materialModel);
    }

    @Test
    public void updateMaterialModel() throws Exception {
        MaterialModel materialModel = new MaterialModel();
        materialModel.setMaterialModelId(11);
        materialModel.setModelName("水冷机箱1");
        TestResult result = getJsonTestResult("/material/updateModel", materialModel);
    }

    @Test
    public void queryModel() throws Exception {
        MaterialModelQueryParam materialModelQueryParam = new MaterialModelQueryParam();
        materialModelQueryParam.setMaterialType(2);
        TestResult result = getJsonTestResult("/material/queryModel", materialModelQueryParam);
    }
}
