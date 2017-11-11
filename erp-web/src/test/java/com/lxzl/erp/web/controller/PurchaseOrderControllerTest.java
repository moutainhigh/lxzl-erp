package com.lxzl.erp.web.controller;

import com.lxzl.erp.ERPUnTransactionalTest;
import com.lxzl.erp.TestResult;
import com.lxzl.erp.common.constant.CommonConstant;
import com.lxzl.erp.common.domain.purchase.PurchaseDeliveryOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderCommitParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseReceiveOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.*;
import com.lxzl.erp.core.service.purchase.PurchaseOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PurchaseOrderControllerTest extends ERPUnTransactionalTest {

    /**
     * 测试自动过总公司的采购单
     * 条件：1.没有发票，2.收货库房为分公司
     * @throws Exception
     */
    @Test
    public void addPurchaseOrder() throws Exception {

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setWarehouseNo("W201708081508");//仓库编号必填,这里为分公司
        purchaseOrder.setIsInvoice(CommonConstant.COMMON_CONSTANT_NO);//是否有发票字段必填
        purchaseOrder.setIsNew(CommonConstant.COMMON_CONSTANT_NO);//是否是全新机
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空

        List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表不能为空
        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
        purchaseOrderProduct.setProductSkuId(1); //采购单商品项SKU UD 不能为空
        purchaseOrderProduct.setProductAmount(new BigDecimal(1100));//采购单商品单价不能为空且大于0
        purchaseOrderProduct.setProductCount(10);//采购单商品数量不能为空且大于0


        PurchaseOrderProduct purchaseOrderProduct2 = new PurchaseOrderProduct();
        purchaseOrderProduct2.setProductSkuId(2); //采购单商品项SKU UD 不能为空，且不能重复
        purchaseOrderProduct2.setProductAmount(new BigDecimal(1300));//采购单商品单价不能为空且大于0
        purchaseOrderProduct2.setProductCount(10);//采购单商品数量不能为空且大于0

        purchaseOrderProductList.add(purchaseOrderProduct);
        purchaseOrderProductList.add(purchaseOrderProduct2);

        purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        TestResult result = getJsonTestResult("/purchaseOrder/add",purchaseOrder);
    }
    @Test
    public void updatePurchaseOrder() throws Exception {

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711081828392315000051602");
        purchaseOrder.setWarehouseNo("W201708081508");//仓库编号必填,这里为分公司
        purchaseOrder.setIsInvoice(CommonConstant.COMMON_CONSTANT_NO);//是否有发票字段必填
        purchaseOrder.setIsNew(CommonConstant.COMMON_CONSTANT_NO);//是否是全新机
        purchaseOrder.setProductSupplierId(1);//商品供应商ID不能为空

        List<PurchaseOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表不能为空
        PurchaseOrderProduct purchaseOrderProduct = new PurchaseOrderProduct();
        purchaseOrderProduct.setProductSkuId(1); //采购单商品项SKU UD 不能为空
        purchaseOrderProduct.setProductAmount(new BigDecimal(800));//采购单商品单价不能为空且大于0
        purchaseOrderProduct.setProductCount(10);//采购单商品数量不能为空且大于0


        PurchaseOrderProduct purchaseOrderProduct2 = new PurchaseOrderProduct();
        purchaseOrderProduct2.setProductSkuId(2); //采购单商品项SKU UD 不能为空，且不能重复
        purchaseOrderProduct2.setProductAmount(new BigDecimal(2000));//采购单商品单价不能为空且大于0
        purchaseOrderProduct2.setProductCount(8);//采购单商品数量不能为空且大于0

        purchaseOrderProductList.add(purchaseOrderProduct);
        purchaseOrderProductList.add(purchaseOrderProduct2);

        purchaseOrder.setPurchaseOrderProductList(purchaseOrderProductList);
        TestResult result = getJsonTestResult("/purchaseOrder/update",purchaseOrder);
    }
    @Test
    public void commit() throws Exception {
        PurchaseOrderCommitParam purchaseOrderCommitParam = new PurchaseOrderCommitParam();
        purchaseOrderCommitParam.setPurchaseNo("C201711111120157125000051666");
        purchaseOrderCommitParam.setVerifyUserId(500006);
        TestResult result = getJsonTestResult("/purchaseOrder/commit",purchaseOrderCommitParam);
    }
    @Test
    public void delete() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711081745290995000051114");
        TestResult result = getJsonTestResult("/purchaseOrder/delete",purchaseOrder);
    }
    @Test
    public void page() throws Exception {
        PurchaseOrderQueryParam purchaseOrderQueryParam = new PurchaseOrderQueryParam();
//        purchaseOrderQueryParam.setPurchaseNo("C201711071624559555000051509");
//        purchaseOrderQueryParam.setProductSupplierId(1);
//        purchaseOrderQueryParam.setInvoiceSupplierId(1);
        purchaseOrderQueryParam.setWarehouseNo("W201708081508");
//        purchaseOrderQueryParam.setIsInvoice(1);
//        purchaseOrderQueryParam.setIsNew(1);
//        purchaseOrderQueryParam.setCreateStartTime(new Date());
//        purchaseOrderQueryParam.setCreateEndTime(new Date());
//        purchaseOrderQueryParam.setPurchaseOrderStatus(PurchaseOrderStatus.PURCHASE_ORDER_STATUS_WAIT_COMMIT);
//        purchaseOrderQueryParam.setCommitStatus(CommonConstant.COMMON_CONSTANT_YES);
        TestResult result = getJsonTestResult("/purchaseOrder/page",purchaseOrderQueryParam);
    }
    @Test
    public void queryPurchaseOrderByNo() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711111120157125000051666");
        TestResult result = getJsonTestResult("/purchaseOrder/queryPurchaseOrderByNo",purchaseOrder);
    }
    @Test
    public void receiveVerifyResult(){
        boolean flag = purchaseOrderService.receiveVerifyResult(true,6000001);
    }

    @Test
    public void pagePurchaseDelivery() throws Exception {
        PurchaseDeliveryOrderQueryParam purchaseOrderDeliveryQueryParam = new PurchaseDeliveryOrderQueryParam();
        purchaseOrderDeliveryQueryParam.setPurchaseNo("C201711091550440665000051897");
//        purchaseOrderDeliveryQueryParam.setWarehouseId(4000001);
//        purchaseOrderDeliveryQueryParam.setIsInvoice(1);
//        purchaseOrderDeliveryQueryParam.setIsNew(1);
//        purchaseOrderDeliveryQueryParam.setCreateEndTime(new Date());
//        purchaseOrderDeliveryQueryParam.setCreateStartTime(new Date());
//        purchaseOrderDeliveryQueryParam.setPurchaseDeliveryOrderStatus(0);
        TestResult result = getJsonTestResult("/purchaseOrder/pagePurchaseDelivery",purchaseOrderDeliveryQueryParam);
    }

    @Test
    public void queryPurchaseDeliveryOrderByNo() throws Exception {
        PurchaseDeliveryOrder purchaseDeliveryOrder = new PurchaseDeliveryOrder();
        purchaseDeliveryOrder.setPurchaseDeliveryNo("D2017110813565090960000051745");
        TestResult result = getJsonTestResult("/purchaseOrder/queryPurchaseDeliveryOrderByNo",purchaseDeliveryOrder);
    }

    @Test
    public void updatePurchaseReceiveOrder() throws Exception {
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        purchaseReceiveOrder.setPurchaseReceiveNo("R2017111111213674960000161292");
        purchaseReceiveOrder.setIsNew(CommonConstant.COMMON_CONSTANT_NO);

        List<PurchaseReceiveOrderProduct> purchaseOrderProductList = new ArrayList<>();//采购单商品项列表不能为空

        PurchaseReceiveOrderProduct purchaseReceiveOrderProduct = new PurchaseReceiveOrderProduct();
        purchaseReceiveOrderProduct.setRealProductSkuId(1); //采购单商品项SKU ID 不能为空
        purchaseReceiveOrderProduct.setRealProductCount(5);//采购单商品数量不能为空且大于0
        purchaseOrderProductList.add(purchaseReceiveOrderProduct);

//        PurchaseReceiveOrderProduct purchaseReceiveOrderProduct = new PurchaseReceiveOrderProduct();
//        purchaseReceiveOrderProduct.setRealProductSkuId(2); //采购单商品项SKU ID 不能为空
//        purchaseReceiveOrderProduct.setRealProductCount(5);//采购单商品数量不能为空且大于0
//        purchaseOrderProductList.add(purchaseReceiveOrderProduct);
//
        PurchaseReceiveOrderProduct purchaseReceiveOrderProduct2 = new PurchaseReceiveOrderProduct();
        purchaseReceiveOrderProduct2.setRealProductSkuId(3); //采购单商品项SKU ID 不能为空
        purchaseReceiveOrderProduct2.setRealProductCount(10);//采购单商品数量不能为空且大于0
        purchaseOrderProductList.add(purchaseReceiveOrderProduct2);
        purchaseReceiveOrder.setPurchaseReceiveOrderProductList(purchaseOrderProductList);
        TestResult result = getJsonTestResult("/purchaseOrder/updatePurchaseReceiveOrder",purchaseReceiveOrder);
     }

    @Test
    public void commitPurchaseReceiveOrder() throws Exception {
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        purchaseReceiveOrder.setPurchaseReceiveNo("R2017111111213674960000161292");
        TestResult result = getJsonTestResult("/purchaseOrder/commitPurchaseReceiveOrder",purchaseReceiveOrder);
    }


    @Test
    public void pagePurchaseReceive() throws Exception {
        PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam = new PurchaseReceiveOrderQueryParam();

        purchaseReceiveOrderQueryParam.setPurchaseOrderId(1);
        purchaseReceiveOrderQueryParam.setPurchaseDeliveryOrderId(1);
        purchaseReceiveOrderQueryParam.setWarehouseId(1);
        purchaseReceiveOrderQueryParam.setProductSupplierId(1);//商品供应商ID
        purchaseReceiveOrderQueryParam.setAutoAllotStatus(1);//分拨情况，0-未分拨，1-已分拨，2-被分拨，没发票的分公司仓库单，将生成一个总公司收货单，并生成分拨单号，自动分拨到分公司仓库
        purchaseReceiveOrderQueryParam.setIsInvoice(0);//是否有发票，0否1是
        purchaseReceiveOrderQueryParam.setIsNew(0);//是否全新机
        purchaseReceiveOrderQueryParam.setPurchaseReceiveOrderStatus(0);//采购单收货状态，0待收货，1已签单

        purchaseReceiveOrderQueryParam.setPurchaseNo("C201711091550440665000051897");//采购单编号
        purchaseReceiveOrderQueryParam.setPurchaseDeliveryNo("D2017110915515787360000061537");//发货单编号
        purchaseReceiveOrderQueryParam.setPurchaseReceiveMo("R2017110915515793660000061795"); //采购收货单编号
        purchaseReceiveOrderQueryParam.setWarehouseNo("W201708091508"); //仓库编号
        Calendar createStartTime  = Calendar.getInstance();
        createStartTime.set(Calendar.DAY_OF_YEAR,-1);
        Calendar createEndTime  = Calendar.getInstance();
        Calendar confirmStartTime  = Calendar.getInstance();
        confirmStartTime.set(Calendar.DAY_OF_YEAR,-1);
        Calendar confirmEndTime  = Calendar.getInstance();
        purchaseReceiveOrderQueryParam.setCreateStartTime(createStartTime.getTime());//创建收货单起始时间
        purchaseReceiveOrderQueryParam.setCreateEndTime(createEndTime.getTime());//创建收货单结束时间
//        purchaseReceiveOrderQueryParam.setConfirmStartTime(confirmStartTime.getTime());//确认签单起始时间
//        purchaseReceiveOrderQueryParam.setConfirmEndTime(confirmEndTime.getTime());//确认签单结束时间
        TestResult result = getJsonTestResult("/purchaseOrder/pagePurchaseReceive",purchaseReceiveOrderQueryParam);
    }

    @Test
    public void queryPurchaseReceiveOrderByNo() throws Exception {
        PurchaseReceiveOrder purchaseReceiveOrder = new PurchaseReceiveOrder();
        purchaseReceiveOrder.setPurchaseReceiveNo("R2017110915515793660000061795");
        TestResult result = getJsonTestResult("/purchaseOrder/queryPurchaseReceiveOrderByNo",purchaseReceiveOrder);
    }
    @Test
    public void endPurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711091550440665000051897");
        TestResult result = getJsonTestResult("/purchaseOrder/endPurchaseOrder",purchaseOrder);
    }
    @Test
    public void continuePurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseNo("C201711111120157125000051666");
        TestResult result = getJsonTestResult("/purchaseOrder/continuePurchaseOrder",purchaseOrder);
    }
    @Autowired
    private PurchaseOrderService purchaseOrderService;
}