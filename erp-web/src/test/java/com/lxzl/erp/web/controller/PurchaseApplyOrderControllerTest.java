package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.PurchaseApplyOrderStatus;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderCommitParam;
import com.lxzl.erp.common.domain.purchaseApply.PurchaseApplyOrderPageParam;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrder;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrderMaterial;
import com.lxzl.erp.common.domain.purchaseApply.pojo.PurchaseApplyOrderProduct;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PurchaseApplyOrderControllerTest extends ERPUnTransactionalTest {
    @Test
    public void add() throws Exception {
        PurchaseApplyOrder purchaseApplyOrder = new PurchaseApplyOrder();
        purchaseApplyOrder.setAllUseTime(new Date());
        purchaseApplyOrder.setDepartmentId(400040);
        purchaseApplyOrder.setRemark("测试采购申请单");

        List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList = new ArrayList<>();
        PurchaseApplyOrderProduct purchaseApplyOrderProduct = new PurchaseApplyOrderProduct();
        purchaseApplyOrderProduct.setProductSkuId(40);
        purchaseApplyOrderProduct.setApplyCount(12);
        purchaseApplyOrderProduct.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderProductList.add(purchaseApplyOrderProduct);

        PurchaseApplyOrderProduct purchaseApplyOrderProduct2 = new PurchaseApplyOrderProduct();
        purchaseApplyOrderProduct2.setProductSkuId(34);
        purchaseApplyOrderProduct2.setApplyCount(1);
        purchaseApplyOrderProduct2.setUseTime(new Date());
        purchaseApplyOrderProduct2.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderProductList.add(purchaseApplyOrderProduct2);

        List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList = new ArrayList<>();
        PurchaseApplyOrderMaterial purchaseApplyOrderMaterial = new PurchaseApplyOrderMaterial();
        purchaseApplyOrderMaterial.setMaterialNo("M201711171838059981292");
        purchaseApplyOrderMaterial.setApplyCount(12);
        purchaseApplyOrderMaterial.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderMaterialList.add(purchaseApplyOrderMaterial);

        purchaseApplyOrder.setPurchaseApplyOrderProductList(purchaseApplyOrderProductList);
        purchaseApplyOrder.setPurchaseApplyOrderMaterialList(purchaseApplyOrderMaterialList);

        TestResult result = getJsonTestResult("/purchaseApplyOrder/add",purchaseApplyOrder);
    }
    @Test
    public void add2() throws Exception {
        PurchaseApplyOrder purchaseApplyOrder = new PurchaseApplyOrder();
        purchaseApplyOrder.setDepartmentId(400040);
        purchaseApplyOrder.setRemark("测试使用时间不一致的分页");

        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DAY_OF_MONTH,3);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH,5);
        List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList = new ArrayList<>();
        PurchaseApplyOrderProduct purchaseApplyOrderProduct = new PurchaseApplyOrderProduct();
        purchaseApplyOrderProduct.setProductSkuId(40);
        purchaseApplyOrderProduct.setApplyCount(12);
        purchaseApplyOrderProduct.setUseTime(calendar1.getTime());
        purchaseApplyOrderProduct.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderProductList.add(purchaseApplyOrderProduct);

        List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList = new ArrayList<>();
        PurchaseApplyOrderMaterial purchaseApplyOrderMaterial = new PurchaseApplyOrderMaterial();
        purchaseApplyOrderMaterial.setMaterialNo("M201711171838059981292");
        purchaseApplyOrderMaterial.setApplyCount(12);
        purchaseApplyOrderMaterial.setUseTime(calendar2.getTime());
        purchaseApplyOrderMaterial.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderMaterialList.add(purchaseApplyOrderMaterial);

        PurchaseApplyOrderMaterial purchaseApplyOrderMaterial2 = new PurchaseApplyOrderMaterial();
        purchaseApplyOrderMaterial2.setMaterialNo("M201711171838059981293");
        purchaseApplyOrderMaterial2.setApplyCount(1);
        purchaseApplyOrderMaterial2.setUseTime(calendar2.getTime());
        purchaseApplyOrderMaterial2.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderMaterialList.add(purchaseApplyOrderMaterial2);

        purchaseApplyOrder.setPurchaseApplyOrderProductList(purchaseApplyOrderProductList);
        purchaseApplyOrder.setPurchaseApplyOrderMaterialList(purchaseApplyOrderMaterialList);

        TestResult result = getJsonTestResult("/purchaseApplyOrder/add",purchaseApplyOrder);
    }
    @Test
    public void update() throws Exception {
        PurchaseApplyOrder purchaseApplyOrder = new PurchaseApplyOrder();
        purchaseApplyOrder.setPurchaseApplyOrderNo("LXPA-0755-2018010800005");
        purchaseApplyOrder.setDepartmentId(400040);
        purchaseApplyOrder.setRemark("测试采购申请单");

        List<PurchaseApplyOrderProduct> purchaseApplyOrderProductList = new ArrayList<>();
        PurchaseApplyOrderProduct purchaseApplyOrderProduct = new PurchaseApplyOrderProduct();
        purchaseApplyOrderProduct.setProductSkuId(40);
        purchaseApplyOrderProduct.setApplyCount(11);
        purchaseApplyOrderProduct.setUseTime(new Date());
        purchaseApplyOrderProduct.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderProductList.add(purchaseApplyOrderProduct);

        PurchaseApplyOrderProduct purchaseApplyOrderProduct2 = new PurchaseApplyOrderProduct();
        purchaseApplyOrderProduct2.setProductSkuId(34);
        purchaseApplyOrderProduct2.setApplyCount(1);
        purchaseApplyOrderProduct2.setUseTime(new Date());
        purchaseApplyOrderProduct2.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderProductList.add(purchaseApplyOrderProduct2);

        List<PurchaseApplyOrderMaterial> purchaseApplyOrderMaterialList = new ArrayList<>();
        PurchaseApplyOrderMaterial purchaseApplyOrderMaterial = new PurchaseApplyOrderMaterial();
        purchaseApplyOrderMaterial.setMaterialNo("M201711171838059981292");
        purchaseApplyOrderMaterial.setApplyCount(11);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        purchaseApplyOrderMaterial.setUseTime(calendar.getTime());
        purchaseApplyOrderMaterial.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderMaterialList.add(purchaseApplyOrderMaterial);

        PurchaseApplyOrderMaterial purchaseApplyOrderMaterial2 = new PurchaseApplyOrderMaterial();
        purchaseApplyOrderMaterial2.setMaterialNo("M201711171838059981293");
        purchaseApplyOrderMaterial2.setApplyCount(1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        purchaseApplyOrderMaterial2.setUseTime(calendar.getTime());
        purchaseApplyOrderMaterial2.setIsNew(CommonConstant.COMMON_CONSTANT_YES);
        purchaseApplyOrderMaterialList.add(purchaseApplyOrderMaterial2);

        purchaseApplyOrder.setPurchaseApplyOrderProductList(purchaseApplyOrderProductList);
        purchaseApplyOrder.setPurchaseApplyOrderMaterialList(purchaseApplyOrderMaterialList);
        TestResult result = getJsonTestResult("/purchaseApplyOrder/update",purchaseApplyOrder);
    }

    @Test
    public void cancel() throws Exception {
        PurchaseApplyOrder purchaseApplyOrder = new PurchaseApplyOrder();
        purchaseApplyOrder.setPurchaseApplyOrderNo("LXPA-0755-2018010800005");
        TestResult result = getJsonTestResult("/purchaseApplyOrder/cancel",purchaseApplyOrder);
    }

    @Test
    public void commit() throws Exception {
        PurchaseApplyOrderCommitParam purchaseApplyOrderCommitParam = new PurchaseApplyOrderCommitParam();
        purchaseApplyOrderCommitParam.setPurchaseApplyOrderNo("LXPA-0755-2018010800005");
        purchaseApplyOrderCommitParam.setVerifyUserId(500006);
        TestResult result = getJsonTestResult("/purchaseApplyOrder/commit",purchaseApplyOrderCommitParam);
    }

    @Test
    public void queryAll() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DAY_OF_MONTH,4);
        PurchaseApplyOrderPageParam purchaseApplyOrderPageParam = new PurchaseApplyOrderPageParam();
