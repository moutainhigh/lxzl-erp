package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;
import com.lxzl.erp.common.domain.material.pojo.Material;
import com.lxzl.erp.common.domain.product.pojo.ProductSku;
import com.lxzl.erp.common.domain.returnOrder.*;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderConsignInfo;
import com.lxzl.erp.common.domain.validGroup.returnOrder.AddReturnOrderGroup;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReturnOrderControllerTest extends ERPUnTransactionalTest{
    @Test
    public void create() throws Exception {
        AddReturnOrderParam addReturnOrderParam = new AddReturnOrderParam();
        addReturnOrderParam.setCustomerNo("CC201711301106206721011");
        List<ReturnSkuParam> returnSkuParamList = new ArrayList<>();
        ReturnSkuParam returnSkuParam = new ReturnSkuParam();
        returnSkuParam.setSkuId(40);
        returnSkuParam.setReturnCount(1);
        returnSkuParamList.add(returnSkuParam);
        addReturnOrderParam.setProductSkuList(returnSkuParamList);

        List<ReturnMaterialParam> returnMaterialParamList = new ArrayList<>();
        ReturnMaterialParam returnMaterialParam = new ReturnMaterialParam();
        returnMaterialParam.setMaterialNo("M201711201356145971009");
        returnMaterialParam.setReturnCount(1);
        returnMaterialParamList.add(returnMaterialParam);
        addReturnOrderParam.setMaterialList(returnMaterialParamList);
        addReturnOrderParam.setRemark("这是一条备注");

        ReturnOrderConsignInfo returnOrderConsignInfo = new ReturnOrderConsignInfo();
        returnOrderConsignInfo.setConsigneeName("张三");
        returnOrderConsignInfo.setConsigneePhone("13612342234");
        returnOrderConsignInfo.setAddress("深圳市宝安区");
        addReturnOrderParam.setReturnOrderConsignInfo(returnOrderConsignInfo);
        addReturnOrderParam.setIsCharging(CommonConstant.COMMON_CONSTANT_YES);
        TestResult testResult = getJsonTestResult("/returnOrder/add",addReturnOrderParam);
    }  @Test
    public void create2() throws Exception {
        AddReturnOrderParam addReturnOrderParam = JSON.parseObject("{\n" +
                "  \"customerNo\": \"CP201712060843154191841\",\n" +
                "  \"isCharging\": \"1\",\n" +
                "  \"remark\": \"退货\",\n" +
                "  \"returnMode\": \"1\",\n" +
                "  \"returnOrderConsignInfo\": {\n" +
                "    \"consigneeName\": \"黎文彬\",\n" +
                "    \"consigneePhone\": \"18033402833\",\n" +
                "    \"province\": 19,\n" +
                "    \"city\": 202,\n" +
                "    \"district\": 1956,\n" +
                "    \"address\": \"车公庙安华工业区\"\n" +
                "  },\n" +
                "  \"productSkuList\": [{\n" +
                "    \"skuId\": 76,\n" +
                "    \"returnCount\": \"5\"\n" +
                "  }],\n" +
                "  \"materialList\": [{\n" +
                "    \"materialNo\": \"M201711201356145971009\",\n" +
                "    \"returnCount\": \"1\"\n" +
                "  }]\n" +
                "}",AddReturnOrderParam.class);
        TestResult testResult = getJsonTestResult("/returnOrder/add",addReturnOrderParam);
    }


    @Test
    public void doReturnEquipment() throws Exception {
        DoReturnEquipmentParam doReturnEquipmentParam = new DoReturnEquipmentParam();
        doReturnEquipmentParam.setReturnOrderNo("RO201712011933151931203");
        doReturnEquipmentParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010009");
        TestResult testResult = getJsonTestResult("/returnOrder/doReturnEquipment",doReturnEquipmentParam);
    }
    @Test
    public void doReturnMaterial() throws Exception {
        DoReturnMaterialParam doReturnMaterialParam = new DoReturnMaterialParam();
        doReturnMaterialParam.setReturnOrderNo("RO201712011933151931203");
        List<String> materialList = new ArrayList<>();
        materialList.add("BM2017112017070030810018");
        doReturnMaterialParam.setMaterialNoList(materialList);
        TestResult testResult = getJsonTestResult("/returnOrder/doReturnMaterial",doReturnMaterialParam);
    }
    @Test
    public void detail() throws Exception {
        DoReturnEquipmentParam doReturnEquipmentParam = new DoReturnEquipmentParam();
        doReturnEquipmentParam.setReturnOrderNo("RO201711291746283331383");
        doReturnEquipmentParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010006");
        TestResult testResult = getJsonTestResult("/returnOrder/detail",doReturnEquipmentParam);
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
        TestResult testResult = getJsonTestResult("/returnOrder/page",returnOrderPageParam);
    }
    @Test
    public void pageReturnEquipment() throws Exception {
        ReturnEquipmentPageParam returnEquipmentPageParam = new ReturnEquipmentPageParam();
        returnEquipmentPageParam.setReturnOrderProductId(8);
        TestResult testResult = getJsonTestResult("/returnOrder/pageReturnEquipment",returnEquipmentPageParam);
    }
    @Test
    public void pageReturnBulk() throws Exception {
        ReturnBulkPageParam returnBulkPageParam = new ReturnBulkPageParam();
        returnBulkPageParam.setReturnOrderMaterialId(5);
        TestResult testResult = getJsonTestResult("/returnOrder/pageReturnBulk",returnBulkPageParam);
    }
    @Test
    public void cancel() throws Exception {
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnOrderNo("RO201712011933151931203");
        TestResult testResult = getJsonTestResult("/returnOrder/cancel",returnOrder);
    }
    @Test
    public void end() throws Exception {
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnOrderNo("RO201711291746283331383");
        returnOrder.setServiceCost(BigDecimal.ZERO);
        returnOrder.setDamageCost(BigDecimal.ZERO);
        TestResult testResult = getJsonTestResult("/returnOrder/end",returnOrder);
    }

}