package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.ReturnOrChangeMode;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class ChangeOrderControllerTest extends ERPUnTransactionalTest{
    @Test
    public void add() throws Exception {
        AddChangeOrderParam addChangeOrderParam = new AddChangeOrderParam();
        addChangeOrderParam.setCustomerNo("C201711152010206581143");
        addChangeOrderParam.setChangeMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        ChangeOrderConsignInfo changeOrderConsignInfo = new ChangeOrderConsignInfo();
        changeOrderConsignInfo.setAddress("这是一个测试地址");
        changeOrderConsignInfo.setConsigneePhone("13612342234");
        changeOrderConsignInfo.setConsigneeName("张武");
        addChangeOrderParam.setChangeOrderConsignInfo(changeOrderConsignInfo);

        List<ChangeOrderProduct> changeOrderProductList = new ArrayList<>();
        ChangeOrderProduct changeOrderProduct = new ChangeOrderProduct();
        changeOrderProduct.setChangeProductSkuIdSrc(40);
        changeOrderProduct.setChangeProductSkuIdDest(40);
        changeOrderProduct.setChangeProductSkuCount(2);
        changeOrderProductList.add(changeOrderProduct);
        addChangeOrderParam.setChangeOrderProductList(changeOrderProductList);
        TestResult testResult = getJsonTestResult("/changeOrder/add",addChangeOrderParam);
    }
    @Test
    public void addMaterial() throws Exception {
        AddChangeOrderParam addChangeOrderParam = new AddChangeOrderParam();
        addChangeOrderParam.setCustomerNo("C201711152010206581143");
        ChangeOrderConsignInfo changeOrderConsignInfo = new ChangeOrderConsignInfo();
        changeOrderConsignInfo.setAddress("这是一个测试地址");
        changeOrderConsignInfo.setConsigneePhone("13612342234");
        changeOrderConsignInfo.setConsigneeName("张武");
        addChangeOrderParam.setChangeOrderConsignInfo(changeOrderConsignInfo);

        List<ChangeOrderMaterial> changeOrderMaterialList = new ArrayList<>();
        ChangeOrderMaterial changeOrderMaterial = new ChangeOrderMaterial();
        changeOrderMaterial.setChangeMaterialNoSrc("M201711201356145971009");
        changeOrderMaterial.setChangeMaterialNoDest("M201711201356145971009");
        changeOrderMaterial.setChangeMaterialCount(3);
        changeOrderMaterialList.add(changeOrderMaterial);
        addChangeOrderParam.setChangeOrderMaterialList(changeOrderMaterialList);
        TestResult testResult = getJsonTestResult("/changeOrder/add",addChangeOrderParam);
    }
    @Test
    public void update() throws Exception {
        UpdateChangeOrderParam addChangeOrderParam = new UpdateChangeOrderParam();
        addChangeOrderParam.setChangeOrderNo("CO201712192043341871711");
        addChangeOrderParam.setChangeMode(ReturnOrChangeMode.RETURN_OR_CHANGE_MODE_TO_DOOR);
        ChangeOrderConsignInfo changeOrderConsignInfo = new ChangeOrderConsignInfo();
        changeOrderConsignInfo.setAddress("这是一个测试地址2");
        changeOrderConsignInfo.setConsigneePhone("13612342234");
        changeOrderConsignInfo.setConsigneeName("张三");
        addChangeOrderParam.setChangeOrderConsignInfo(changeOrderConsignInfo);

        List<ChangeOrderProduct> changeOrderProductList = new ArrayList<>();
        ChangeOrderProduct changeOrderProduct = new ChangeOrderProduct();
        //这里在每次【测试】时要修改
        changeOrderProduct.setChangeOrderProductId(10);
        changeOrderProduct.setChangeProductSkuIdSrc(40);
        changeOrderProduct.setChangeProductSkuIdDest(40);
        changeOrderProduct.setChangeProductSkuCount(3);
        changeOrderProductList.add(changeOrderProduct);
        addChangeOrderParam.setChangeOrderProductList(changeOrderProductList);

        List<ChangeOrderMaterial> changeOrderMaterialList = new ArrayList<>();
        ChangeOrderMaterial changeOrderMaterial = new ChangeOrderMaterial();
        changeOrderMaterial.setChangeMaterialNoSrc("M201711201356145971009");
        changeOrderMaterial.setChangeMaterialNoDest("M201711201356145971009");
        changeOrderMaterial.setChangeMaterialCount(3);
        changeOrderMaterialList.add(changeOrderMaterial);
        addChangeOrderParam.setChangeOrderMaterialList(changeOrderMaterialList);
        TestResult testResult = getJsonTestResult("/changeOrder/update",addChangeOrderParam);
    }
    @Test
    public void commit() throws Exception {
        ChangeOrderCommitParam changeOrderCommitParam = new ChangeOrderCommitParam();
        changeOrderCommitParam.setChangeOrderNo("CO201712181114261141769");
        changeOrderCommitParam.setVerifyUserId(500006);
        changeOrderCommitParam.setRemark("审核备注");
        TestResult testResult = getJsonTestResult("/changeOrder/commit",changeOrderCommitParam);
    }

    @Test
    public void stockUpForChange() throws Exception {
        StockUpForChangeParam stockUpForChangeParam = new StockUpForChangeParam();
        stockUpForChangeParam.setChangeOrderNo("CO201712181114261141769");
        //select * from erp_product_equipment where sku_id=40 and equipment_status = 1 and data_status = 1 and order_no is null and current_warehouse_id = 4000002
        stockUpForChangeParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017121610008");
//        stockUpForChangeParam.setMaterialNo("M201711201356145971009");
        stockUpForChangeParam.setMaterialCount(1);
        stockUpForChangeParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult testResult = getJsonTestResult("/changeOrder/stockUpForChange",stockUpForChangeParam);
    }

    @Test
    public void delivery() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("CO201712181114261141769");
        TestResult testResult = getJsonTestResult("/changeOrder/delivery",changeOrder);
    }
    @Test
    public void doChangeEquipment() throws Exception {
        ChangeOrderProductEquipment changeOrderProductEquipment = new ChangeOrderProductEquipment();
        changeOrderProductEquipment.setChangeOrderNo("CO201712181114261141769");
        changeOrderProductEquipment.setSrcEquipmentNo("LX-EQUIPMENT-4000002-2017121610003");
        TestResult testResult = getJsonTestResult("/changeOrder/doChangeEquipment",changeOrderProductEquipment);
    }
    @Test
    public void doChangeMaterial() throws Exception {
        ChangeOrderMaterial changeOrderMaterial = new ChangeOrderMaterial();
        changeOrderMaterial.setChangeOrderMaterialId(7);
        changeOrderMaterial.setChangeMaterialNoSrc("M201711201356145971009");
        changeOrderMaterial.setRealChangeMaterialCount(2);
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
        changeOrder.setChangeOrderNo("CO201712161841403121305");
        TestResult testResult = getJsonTestResult("/changeOrder/cancel",changeOrder);
    }
    @Test
    public void detail() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("CO201712181114261141769");
        TestResult testResult = getJsonTestResult("/changeOrder/detail",changeOrder);
    }
    @Test
    public void page() throws Exception {
        ChangeOrderPageParam changeOrderPageParam = new ChangeOrderPageParam();
        changeOrderPageParam.setOwnerName("涛");
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
    public void processNoChangeEquipment() throws Exception {

    }

}