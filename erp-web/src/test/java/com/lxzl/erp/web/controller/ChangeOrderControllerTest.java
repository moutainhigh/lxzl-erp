package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.changeOrder.AddChangeOrderParam;
import com.lxzl.erp.common.domain.changeOrder.ChangeMaterialPairs;
import com.lxzl.erp.common.domain.changeOrder.ChangeOrderCommitParam;
import com.lxzl.erp.common.domain.changeOrder.ChangeProductSkuPairs;
import com.lxzl.erp.common.domain.changeOrder.pojo.ChangeOrderConsignInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
        changeProductSkuPairs.setChangeCount(1);
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
    public void commit() throws Exception {
        ChangeOrderCommitParam changeOrderCommitParam = new ChangeOrderCommitParam();
        changeOrderCommitParam.setChangeOrderNo("CO201712121339447471266");
        changeOrderCommitParam.setVerifyUserId(500006);
        changeOrderCommitParam.setRemark("审核备注");
        TestResult testResult = getJsonTestResult("/changeOrder/commit",changeOrderCommitParam);
    }

    @Test
    public void stockUpByChange() throws Exception {
    }

}