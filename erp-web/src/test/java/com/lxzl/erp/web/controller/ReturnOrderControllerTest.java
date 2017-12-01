package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.returnOrder.AddReturnOrderParam;
import com.lxzl.erp.common.domain.returnOrder.DoReturnEquipmentParam;
import com.lxzl.erp.common.domain.returnOrder.DoReturnMaterialParam;
import com.lxzl.erp.common.domain.returnOrder.ReturnOrderPageParam;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderConsignInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReturnOrderControllerTest extends ERPUnTransactionalTest{
    @Test
    public void create() throws Exception {
        AddReturnOrderParam addReturnOrderParam = new AddReturnOrderParam();
        addReturnOrderParam.setCustomerNo("CC201711301106206721011");
        List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setSkuId(2);
        productSku.setReturnCount(2);
        productSkuList.add(productSku);
//        addReturnOrderParam.setProductSkuList(productSkuList);

        List<Material> materialList = new ArrayList<>();
        Material material = new Material();
        material.setMaterialNo("M201711201356145971009");
        material.setReturnCount(1);
        materialList.add(material);
        addReturnOrderParam.setMaterialList(materialList);
        addReturnOrderParam.setRemark("这是一条备注");

        ReturnOrderConsignInfo returnOrderConsignInfo = new ReturnOrderConsignInfo();
        returnOrderConsignInfo.setConsigneeName("张三");
        returnOrderConsignInfo.setConsigneePhone("13612342234");
        returnOrderConsignInfo.setAddress("深圳市宝安区");
        addReturnOrderParam.setReturnOrderConsignInfo(returnOrderConsignInfo);
        addReturnOrderParam.setIsCharging(CommonConstant.COMMON_CONSTANT_YES);
        TestResult result = getJsonTestResult("/returnOrder/add",addReturnOrderParam);
    }
    @Test
    public void doReturnEquipment() throws Exception {
        DoReturnEquipmentParam doReturnEquipmentParam = new DoReturnEquipmentParam();
        doReturnEquipmentParam.setReturnOrderNo("RO201712011933151931203");
        doReturnEquipmentParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010006");
        TestResult result = getJsonTestResult("/returnOrder/doReturnEquipment",doReturnEquipmentParam);
    }
    @Test
    public void doReturnMaterial() throws Exception {
        DoReturnMaterialParam doReturnMaterialParam = new DoReturnMaterialParam();
        doReturnMaterialParam.setReturnOrderNo("RO201711291746283331383");
        List<String> materialList = new ArrayList<>();
        materialList.add("12312");
        doReturnMaterialParam.setMaterialNoList(materialList);
        TestResult result = getJsonTestResult("/returnOrder/doReturnMaterial",doReturnMaterialParam);
    }
    @Test
    public void detail() throws Exception {
        DoReturnEquipmentParam doReturnEquipmentParam = new DoReturnEquipmentParam();
        doReturnEquipmentParam.setReturnOrderNo("RO201711291746283331383");
        doReturnEquipmentParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010006");
        TestResult result = getJsonTestResult("/returnOrder/detail",doReturnEquipmentParam);
    }

    @Test
    public void page() throws Exception {
        ReturnOrderPageParam returnOrderPageParam = new ReturnOrderPageParam();
//        returnOrderPageParam.setReturnOrderNo("RO201711291746283331383");
//        returnOrderPageParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010006");
        returnOrderPageParam.setReturnOrderNo("RO20171129174628333138");
        returnOrderPageParam.setCustomerNo("CC201711230928540471145");
        returnOrderPageParam.setReturnOrderStatus(5);
        returnOrderPageParam.setOwnerName("毛");
        returnOrderPageParam.setCreateEndTime(new Date());
        returnOrderPageParam.setPageNo(1);
        returnOrderPageParam.setPageSize(5);
        TestResult result = getJsonTestResult("/returnOrder/page",returnOrderPageParam);
    }







}