//        purchaseApplyOrderPageParam.setCreateStartTime(calendar.getTime());
//        purchaseApplyOrderPageParam.setCreateEndTime(new Date());
        purchaseApplyOrderPageParam.setUseStartTime(calendar.getTime());
        purchaseApplyOrderPageParam.setUseEndTime(calendar2.getTime());
//        purchaseApplyOrderPageParam.setApplyUserName("姜明");
//        purchaseApplyOrderPageParam.setDepartmentName("采购");
//        purchaseApplyOrderPageParam.setWarehouseName("分公司");
//        purchaseApplyOrderPageParam.setPurchaseApplyOrderNo("00001");
//        purchaseApplyOrderPageParam.setPurchaseApplyOrderStatus(PurchaseApplyOrderStatus.PURCHASE_APPLY_ORDER_STATUS_WAIT_PURCHASE);
        TestResult result = getJsonTestResult("/purchaseApplyOrder/queryAll",purchaseApplyOrderPageParam);
    }

    @Test
    public void queryByNo() throws Exception {
        PurchaseApplyOrder purchaseApplyOrder = new PurchaseApplyOrder();
        purchaseApplyOrder.setPurchaseApplyOrderNo("LXPA-0755-2018010800001");
        TestResult result = getJsonTestResult("/purchaseApplyOrder/queryByNo",purchaseApplyOrder);
    }

}