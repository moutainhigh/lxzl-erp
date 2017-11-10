package com.lxzl.erp.core.service.purchase;

import com.lxzl.erp.common.domain.Page;
import com.lxzl.erp.common.domain.ServiceResult;
import com.lxzl.erp.common.domain.purchase.PurchaseDeliveryOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderCommitParam;
import com.lxzl.erp.common.domain.purchase.PurchaseOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.PurchaseReceiveOrderQueryParam;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseDeliveryOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseOrder;
import com.lxzl.erp.common.domain.purchase.pojo.PurchaseReceiveOrder;
import com.lxzl.erp.core.service.VerifyReceiver;


public interface PurchaseOrderService extends VerifyReceiver {

    ServiceResult<String,String> add(PurchaseOrder purchaseOrder);
    ServiceResult<String,String> update(PurchaseOrder purchaseOrder);
    ServiceResult<String,PurchaseOrder> queryPurchaseOrderByNo(String purchaseNo);
    ServiceResult<String,Page<PurchaseOrder>> page(PurchaseOrderQueryParam purchaseOrderQueryParam);
    ServiceResult<String,Integer> commit(PurchaseOrderCommitParam purchaseOrderCommitParam);
    String  delete(PurchaseOrder purchaseOrder);
    ServiceResult<String,Page<PurchaseDeliveryOrder>> pagePurchaseDelivery(PurchaseDeliveryOrderQueryParam purchaseDeliveryOrderQueryParam);
    ServiceResult<String,PurchaseDeliveryOrder> queryPurchaseDeliveryOrderByNo(PurchaseDeliveryOrder purchaseDeliveryOrder);
    ServiceResult<String,String> updatePurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder);
    ServiceResult<String,String> commitPurchaseReceiveOrder(PurchaseReceiveOrder purchaseReceiveOrder);
    ServiceResult<String,Page<PurchaseReceiveOrder>> pagePurchaseReceive(PurchaseReceiveOrderQueryParam purchaseReceiveOrderQueryParam);
}
