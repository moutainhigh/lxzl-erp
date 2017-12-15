package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.changeOrder.*;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrder;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderConsignInfo;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderProductEquipment;
import com.lxzl.se.common.domain.Result;
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
        Result Result = getJsonTestResult("/changeOrder/add",addChangeOrderParam);
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
        Result Result = getJsonTestResult("/changeOrder/add",addChangeOrderParam);
    }
    @Test
    public void update() throws Exception {
        UpdateChangeOrderParam addChangeOrderParam = new UpdateChangeOrderParam();
        addChangeOrderParam.setChangeOrderNo("CO201712141454461351381");
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
        Result Result = getJsonTestResult("/changeOrder/update",addChangeOrderParam);
    }
    @Test
    public void commit() throws Exception {
        ChangeOrderCommitParam changeOrderCommitParam = new ChangeOrderCommitParam();
        changeOrderCommitParam.setChangeOrderNo("CO201712141454461351381");
        changeOrderCommitParam.setVerifyUserId(500006);
        changeOrderCommitParam.setRemark("审核备注");
        Result Result = getJsonTestResult("/changeOrder/commit",changeOrderCommitParam);
    }

    @Test
    public void stockUpForChange() throws Exception {
        StockUpForChangeParam stockUpForChangeParam = new StockUpForChangeParam();
        stockUpForChangeParam.setChangeOrderNo("CO201712141454461351381");
        //select * from erp_product_equipment where sku_id=40 and equipment_status = 1 and data_status = 1 and order_no is null and current_warehouse_id = 4000002
        stockUpForChangeParam.setEquipmentNo("LX-EQUIPMENT-4000002-2017112010011");
        stockUpForChangeParam.setOperationType(CommonConstant.COMMON_DATA_OPERATION_TYPE_ADD);
        Result Result = getJsonTestResult("/changeOrder/stockUpForChange",stockUpForChangeParam);
    }

    @Test
    public void delivery() throws Exception {
        ChangeOrder changeOrder = new ChangeOrder();
        changeOrder.setChangeOrderNo("CO201712141454461351381");
        Result Result = getJsonTestResult("/changeOrder/delivery",changeOrder);
    }
    @Test
    public void doChangeEquipment() throws Exception {
        ChangeOrderProductEquipment changeOrderProductEquipment = new ChangeOrderProductEquipment();
        changeOrderProductEquipment.setChangeOrderNo("CO201712141454461351381");
        changeOrderProductEquipment.setSrcEquipmentNo("LX-EQUIPMENT-4000002-2017112010005");
        Result Result = getJsonTestResult("/changeOrder/doChangeEquipment",changeOrderProductEquipment);
    }
    @Test
    public void doChangeMaterial() throws Exception {

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
        Result Result = getJsonTestResult("/changeOrder/detail",changeOrder);
    }
    @Test
    public void page() throws Exception {
        ChangeOrderPageParam changeOrderPageParam = new ChangeOrderPageParam();
        changeOrderPageParam.setOwnerName("文");
        Result Result = getJsonTestResult("/changeOrder/page",changeOrderPageParam);
    }
    @Test
    public void pageChangeOrderProductEquipment() throws Exception {

    }
    @Test
    public void pageChangeOrderMaterialBulk() throws Exception {

    }
    @Test
    public void deleteChangeOrderProductEquipment() throws Exception {

    }
    @Test
    public void deleteChangeOrderMaterialBulk() throws Exception {

    }

}