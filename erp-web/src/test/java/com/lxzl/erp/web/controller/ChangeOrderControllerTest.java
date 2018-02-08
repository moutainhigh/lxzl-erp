package com.lxzl.erp.web.controller;

import com.alibaba.fastjson.JSON;
import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.*;
import com.lxzl.erp.common.util.DateUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class ChangeOrderControllerTest extends ERPUnTransactionalTest {
    @Test
    public void add() throws Exception {
        AddChangeOrderParam addChangeOrderParam = new AddChangeOrderParam();
        addChangeOrderParam.setOwner(500006);
        addChangeOrderParam.setCustomerNo("LXCC-1000-20180129-00062");
        addChangeOrderParam.setChangeMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        addChangeOrderParam.setRentStartTime(DateUtil.getDayByOffset(1));
        ChangeOrderConsignInfo changeOrderConsignInfo = new ChangeOrderConsignInfo();
        changeOrderConsignInfo.setAddress("这是一个测试地址");
        changeOrderConsignInfo.setConsigneePhone("13612342234");
        changeOrderConsignInfo.setConsigneeName("陈凯");
        addChangeOrderParam.setChangeOrderConsignInfo(changeOrderConsignInfo);

        List<ChangeOrderProduct> changeOrderProductList = new ArrayList<>();
        ChangeOrderProduct changeOrderProduct = new ChangeOrderProduct();
//        changeOrderProduct.setSrcChangeProductSkuId(65);
        changeOrderProduct.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
        changeOrderProduct.setSrcChangeProductSkuId(217);
        changeOrderProduct.setDestChangeProductSkuId(216);
        changeOrderProduct.setChangeProductSkuCount(1);
        changeOrderProductList.add(changeOrderProduct);
        addChangeOrderParam.setChangeOrderProductList(changeOrderProductList);
        TestResult testResult = getJsonTestResult("/changeOrder/add",addChangeOrderParam);
    }

    @Test
    public void addJson() throws Exception {
        String json = "{\n" +
                "  \"customerNo\": \"CP201712060843154191841\",\n" +
                "  \"changeReasonType\": \"1\",\n" +
                "  \"changeReason\": \"有损坏\",\n" +
                "  \"remark\": \"测试，勿动\",\n" +
                "  \"changeMode\": \"1\",\n" +
                "  \"rentStartTime\": 1517961600000,\n" +
                "  \"owner\": \"500029\",\n" +
                "  \"changeOrderConsignInfo\": {\n" +
                "    \"consigneeName\": \"黎文彬\",\n" +
                "    \"consigneePhone\": \"18033402833\",\n" +
                "    \"province\": 19,\n" +
                "    \"city\": 202,\n" +
                "    \"district\": 1956,\n" +
                "    \"address\": \"车公庙安华工业区\"\n" +
                "  },\n" +
                "  \"changeOrderProductList\": [{\n" +
                "    \"changeProductSkuIdSrc\": 72,\n" +
                "    \"changeProductSkuIdDest\": 72,\n" +
                "    \"isNew\": 0,\n" +
                "    \"changeProductSkuCount\": \"1\"\n" +
                "  }],\n" +
                "  \"changeOrderMaterialList\": []\n" +
                "}";
        AddChangeOrderParam addChangeOrderParam = JSON.parseObject(json,AddChangeOrderParam.class);
        TestResult testResult = getJsonTestResult("/changeOrder/add",addChangeOrderParam);
    }
    @Test
    public void addMaterial() throws Exception {
        AddChangeOrderParam addChangeOrderParam = new AddChangeOrderParam();
        addChangeOrderParam.setRentStartTime(DateUtil.getDayByOffset(1));
        addChangeOrderParam.setCustomerNo("LXCC-1000-20180130-00063");
        addChangeOrderParam.setChangeMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        addChangeOrderParam.setOwner(500006);
        ChangeOrderConsignInfo changeOrderConsignInfo = new ChangeOrderConsignInfo();
        changeOrderConsignInfo.setAddress("这是一个测试地址");
        changeOrderConsignInfo.setConsigneePhone("13612342234");
        changeOrderConsignInfo.setConsigneeName("张武");
        addChangeOrderParam.setChangeOrderConsignInfo(changeOrderConsignInfo);

        List<ChangeOrderMaterial> changeOrderMaterialList = new ArrayList<>();
        ChangeOrderMaterial changeOrderMaterial = new ChangeOrderMaterial();
        changeOrderMaterial.setSrcChangeMaterialNo("M201712250956366751399");
        changeOrderMaterial.setDestChangeMaterialNo("M201711201457288791418");
        changeOrderMaterial.setChangeMaterialCount(3);
        changeOrderMaterial.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
        changeOrderMaterialList.add(changeOrderMaterial);
        addChangeOrderParam.setChangeOrderMaterialList(changeOrderMaterialList);
        TestResult testResult = getJsonTestResult("/changeOrder/add",addChangeOrderParam);
    }
    @Test
    public void update() throws Exception {
        UpdateChangeOrderParam updateChangeOrderParam = new UpdateChangeOrderParam();
        updateChangeOrderParam.setChangeOrderNo("LXCO-701388-20180206-00002");
        updateChangeOrderParam.setOwner(500006);
        updateChangeOrderParam.setChangeMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        updateChangeOrderParam.setRentStartTime(DateUtil.getDayByOffset(1));
        ChangeOrderConsignInfo changeOrderConsignInfo = new ChangeOrderConsignInfo();
        changeOrderConsignInfo.setAddress("这是一个测试地址123123");
        changeOrderConsignInfo.setConsigneePhone("13612342234");
        changeOrderConsignInfo.setConsigneeName("陈凯");
        updateChangeOrderParam.setChangeOrderConsignInfo(changeOrderConsignInfo);

        List<ChangeOrderProduct> changeOrderProductList = new ArrayList<>();
        ChangeOrderProduct changeOrderProduct = new ChangeOrderProduct();
//        changeOrderProduct.setSrcChangeProductSkuId(65);
        changeOrderProduct.setIsNew(CommonConstant.COMMON_CONSTANT_NO);
        changeOrderProduct.setSrcChangeProductSkuId(217);
        changeOrderProduct.setDestChangeProductSkuId(216);
        changeOrderProduct.setChangeProductSkuCount(1);
        changeOrderProductList.add(changeOrderProduct);
        updateChangeOrderParam.setChangeOrderProductList(changeOrderProductList);
        TestResult testResult = getJsonTestResult("/changeOrder/update",updateChangeOrderParam);
    }
    @Test
    public void updateJson() throws Exception {
        String json = "{\n" +
                "  \"changeOrderNo\": \"LXCO-700032-20180206-00004\",\n" +
                "  \"customerNo\": \"CP201712060843154191841\",\n" +
                "  \"changeReasonType\": \"1\",\n" +
                "  \"changeReason\": \"升级\",\n" +
                "  \"remark\": \"测试，勿动\",\n" +
                "  \"changeMode\": \"1\",\n" +
                "  \"rentStartTime\": 1517875200000,\n" +
                "  \"owner\": \"500029\",\n" +
                "  \"changeOrderConsignInfo\": {\n" +
                "    \"consigneeName\": \"黎文彬\",\n" +
                "    \"consigneePhone\": \"13638699632\",\n" +
                "    \"province\": 1,\n" +
                "    \"city\": 1,\n" +
                "    \"district\": 1,\n" +
                "    \"address\": \"茶飘香\"\n" +
                "  },\n" +
                "  \"changeOrderProductList\": [{\n" +
                "    \"srcChangeProductSkuId\": 72,\n" +
                "    \"destChangeProductSkuId\": 72,\n" +
                "    \"isNew\": 0,\n" +
                "    \"changeProductSkuCount\": 1,\n" +
                "    \"changeOrderProductId\": 25\n" +
                "  }],\n" +
                "  \"changeOrderMaterialList\": [{\n" +
                "    \"srcChangeMaterialNo\": \"M201711291745413251585\",\n" +
                "    \"destChangeMaterialNo\": \"M201711291745413251585\",\n" +
                "    \"isNew\": 0,\n" +
                "    \"changeMaterialCount\": \"1\"\n" +
                "  }]\n" +
                "}";
        UpdateChangeOrderParam updateChangeOrderParam = JSON.parseObject(json,UpdateChangeOrderParam.class);
        TestResult testResult = getJsonTestResult("/changeOrder/update",updateChangeOrderParam);
    }
    @Test
    public void commit() throws Exception {
        ChangeOrderCommitParam changeOrderCommitParam = new ChangeOrderCommitParam();
        changeOrderCommitParam.setChangeOrderNo("LXCO-701389-20180207-00006");
        changeOrderCommitParam.setVerifyUserId(500006);
        changeOrderCommitParam.setRemark("审核备注");
        TestResult testResult = getJsonTestResult("/changeOrder/commit",changeOrderCommitParam);
    }

    @Test
    public void stockUpForChange() throws Exception {
        StockUpForChangeParam stockUpForChangeParam = new StockUpForChangeParam();
        stockUpForChangeParam.setChangeOrderNo("LXCO-701389-20180207-00006");
        //select * from erp_product_equipment where sku_id=40 and equipment_status = 1 and data_status = 1 and order_no is null and current_warehouse_id = 4000002

        //查询可用设备
        //SELECT * FROM  `erp_stock_order_equipment`  esoe  LEFT JOIN `erp_product_equipment` epe ON esoe.equipment_no = epe.equipment_no WHERE epe.sku_id=63 AND epe.equipment_status=1
//        stockUpForChangeParam.setEquipmentNo("LX-1000-Ideapad320S-20180206-16200");
        stockUpForChangeParam.setChangeOrderMaterialId(13);
        stockUpForChangeParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult testResult = getJsonTestResult("/changeOrder/stockUpForChange",stockUpForChangeParam);
    }
    @Test
    public void stockUpForChangeJson() throws Exception {
        String json = "{\n" +
                "  \"changeOrderNo\": \"LXCO-700032-20180206-00004\",\n" +
                "  \"equipmentNo\": \"LX-1000--20180205-05943\",\n" +
                "  \"operationType\": 3\n" +
                "}";
        StockUpForChangeParam stockUpForChangeParam = JSON.parseObject(json,StockUpForChangeParam.class);
        TestResult testResult = getJsonTestResult("/changeOrder/stockUpForChange",stockUpForChangeParam);
    }

    @Test
    public void delivery() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("LXCO-701389-20180207-00006");
        TestResult testResult = getJsonTestResult("/changeOrder/delivery",changeOrder);
    }
    @Test
    public void doChangeEquipment() throws Exception {
        ChangeOrderProductEquipment changeOrderProductEquipment = new ChangeOrderProductEquipment();
        changeOrderProductEquipment.setChangeOrderNo("CO201712231731362891843");
        //SELECT * FROM  `erp_stock_order_equipment`  esoe LEFT JOIN `erp_product_equipment` epe ON esoe.equipment_no = epe.equipment_no WHERE epe.sku_id=63 AND epe.equipment_status=2 and epe.order_no = 'O201712222007232111047'
        changeOrderProductEquipment.setSrcEquipmentNo("LX-EQUIPMENT-4000001-2017122310019");
        TestResult testResult = getJsonTestResult("/changeOrder/doChangeEquipment",changeOrderProductEquipment);
    }
    @Test
    public void doChangeMaterial() throws Exception {
        ChangeOrderMaterial changeOrderMaterial = new ChangeOrderMaterial();
        changeOrderMaterial.setChangeOrderMaterialId(13);
        changeOrderMaterial.setSrcChangeMaterialNo("M201712250956366751399");
        changeOrderMaterial.setRealChangeMaterialCount(1);
        TestResult testResult = getJsonTestResult("/changeOrder/doChangeMaterial",changeOrderMaterial);
    }
    @Test
    public void end() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("CO201712181114261141769");
        TestResult testResult = getJsonTestResult("/changeOrder/end",changeOrder);
    }
    @Test
    public void cancel() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("LXCO-701388-20180206-00002");
        TestResult testResult = getJsonTestResult("/changeOrder/cancel",changeOrder);
    }
    @Test
    public void detail() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("LXCO-701389-20180207-00005");
        TestResult testResult = getJsonTestResult("/changeOrder/detail",changeOrder);
    }
    @Test
    public void page() throws Exception {
        ChangeOrderPageParam changeOrderPageParam = new ChangeOrderPageParam();
//        changeOrderPageParam.setOwnerName("涛");
        TestResult testResult = getJsonTestResult("/changeOrder/page",changeOrderPageParam);
    }
    @Test
    public void pageChangeOrderProductEquipment() throws Exception {
        ChangeEquipmentPageParam changeEquipmentPageParam = new ChangeEquipmentPageParam();
        changeEquipmentPageParam.setChangeOrderProductId(9);
        TestResult testResult = getJsonTestResult("/changeOrder/pageChangeOrderProductEquipment",changeEquipmentPageParam);
    }
    @Test
    public void pageChangeOrderMaterialBulk() throws Exception {
        ChangeBulkPageParam changeBulkPageParam = new ChangeBulkPageParam();
        changeBulkPageParam.setChangeOrderMaterialId(7);
        TestResult testResult = getJsonTestResult("/changeOrder/pageChangeOrderMaterialBulk",changeBulkPageParam);
    }

    @Test
    public void updateChangeEquipmentRemark() throws Exception {
        UpdateChangeEquipmentRemarkParam updateChangeEquipmentRemarkParam = new UpdateChangeEquipmentRemarkParam();
        updateChangeEquipmentRemarkParam.setChangeOrderNo("CO201712231731362891843");
        updateChangeEquipmentRemarkParam.setEquipmentNo("LX-EQUIPMENT-4000001-2017122310019");
        updateChangeEquipmentRemarkParam.setRemark("这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊");
        TestResult testResult = getJsonTestResult("/changeOrder/updateChangeEquipmentRemark",updateChangeEquipmentRemarkParam);
    }
    @Test
    public void updateChangeMaterialRemark() throws Exception {
        UpdateChangeMaterialRemarkParam updateChangeMaterialRemarkParam = new UpdateChangeMaterialRemarkParam();
        updateChangeMaterialRemarkParam.setChangeOrderMaterialId(13);
        updateChangeMaterialRemarkParam.setRemark("这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊这句话一共是十个字啊");
        TestResult testResult = getJsonTestResult("/changeOrder/updateChangeMaterialRemark",updateChangeMaterialRemarkParam);
    }
    @Test
    public void confirmChangeOrder() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("LXCO-701389-20180207-00006");
        TestResult testResult = getJsonTestResult("/changeOrder/confirmChangeOrder",changeOrder);
    }
    @Test
    public void processNoChangeEquipment() throws Exception {

    }

}