package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.returnOrder.*;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderConsignInfo;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ReturnOrderControllerTest extends ERPUnTransactionalTest{
    @Test
    public void create() throws Exception {
        AddReturnOrderParam addReturnOrderParam = new AddReturnOrderParam();
        addReturnOrderParam.setCustomerNo("CC201711301106206721011");
        List<ProductSku> productSkuList = new ArrayList<>();
        ProductSku productSku = new ProductSku();
        productSku.setSkuId(40);
        productSku.setReturnCount(1);
        productSkuList.add(productSku);
        addReturnOrderParam.setProductSkuList(productSkuList);

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
        doReturnEquipmentParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010009");
        TestResult result = getJsonTestResult("/returnOrder/doReturnEquipment",doReturnEquipmentParam);
    }
    @Test
    public void doReturnMaterial() throws Exception {
        DoReturnMaterialParam doReturnMaterialParam = new DoReturnMaterialParam();
        doReturnMaterialParam.setReturnOrderNo("RO201712011933151931203");
        List<String> materialList = new ArrayList<>();
        materialList.add("BM2017112017070030810018");
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
//        returnOrderPageParam.setReturnOrderNo("RO20171129174628333138");
//        returnOrderPageParam.setCustomerNo("CC201711230928540471145");
//        returnOrderPageParam.setReturnOrderStatus(5);
//        returnOrderPageParam.setOwnerName("毛");
//        returnOrderPageParam.setCreateEndTime(new Date());
        returnOrderPageParam.setPageNo(1);
        returnOrderPageParam.setPageSize(1);
        TestResult result = getJsonTestResult("/returnOrder/page",returnOrderPageParam);
    }
    @Test
    public void pageReturnEquipment() throws Exception {
        ReturnEquipmentPageParam returnEquipmentPageParam = new ReturnEquipmentPageParam();
        returnEquipmentPageParam.setReturnOrderProductId(8);
        TestResult result = getJsonTestResult("/returnOrder/pageReturnEquipment",returnEquipmentPageParam);
    }
    @Test
    public void pageReturnBulk() throws Exception {
        ReturnBulkPageParam returnBulkPageParam = new ReturnBulkPageParam();
        returnBulkPageParam.setReturnOrderMaterialId(5);
        TestResult result = getJsonTestResult("/returnOrder/pageReturnBulk",returnBulkPageParam);
    }
    @Test
    public void cancel() throws Exception {
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnOrderNo("RO201712011933151931203");
        TestResult result = getJsonTestResult("/returnOrder/cancel",returnOrder);
    }
    @Test
    public void end() throws Exception {
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnOrderNo("RO201711291746283331383");
        returnOrder.setServiceCost(BigDecimal.ZERO);
        returnOrder.setDamageCost(BigDecimal.ZERO);
        TestResult result = getJsonTestResult("/returnOrder/end",returnOrder);
    }
    @Test
    public void pageRentProduct() throws Exception {
        RentProductSkuPageParam rentProductSkuPageParam = new RentProductSkuPageParam();
        rentProductSkuPageParam.setCustomerNo("CC201711301106206721011");
        TestResult result = getJsonTestResult("/returnOrder/pageRentProduct",rentProductSkuPageParam);
    }
    @Test
    public void pageRentMaterial() throws Exception {
        RentProductSkuPageParam rentProductSkuPageParam = new RentProductSkuPageParam();
        rentProductSkuPageParam.setCustomerNo("CC201711301106206721011");
        TestResult result = getJsonTestResult("/returnOrder/pageRentMaterial",rentProductSkuPageParam);
    }
}