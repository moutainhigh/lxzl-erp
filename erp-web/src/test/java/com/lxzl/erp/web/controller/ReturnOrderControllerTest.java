package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.returnOrder.*;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrder;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderConsignInfo;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderMaterial;
import com.lxzl.erp.common.domain.returnOrder.pojo.ReturnOrderProduct;
import com.lxzl.erp.core.service.order.impl.support.ReturnSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReturnOrderControllerTest extends ERPUnTransactionalTest {

    @Autowired
    private ReturnSupport returnSupport;

    @Test
    public void create() throws Exception {
        AddReturnOrderParam addReturnOrderParam = new AddReturnOrderParam();
        addReturnOrderParam.setCustomerNo("C201711152010206581143");
        addReturnOrderParam.setReturnTime(new Date());
        addReturnOrderParam.setOwner(500005);
        addReturnOrderParam.setReturnReasonType(1);
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
    public void createJson() throws Exception {
        String json = "{\"customerNo\":\"LXCC-1000-20180130-00063\",\"isCharging\":\"1\",\"remark\":\"\",\"returnMode\":\"1\",\"returnTime\":1518134400000,\"returnReasonType\":\"1\",\"owner\":\"500030\",\"returnOrderConsignInfo\":{\"consigneeName\":\"晓晓\",\"consigneePhone\":\"18566479745\",\"province\":6,\"city\":47,\"district\":530,\"address\":\"鹏鹏测试地址\"},\"returnOrderProductList\":[],\"returnOrderMaterialList\":[{\"returnMaterialNo\":\"M201712250956366751399\",\"returnMaterialCount\":\"1\"},{\"returnMaterialNo\":\"M201712250956366751399\"}]}";
        AddReturnOrderParam addReturnOrderParam = JSON.parseObject(json, AddReturnOrderParam.class);
        TestResult testResult = getJsonTestResult("/returnOrder/add", addReturnOrderParam);
    }

    @Test
    public void update2() throws Exception {
        UpdateReturnOrderParam updateReturnOrderParam = JSON.parseObject("{\n" +
                "  \"returnOrderNo\": \"LXRO7013392018010500007\",\n" +
                "  \"customerNo\": \"LXCC10002018010500022\",\n" +
                "  \"isCharging\": \"1\",\n" +
                "  \"remark\": \"\",\n" +
                "  \"returnMode\": \"1\",\n" +
                "  \"returnOrderConsignInfo\": {\n" +
                "    \"consigneeName\": \"测试紧急联系人\",\n" +
                "    \"consigneePhone\": \"18566324590\",\n" +
                "    \"address\": \"企业信息详细地址测试\"\n" +
                "  },\n" +
                "  \"returnOrderProductList\": [],\n" +
                "  \"returnOrderMaterialList\": [{\n" +
                "    \"returnMaterialNo\": \"M201801011026586021729\",\n" +
                "    \"returnMaterialCount\": \"2\"\n" +
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
    public void doReturnEquipment1() throws Exception {
        DoReturnEquipmentParam doReturnEquipmentParam = JSON.parseObject("{\"returnOrderNo\":\"LXRO7013362018010500005\",\"equipmentNo\":\"LX-E-4000002-2018010510029\"}",DoReturnEquipmentParam.class);
        TestResult testResult = getJsonTestResult("/returnOrder/doReturnEquipment", doReturnEquipmentParam);
    }
    @Test
    public void doReturnMaterial() throws Exception {
        DoReturnMaterialParam doReturnMaterialParam = new DoReturnMaterialParam();
        doReturnMaterialParam.setReturnOrderNo("LXRO7013392018010500008");
        doReturnMaterialParam.setMaterialNo("M201712181610381101755");
        doReturnMaterialParam.setReturnCount(3);
        TestResult testResult = getJsonTestResult("/returnOrder/doReturnMaterial", doReturnMaterialParam);
    }

    @Test
    public void doReturnMaterial1() throws Exception {
        DoReturnMaterialParam doReturnMaterialParam = JSON.parseObject("{\"returnOrderNo\":\"LXRO7013382018010500004\",\"materialNo\":\"M201711291745413251585\",\"returnCount\":\"1\"}", DoReturnMaterialParam.class);
        TestResult testResult = getJsonTestResult("/returnOrder/doReturnMaterial", doReturnMaterialParam);
    }

    @Test
    public void detail() throws Exception {
        ReturnOrder returnOrder = new ReturnOrder();
        returnOrder.setReturnOrderNo("LXRO7013392018010500007");
        TestResult testResult = getJsonTestResult("/returnOrder/detail", returnOrder);
    }

    @Test
    public void page() throws Exception {
        ReturnOrderPageParam returnOrderPageParam = new ReturnOrderPageParam();
//        returnOrderPageParam.setCustomerName("星期五早上_不要动");
        returnOrderPageParam.setOwnerName("毛涛");
//        returnOrderPageParam.setReturnOrderNo("RO201711291746283331383");
//        returnOrderPageParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010006");
//        returnOrderPageParam.setReturnOrderNo("RO20171129174628333138");
//        returnOrderPageParam.setCustomerNo("CC201711230928540471145");
//        returnOrderPageParam.setReturnOrderStatus(5);
//        returnOrderPageParam.setOwnerName("毛");
//        returnOrderPageParam.setCreateEndTime(new Date());
//        returnOrderPageParam.setPageNo(1);
//        returnOrderPageParam.setPageSize(1);
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
        returnBulkPageParam.setReturnOrderMaterialId(12);
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
        returnOrder.setReturnOrderNo("LXRO7013362018010500005");
        returnOrder.setServiceCost(new BigDecimal(100));
        returnOrder.setDamageCost(BigDecimal.ZERO);
        returnOrder.setIsDamage(CommonConstant.YES);
        TestResult testResult = getJsonTestResult("/returnOrder/end", returnOrder);
    }
    @Test
    public void endJson() throws Exception {
        String json = "{\"serviceCost\":\"0\",\"damageCost\":\"0\",\"isDamage\":\"0\",\"returnTime\":1518307200000,\"remark\":\"\",\"returnOrderNo\":\"LXRO-700032-20180122-00014\"}";
        ReturnOrder returnOrder = JSON.parseObject(json,ReturnOrder.class);
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

    @Test
    public void returnMaterial() throws Exception {
        String returnOrderNo = "LXRO-731494-20180205-00020";
        String orderNo = "LXO-20180205-731494-00018";
        ServiceResult<String, BigDecimal> totalPenalty = returnSupport.orderPenalty(returnOrderNo,orderNo);
        System.out.println(totalPenalty);
    }

}