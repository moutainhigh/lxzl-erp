package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.returnOrder.*;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderConsignInfo;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderMaterial;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderProduct;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReturnOrderControllerTest extends ERPUnTransactionalTest {
    @Test
    public void create() throws Exception {
        AddReturnOrderParam addReturnOrderParam = new AddReturnOrderParam();
        addReturnOrderParam.setCustomerNo("C201711152010206581143");
        addReturnOrderParam.setReturnMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        List<ReturnOrderProduct> returnOrderProductList = new ArrayList<>();
        ReturnOrderProduct returnOrderProduct = new ReturnOrderProduct();
        returnOrderProduct.setReturnProductSkuId(40);
        returnOrderProduct.setReturnProductSkuCount(1);
        returnOrderProductList.add(returnOrderProduct);
        addReturnOrderParam.setReturnOrderProductList(returnOrderProductList);

        List<ReturnOrderMaterial> returnOrderMaterialList = new ArrayList<>();
        ReturnOrderMaterial returnOrderMaterial = new ReturnOrderMaterial();
        returnOrderMaterial.setReturnMaterialNo("M201711201356145971009");
        returnOrderMaterial.setReturnMaterialCount(1);
        returnOrderMaterialList.add(returnOrderMaterial);
        addReturnOrderParam.setReturnOrderMaterialList(returnOrderMaterialList);
        addReturnOrderParam.setRemark("这是一条备注");

        ReturnOrderConsignInfo returnOrderConsignInfo = new ReturnOrderConsignInfo();
        returnOrderConsignInfo.setConsigneeName("张三");
        returnOrderConsignInfo.setConsigneePhone("13612342234");
        returnOrderConsignInfo.setAddress("深圳市宝安区");
        addReturnOrderParam.setReturnOrderConsignInfo(returnOrderConsignInfo);
        addReturnOrderParam.setIsCharging(CommonConstant.COMMON_CONSTANT_YES);
        TestResult testResult = getJsonTestResult("/returnOrder/add", addReturnOrderParam);
    }
    @Test
    public void create3() throws Exception {
        AddReturnOrderParam addReturnOrderParam = new AddReturnOrderParam();
        addReturnOrderParam.setCustomerNo("CC201712091546467081096");
        addReturnOrderParam.setReturnMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        List<ReturnOrderProduct> returnOrderProductList = new ArrayList<>();
        ReturnOrderProduct returnOrderProduct = new ReturnOrderProduct();
        returnOrderProduct.setReturnProductSkuId(40);
        returnOrderProduct.setReturnProductSkuCount(1);
        returnOrderProductList.add(returnOrderProduct);
        addReturnOrderParam.setReturnOrderProductList(returnOrderProductList);

        ReturnOrderConsignInfo returnOrderConsignInfo = new ReturnOrderConsignInfo();
        returnOrderConsignInfo.setConsigneeName("随便");
        returnOrderConsignInfo.setConsigneePhone("13612342234");
        returnOrderConsignInfo.setAddress("随便");
        addReturnOrderParam.setReturnOrderConsignInfo(returnOrderConsignInfo);
        addReturnOrderParam.setIsCharging(CommonConstant.COMMON_CONSTANT_YES);
        TestResult testResult = getJsonTestResult("/returnOrder/add", addReturnOrderParam);
    }
    @Test
    public void create2() throws Exception {
        AddReturnOrderParam addReturnOrderParam = JSON.parseObject("{\n" +
                "  \"customerNo\": \"CP201712060843154191841\",\n" +
                "  \"isCharging\": \"1\",\n" +
                "  \"remark\": \"退货\",\n" +
                "  \"returnMode\": \"1\",\n" +
                "  \"returnOrderConsignInfo\": {\n" +
                "    \"consigneeName\": \"黎文\",\n" +
                "    \"consigneePhone\": \"18033402833\",\n" +
                "    \"province\": 19,\n" +
                "    \"city\": 202,\n" +
                "    \"district\": 1956,\n" +
                "    \"address\": \"车公庙安华工业区\"\n" +
                "  },\n" +
                "  \"returnOrderProductList\": [{\n" +
                "    \"returnProductSkuId\": 76,\n" +
                "    \"returnProductSkuCount\": \"4\"\n" +
                "  }],\n" +
                "  \"returnOrderMaterialList\": [{\n" +
                "    \"returnMaterialNo\": \"M201711201356145971009\",\n" +
                "    \"returnMaterialCount\": \"2\"\n" +
                "  }]\n" +
                "}", AddReturnOrderParam.class);
        TestResult testResult = getJsonTestResult("/returnOrder/add", addReturnOrderParam);
    }

    @Test
    public void update2() throws Exception {
        UpdateReturnOrderParam updateReturnOrderParam = JSON.parseObject("{\n" +
                "  \"returnOrderNo\": \"RO201712181649257021207\",\n" +
                "  \"isCharging\": \"1\",\n" +
                "  \"remark\": \"退货\",\n" +
                "  \"returnMode\": \"1\",\n" +
                "  \"returnOrderConsignInfo\": {\n" +
                "    \"consigneeName\": \"黎文\",\n" +
                "    \"consigneePhone\": \"18033402833\",\n" +
                "    \"province\": 19,\n" +
                "    \"city\": 202,\n" +
                "    \"district\": 1956,\n" +
                "    \"address\": \"车公庙安华工业区\"\n" +
                "  },\n" +
                "  \"returnOrderProductList\": [{\n" +
                "    \"returnProductSkuId\": 76,\n" +
                "    \"returnProductSkuCount\": \"2\"\n" +
                "  }],\n" +
                "  \"returnOrderMaterialList\": [{\n" +
                "    \"returnMaterialNo\": \"M201711201356145971009\",\n" +
                "    \"returnMaterialCount\": \"1\"\n" +
                "  }]\n" +
                "}", UpdateReturnOrderParam.class);
        TestResult testResult = getJsonTestResult("/returnOrder/update", updateReturnOrderParam);
    }


    @Test
    public void doReturnEquipment() throws Exception {
        DoReturnEquipmentParam doReturnEquipmentParam = new DoReturnEquipmentParam();
        doReturnEquipmentParam.setReturnOrderNo("LXRO7000532017123000010");
        doReturnEquipmentParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017122210014");
        TestResult testResult = getJsonTestResult("/returnOrder/doReturnEquipment", doReturnEquipmentParam);
    }

    @Test
    public void doReturnMaterial() throws Exception {
        DoReturnMaterialParam doReturnMaterialParam = new DoReturnMaterialParam();
        doReturnMaterialParam.setReturnOrderNo("RO201712191652428981484");
        doReturnMaterialParam.setMaterialNo("M201711201356145971009");
        doReturnMaterialParam.setReturnCount(1);
        TestResult testResult = getJsonTestResult("/returnOrder/doReturnMaterial", doReturnMaterialParam);
    }

    @Test
    public void detail() throws Exception {
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnOrderNo("RO201712252003510581416");
        TestResult testResult = getJsonTestResult("/returnOrder/detail", returnOrder);
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
        TestResult testResult = getJsonTestResult("/returnOrder/page", returnOrderPageParam);
    }

    @Test
    public void pageReturnEquipment() throws Exception {
        ReturnEquipmentPageParam returnEquipmentPageParam = new ReturnEquipmentPageParam();
        returnEquipmentPageParam.setReturnOrderProductId(8);
        TestResult testResult = getJsonTestResult("/returnOrder/pageReturnEquipment", returnEquipmentPageParam);
    }

    @Test
    public void pageReturnBulk() throws Exception {
        ReturnBulkPageParam returnBulkPageParam = new ReturnBulkPageParam();
        returnBulkPageParam.setReturnOrderMaterialId(5);
        TestResult testResult = getJsonTestResult("/returnOrder/pageReturnBulk", returnBulkPageParam);
    }

    @Test
    public void cancel() throws Exception {
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnOrderNo("RO201712181758392441854");
        TestResult testResult = getJsonTestResult("/returnOrder/cancel", returnOrder);
    }

    @Test
    public void end() throws Exception {
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnOrderNo("LXRO7000722017122900009");
        returnOrder.setServiceCost(new BigDecimal(100));
        returnOrder.setDamageCost(BigDecimal.ZERO);
        returnOrder.setIsDamage(CommonConstant.YES);
        TestResult testResult = getJsonTestResult("/returnOrder/end", returnOrder);
    }
    @Test
    public void commit() throws Exception {
        ReturnOrderCommitParam returnOrderCommitParam = new ReturnOrderCommitParam();
        returnOrderCommitParam.setReturnOrderNo("RO201712191652428981484");
        returnOrderCommitParam.setRemark("备注");
        returnOrderCommitParam.setVerifyUserId(500006);
        TestResult testResult = getJsonTestResult("/returnOrder/commit", returnOrderCommitParam);
    }

}