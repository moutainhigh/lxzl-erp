package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrder;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderConsignInfo;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderMaterial;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderProductEquipment;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class ChangeOrderControllerTest extends ERPUnTransactionalTest{
    @Test
    public void add() throws Exception {
        AddChangeOrderParam addChangeOrderParam = new AddChangeOrderParam();
        addChangeOrderParam.setCustomerNo("C201711152010206581143");
        ChangeOrderConsignInfo changeOrderConsignInfo = new ChangeOrderConsignInfo();
        changeOrderConsignInfo.setAddress("这是一个测试地址");
        changeOrderConsignInfo.setConsigneePhone("13612342234");
        changeOrderConsignInfo.setConsigneeName("张武");
        addChangeOrderParam.setChangeOrderConsignInfo(changeOrderConsignInfo);

        List<ChangeProductSkuPairs> changeProductSkuPairsList = new ArrayList<>();
        ChangeProductSkuPairs changeProductSkuPairs = new ChangeProductSkuPairs();
        changeProductSkuPairs.setProductSkuIdSrc(40);
        changeProductSkuPairs.setProductSkuIdDest(40);
        changeProductSkuPairs.setChangeCount(2);
        changeProductSkuPairsList.add(changeProductSkuPairs);
        addChangeOrderParam.setChangeProductSkuPairsList(changeProductSkuPairsList);
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

        List<ChangeMaterialPairs> changeMaterialPairsList = new ArrayList<>();
        ChangeMaterialPairs changeMaterialPairs = new ChangeMaterialPairs();
        changeMaterialPairs.setMaterialNoSrc("M201711201356145971009");
        changeMaterialPairs.setMaterialNoDest("M201711201356145971009");
        changeMaterialPairs.setChangeCount(1);
        changeMaterialPairsList.add(changeMaterialPairs);
        addChangeOrderParam.setChangeMaterialPairsList(changeMaterialPairsList);
        TestResult testResult = getJsonTestResult("/changeOrder/add",addChangeOrderParam);
    }
    @Test
    public void update() throws Exception {
        UpdateChangeOrderParam addChangeOrderParam = new UpdateChangeOrderParam();
        addChangeOrderParam.setChangeOrderNo("CO201712152003206021155");
        ChangeOrderConsignInfo changeOrderConsignInfo = new ChangeOrderConsignInfo();
        changeOrderConsignInfo.setAddress("这是一个测试地址2");
        changeOrderConsignInfo.setConsigneePhone("13612342234");
        changeOrderConsignInfo.setConsigneeName("张三");
        addChangeOrderParam.setChangeOrderConsignInfo(changeOrderConsignInfo);

        List<ChangeProductSkuPairs> changeProductSkuPairsList = new ArrayList<>();
        ChangeProductSkuPairs changeProductSkuPairs = new ChangeProductSkuPairs();
        changeProductSkuPairs.setChangeOrderProductId(4);
        changeProductSkuPairs.setProductSkuIdSrc(40);
        changeProductSkuPairs.setProductSkuIdDest(40);
        changeProductSkuPairs.setChangeCount(3);
        changeProductSkuPairsList.add(changeProductSkuPairs);
        addChangeOrderParam.setChangeProductSkuPairsList(changeProductSkuPairsList);

        List<ChangeMaterialPairs> changeMaterialPairsList = new ArrayList<>();
        ChangeMaterialPairs changeMaterialPairs = new ChangeMaterialPairs();
        changeMaterialPairs.setMaterialNoSrc("M201711201356145971009");
        changeMaterialPairs.setMaterialNoDest("M201711201356145971009");
        changeMaterialPairs.setChangeCount(3);
        changeMaterialPairsList.add(changeMaterialPairs);
        addChangeOrderParam.setChangeMaterialPairsList(changeMaterialPairsList);
        TestResult testResult = getJsonTestResult("/changeOrder/update",addChangeOrderParam);
    }
    @Test
    public void commit() throws Exception {
        ChangeOrderCommitParam changeOrderCommitParam = new ChangeOrderCommitParam();
        changeOrderCommitParam.setChangeOrderNo("CO201712152003206021155");
        changeOrderCommitParam.setVerifyUserId(500006);
        changeOrderCommitParam.setRemark("审核备注");
        TestResult testResult = getJsonTestResult("/changeOrder/commit",changeOrderCommitParam);
    }

    @Test
    public void stockUpForChange() throws Exception {
        StockUpForChangeParam stockUpForChangeParam = new StockUpForChangeParam();
        stockUpForChangeParam.setChangeOrderNo("CO201712152003206021155");
        //select * from erp_product_equipment where sku_id=40 and equipment_status = 1 and data_status = 1 and order_no is null and current_warehouse_id = 4000002
//        stockUpForChangeParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010014");
        stockUpForChangeParam.setMaterialNo("M201711201356145971009");
        stockUpForChangeParam.setMaterialCount(1);
        stockUpForChangeParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        TestResult testResult = getJsonTestResult("/changeOrder/stockUpForChange",stockUpForChangeParam);
    }

    @Test
    public void delivery() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("CO201712152003206021155");
        TestResult testResult = getJsonTestResult("/changeOrder/delivery",changeOrder);
    }
    @Test
    public void doChangeEquipment() throws Exception {
        ChangeOrderProductEquipment changeOrderProductEquipment = new ChangeOrderProductEquipment();
        changeOrderProductEquipment.setChangeOrderNo("CO201712152003206021155");
        changeOrderProductEquipment.setSrcEquipmentNo("LX-EQUIPMENT-4000002-2017112010005");
        TestResult testResult = getJsonTestResult("/changeOrder/doChangeEquipment",changeOrderProductEquipment);
    }
    @Test
    public void doChangeMaterial() throws Exception {
        ChangeOrderMaterial changeOrderMaterial = new ChangeOrderMaterial();
        changeOrderMaterial.setChangeOrderMaterialId(3);
        changeOrderMaterial.setSrcChangeMaterialNo("M201711201356145971009");
        changeOrderMaterial.setRealChangeMaterialCount(2);
        TestResult testResult = getJsonTestResult("/changeOrder/doChangeMaterial",changeOrderMaterial);
    }
    @Test
    public void end() throws Exception {

    }
    @Test
    public void cancel() throws Exception {

    }
    @Test
    public void detail() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("CO201712121349549021303");
        TestResult testResult = getJsonTestResult("/changeOrder/detail",changeOrder);
    }
    @Test
    public void page() throws Exception {
        ChangeOrderPageParam changeOrderPageParam = new ChangeOrderPageParam();
        changeOrderPageParam.setOwnerName("文");
        TestResult testResult = getJsonTestResult("/changeOrder/page",changeOrderPageParam);
    }
    @Test
    public void pageChangeOrderProductEquipment() throws Exception {

    }
    @Test
    public void pageChangeOrderMaterialBulk() throws Exception {

    }
    @Test
    public void processNoChangeEquipment() throws Exception {

    }

}