package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrderProduct;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
 MAP.put(USER_CAN_NOT_OP_WAREHOUSE,"您没有该仓库的操作权限");
         MAP.put(VERIFY_USER_NOT_NULL,"审核人不能为空");
public class PurchaseOrderControllerTest extends ERPUnTransactionalTest {
    @Test
    public void addPurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setWarehouseId(1);//仓库ID必填
        purchaseOrder.setIsInvoice(1);//是否有发票字段必填
        purchaseOrder.setIsNew(1);//是否是全新机
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空

        List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表不能为空
        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
        purchaseOrderProduct.setProductSkuId(1); //采购单商品项SKU UD 不能为空
        purchaseOrderProduct.setProductAmount(new BigDecimal(10000));//采购单商品单价不能为空且大于0
        purchaseOrderProduct.setProductCount(2);//采购单商品数量不能为空且大于0
        purchaseOrderProductList.add(purchaseOrderProduct);
        purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        TestResult result = getJsonTestResult("/purchaseOrder/add",purchaseOrder);
    }
    @Test
    public void updatePurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711071148077225000011676");
        purchaseOrder.setWarehouseId(1);//仓库ID必填
        purchaseOrder.setIsInvoice(1);//是否有发票字段必填
        purchaseOrder.setIsNew(1);//是否是全新机
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空

        List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表不能为空
        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
        purchaseOrderProduct.setProductSkuId(1); //采购单商品项SKU UD 不能为空
        purchaseOrderProduct.setProductAmount(new BigDecimal(66));//采购单商品单价不能为空且大于0
        purchaseOrderProduct.setProductCount(2);//采购单商品数量不能为空且大于0
        purchaseOrderProductList.add(purchaseOrderProduct);
        purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        TestResult result = getJsonTestResult("/purchaseOrder/update",purchaseOrder);
    }
    @Test
    public void commit() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711070927074005000011390");
        TestResult result = getJsonTestResult("/purchaseOrder/commit",purchaseOrder);
    }

    @Test
    public void receiveVerifyResult(){
        boolean flag = purchaseOrderService.receiveVerifyResult(true,6000001);
    }

    @Autowired
    private PurchaseOrderService purchaseOrderService;
}