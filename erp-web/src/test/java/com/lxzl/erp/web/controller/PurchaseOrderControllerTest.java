package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.constant.PurchaseOrderStatus;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderCommitParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrderProduct;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PurchaseOrderControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addPurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setWarehouseId(4000001);//仓库ID必填
        purchaseOrder.setIsInvoice(1);//是否有发票字段必填
        purchaseOrder.setIsNew(1);//是否是全新机
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空

        List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表不能为空
        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
        purchaseOrderProduct.setProductSkuId(2); //采购单商品项SKU UD 不能为空
        purchaseOrderProduct.setProductAmount(new BigDecimal(9999));//采购单商品单价不能为空且大于0
        purchaseOrderProduct.setProductCount(2);//采购单商品数量不能为空且大于0
        purchaseOrderProductList.add(purchaseOrderProduct);
        purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        TestResult result = getJsonTestResult("/purchaseOrder/add",purchaseOrder);
    }
    @Test
    public void updatePurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711071624559555000051509");
        purchaseOrder.setWarehouseId(4000001);//仓库ID必填
        purchaseOrder.setIsInvoice(0);//是否有发票字段必填
        purchaseOrder.setIsNew(0);//是否是全新机
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空

        List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表不能为空
        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
        purchaseOrderProduct.setProductSkuId(2); //采购单商品项SKU UD 不能为空
        purchaseOrderProduct.setProductAmount(new BigDecimal(22));//采购单商品单价不能为空且大于0
        purchaseOrderProduct.setProductCount(2);//采购单商品数量不能为空且大于0

        purchaseOrderProductList.add(purchaseOrderProduct);
        purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        TestResult result = getJsonTestResult("/purchaseOrder/update",purchaseOrder);
    }
    @Test
    public void commit() throws Exception {
        PurchaseOrderCommitParam purchaseOrderCommitParam = new PurchaseOrderCommitParam();
        purchaseOrderCommitParam.setPurchaseOrderNo("C201711071720430655000051081");
        purchaseOrderCommitParam.setVerifyUserId(500006);
        TestResult result = getJsonTestResult("/purchaseOrder/commit",purchaseOrderCommitParam);
    }
    @Test
    public void delete() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711071624559555000051509");
        TestResult result = getJsonTestResult("/purchaseOrder/delete",purchaseOrder);
    }
    @Test
    public void page() throws Exception {
        PurchaseOrderQueryParam purchaseOrderQueryParam = new PurchaseOrderQueryParam();
//        purchaseOrderQueryParam.setPurchaseNo("C201711071624559555000051509");
//        purchaseOrderQueryParam.setProductSupplierId(1);
//        purchaseOrderQueryParam.setInvoiceSupplierId(1);
//        purchaseOrderQueryParam.setWarehouseId(4000001);
//        purchaseOrderQueryParam.setIsInvoice(1);
        purchaseOrderQueryParam.setIsNew(1);
        purchaseOrderQueryParam.setCreateStartTime(new Date());
        purchaseOrderQueryParam.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_PENDING);
        purchaseOrderQueryParam.setCommitStatus(CommonConstant.COMMON_CONSTANT_YES);
        TestResult result = getJsonTestResult("/purchaseOrder/page",purchaseOrderQueryParam);
    }
    @Test
    public void queryPurchaseOrderByNo() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711072100013815000051609");
        TestResult result = getJsonTestResult("/purchaseOrder/queryPurchaseOrderByNo",purchaseOrder);
    }
    @Test
    public void receiveVerifyResult(){
        boolean flag = purchaseOrderService.receiveVerifyResult(true,6000001);
    }

    @Autowired
    private PurchaseOrderService purchaseOrderService;
}